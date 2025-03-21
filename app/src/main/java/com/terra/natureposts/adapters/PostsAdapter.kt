package com.terra.natureposts.adapters

import androidx.recyclerview.widget.RecyclerView
import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

import com.terra.natureposts.R
import com.terra.natureposts.models.Post

class PostsAdapter(
    private var posts: List<Post>,
    private val onDetailsClick: (Post) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postImage: ImageView = itemView.findViewById(R.id.postImage)
        private val postTitle: TextView = itemView.findViewById(R.id.postTitle)
        private val postAuthor: TextView = itemView.findViewById(R.id.postAuthor)
        private val postDescription: TextView = itemView.findViewById(R.id.postDescription)

        fun bind(post: Post) {
            Glide.with(itemView.context)
                .load(post.photo)
                .placeholder(R.drawable.default_post_image)
                .error(R.drawable.default_error_image)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(25)))
                .into(postImage)

            postTitle.text = "Title: ${post.title}"
            postAuthor.text = "Author: ${post.name}"
            postDescription.text = "Description: ${post.description}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)

        holder.itemView.findViewById<Button>(R.id.detailsButton).setOnClickListener {
            onDetailsClick(post)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePosts(newPosts: List<Post>) {
        this.posts = newPosts
        notifyDataSetChanged()
    }
}