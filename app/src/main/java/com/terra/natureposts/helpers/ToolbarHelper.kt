package com.terra.natureposts.helpers

import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

import com.terra.natureposts.ui.LoginActivity
import com.terra.natureposts.R

object ToolbarHelper {
    fun setupToolbar(activity: AppCompatActivity, toolbar: Toolbar, auth: FirebaseAuth) {
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userNameTextView = toolbar.findViewById<TextView>(R.id.userName)
            userNameTextView.text = currentUser.displayName ?: "Unknown User"

            val userAvatarImageView = toolbar.findViewById<ImageView>(R.id.userAvatar)
            val userAvatarUrl = currentUser.photoUrl?.toString()

            if (!userAvatarUrl.isNullOrEmpty()) {
                Glide.with(activity)
                    .load(userAvatarUrl)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(userAvatarImageView)
            } else {
                userAvatarImageView.setImageResource(R.drawable.default_avatar)
            }
        } else {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
            activity.finish()
            Toast.makeText(activity, "User not found, please sign in or sign up", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleLogout(activity: AppCompatActivity, auth: FirebaseAuth) {
        auth.signOut()
        activity.startActivity(Intent(activity, LoginActivity::class.java))
        activity.finish()
    }
}