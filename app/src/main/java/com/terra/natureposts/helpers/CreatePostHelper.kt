package com.terra.natureposts.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import com.terra.natureposts.models.Coords
import com.terra.natureposts.models.Post
import com.terra.natureposts.ui.PostsActivity

object CreatePostHelper {
    private val auth = FirebaseAuth.getInstance()

    fun submitPost(
        context: Context,
        title: String,
        description: String,
        photoUri: Uri?,
        currentLocation: android.location.Location?
    ) {
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.length > 30) {
            Toast.makeText(context, "Title must be no more than 30 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.length > 50) {
            Toast.makeText(context, "Description must be no more than 50 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if (photoUri == null) {
            Toast.makeText(context, "Please capture a photo", Toast.LENGTH_SHORT).show()
            return
        }

        if (currentLocation == null) {
            Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
            return
        }

        uploadPhotoAndCreatePost(context, title, description, photoUri, currentLocation)
    }

    private fun uploadPhotoAndCreatePost(
        context: Context,
        title: String,
        description: String,
        photoUri: Uri,
        currentLocation: android.location.Location
    ) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${photoUri.lastPathSegment}")
        storageRef.putFile(photoUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val currentPhotoCoords = Coords(
                        accuracy = currentLocation.accuracy.toDouble(),
                        altitude = currentLocation.altitude,
                        altitudeAccuracy = 0.0,
                        heading = currentLocation.bearing.toDouble(),
                        latitude = currentLocation.latitude,
                        longitude = currentLocation.longitude,
                        speed = currentLocation.speed.toDouble()
                    )

                    val post = Post(
                        description = description,
                        location = currentPhotoCoords,
                        name = auth.currentUser?.displayName ?: "Unknown",
                        photo = uri.toString(),
                        title = title,
                        userId = auth.currentUser?.uid ?: "",
                        timestamp = System.currentTimeMillis()
                    )

                    val postsCollection = FirebaseFirestore.getInstance().collection("posts")
                    val newPostRef = postsCollection.document()
                    post.id = newPostRef.id

                    newPostRef.set(post)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Post created successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, PostsActivity::class.java)
                            context.startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Failed to create post: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to upload photo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}