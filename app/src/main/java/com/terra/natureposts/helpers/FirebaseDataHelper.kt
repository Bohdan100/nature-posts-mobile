package com.terra.natureposts.helpers

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query

import com.terra.natureposts.adapters.PostsAdapter
import com.terra.natureposts.models.Post
import com.terra.natureposts.models.Comment

object FirebaseDataHelper {
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    fun loadAllPostsFromFirestore(postsAdapter: PostsAdapter, onFailure: (String) -> Unit) {
        firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val posts = mutableListOf<Post>()
                for (document in querySnapshot) {
                    val post = document.toObject(Post::class.java)
                    posts.add(post)
                }
                postsAdapter.updatePosts(posts)
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message.toString())
            }
    }

    fun loadUserPostsFromFirestore(postsAdapter: PostsAdapter, onFailure: (String) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onFailure("User not found, please sign in or sign up")
            return
        }
        firestore.collection("posts")
            .whereEqualTo("userId", currentUser.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val posts = mutableListOf<Post>()
                for (document in querySnapshot) {
                    val post = document.toObject(Post::class.java)
                    posts.add(post)
                }
                postsAdapter.updatePosts(posts)
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message.toString())
            }
    }

    fun loadPost(postId: String, onSuccess: (Post) -> Unit, onFailure: (String) -> Unit) {
        firestore.collection("posts").document(postId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val post = document.toObject(Post::class.java)
                    post?.let(onSuccess)
                } else {
                    onFailure("Post not found")
                }
            }
            .addOnFailureListener { e ->
                onFailure("Failed to load post: ${e.message}")
            }
    }

    fun loadComments(postId: String, onSuccess: (List<Comment>) -> Unit, onFailure: (String) -> Unit) {
        firestore.collection("posts")
            .document(postId)
            .collection("comments")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val comments = mutableListOf<Comment>()
                for (document in documents) {
                    val comment = document.toObject(Comment::class.java)
                    comments.add(comment.copy(id = document.id))
                }
                onSuccess(comments)
            }
            .addOnFailureListener { exception ->
                onFailure("Failed to load comments: ${exception.message}")
            }
    }

    fun addComment(postId: String, commentText: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val comment = Comment(
            author = auth.currentUser?.displayName ?: "Anonymous",
            text = commentText,
            userAvatar = auth.currentUser?.photoUrl?.toString() ?: "",
            timestamp = System.currentTimeMillis()
        )

        firestore.collection("posts")
            .document(postId)
            .collection("comments")
            .add(comment)
            .addOnSuccessListener { documentReference ->
                val commentId = documentReference.id
                val updatedComment = comment.copy(id = commentId)
                documentReference.set(updatedComment)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure("Failed to update comment with ID: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                onFailure("Failed to add comment: ${e.message}")
            }
    }
}