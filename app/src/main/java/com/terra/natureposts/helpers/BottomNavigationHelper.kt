package com.terra.natureposts.helpers

import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.terra.natureposts.ui.CreatePostActivity
import com.terra.natureposts.ui.PostsActivity
import com.terra.natureposts.ui.ProfilePostsActivity
import com.terra.natureposts.R

object BottomNavigationHelper {

    fun setupBottomNavigation(
        bottomNavigationView: BottomNavigationView,
        currentActivity: android.app.Activity,
        defaultSelectedItemId: Int
    ) {
        bottomNavigationView.selectedItemId = defaultSelectedItemId

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_posts -> {
                    if (currentActivity !is PostsActivity) {
                        currentActivity.startActivity(Intent(currentActivity, PostsActivity::class.java))
                        currentActivity.finish()
                    }
                    true
                }

                R.id.navigation_create_post -> {
                    if (currentActivity !is CreatePostActivity) {
                        currentActivity.startActivity(Intent(currentActivity, CreatePostActivity::class.java))
                        currentActivity.finish()
                    }
                    true
                }

                R.id.navigation_profile -> {
                    if (currentActivity !is ProfilePostsActivity) {
                        currentActivity.startActivity(Intent(currentActivity, ProfilePostsActivity::class.java))
                        currentActivity.finish()
                    }
                    true
                }

                else -> false
            }
        }
    }
}