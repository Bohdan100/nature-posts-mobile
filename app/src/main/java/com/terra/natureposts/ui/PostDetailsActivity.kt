package com.terra.natureposts.ui

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import androidx.exifinterface.media.ExifInterface.*
import java.io.InputStream
import java.io.FileOutputStream
import java.io.File
import java.net.URL

import com.terra.natureposts.R
import com.terra.natureposts.adapters.CommentsAdapter
import com.terra.natureposts.helpers.FirebaseDataHelper
import com.terra.natureposts.helpers.ImageSizeHelper
import com.terra.natureposts.helpers.KeyboardHelper.setupHideKeyboardOnTouchOutside
import com.terra.natureposts.models.Post


class PostDetailsActivity : AppCompatActivity() {
    private var post: Post? = null
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var postId: String

    private val addCommentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            FirebaseDataHelper.loadComments(postId, commentsAdapter::updateComments) { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupHideKeyboardOnTouchOutside(findViewById(R.id.main))

        postId = intent.getStringExtra("postId") ?: run {
            Toast.makeText(this, "Post not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        FirebaseDataHelper.loadPost(postId, ::onPostLoaded) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            finish()
        }

        val commentsRecyclerView = findViewById<RecyclerView>(R.id.commentsRecyclerView)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)

        commentsAdapter = CommentsAdapter(emptyList())
        commentsRecyclerView.adapter = commentsAdapter

        FirebaseDataHelper.loadComments(postId, commentsAdapter::updateComments) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.locationButton).setOnClickListener {
            val intent = Intent(this, PostMapActivity::class.java)
            intent.putExtra("latitude", post?.location?.latitude)
            intent.putExtra("longitude", post?.location?.longitude)
            startActivity(intent)
        }

        findViewById<Button>(R.id.addCommentButton).setOnClickListener {
            val intent = Intent(this, AddCommentActivity::class.java)
            intent.putExtra("postId", postId)
            addCommentLauncher.launch(intent)
        }

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    private fun onPostLoaded(post: Post) {
        this.post = post
        findViewById<TextView>(R.id.postTitle).text = "Title: ${post.title}"
        findViewById<TextView>(R.id.postAuthor).text = "Author: ${post.name}"
        findViewById<TextView>(R.id.postDescription).text = "Description: ${post.description}"
        findViewById<TextView>(R.id.postLocation).text =
            "Location: (${post.location.latitude}, ${post.location.longitude})"

        val postImage = findViewById<SubsamplingScaleImageView>(R.id.postImage)
        ImageSizeHelper.loadImage(this, post.photo, postImage)
    }
}