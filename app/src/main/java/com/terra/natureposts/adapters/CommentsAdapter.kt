package com.terra.natureposts.adapters

import androidx.recyclerview.widget.RecyclerView
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide

import com.terra.natureposts.R
import com.terra.natureposts.models.Comment

class CommentsAdapter(private var comments: List<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val commentAuthor: TextView = itemView.findViewById(R.id.commentAuthor)
        private val commentText: TextView = itemView.findViewById(R.id.commentText)
        private val commentAvatar: ImageView = itemView.findViewById(R.id.commentAvatar)

        fun bind(comment: Comment) {
            commentAuthor.text = comment.author
            commentText.text = comment.text

            Glide.with(itemView.context)
                .load(comment.userAvatar)
                .circleCrop()
                .placeholder(R.drawable.default_avatar)
                .into(commentAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateComments(newComments: List<Comment>) {
        this.comments = newComments
        notifyDataSetChanged()
    }
}