package com.terra.natureposts.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

import com.terra.natureposts.R
import com.terra.natureposts.adapters.PostsAdapter
import com.terra.natureposts.helpers.BottomNavigationHelper
import com.terra.natureposts.helpers.FirebaseDataHelper
import com.terra.natureposts.helpers.ToolbarHelper

class PostsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: Toolbar

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_posts)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        toolbar = findViewById(R.id.toolbar)
        ToolbarHelper.setupToolbar(this, toolbar, auth)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        postsAdapter = PostsAdapter(emptyList()) { post ->
            val intent = Intent(this, PostDetailsActivity::class.java)
            intent.putExtra("postId", post.id)
            startActivity(intent)
        }
        recyclerView.adapter = postsAdapter

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        BottomNavigationHelper.setupBottomNavigation(
            bottomNavigationView,
            this,
            R.id.navigation_posts
        )

        FirebaseDataHelper.loadAllPostsFromFirestore(postsAdapter) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        ToolbarHelper.setupToolbar(this, toolbar, auth)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                ToolbarHelper.handleLogout(this, auth)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}