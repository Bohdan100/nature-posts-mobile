package com.terra.natureposts.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.terra.natureposts.R
import com.terra.natureposts.helpers.FirebaseDataHelper
import com.terra.natureposts.helpers.KeyboardHelper.setupHideKeyboardOnTouchOutside

class AddCommentActivity : AppCompatActivity() {
    private lateinit var commentEditText: EditText
    private lateinit var submitCommentButton: Button
    private lateinit var backButton: Button
    private lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_coment)
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

        commentEditText = findViewById(R.id.commentEditText)
        submitCommentButton = findViewById(R.id.submitCommentButton)
        backButton = findViewById(R.id.backButton)

        submitCommentButton.setOnClickListener {
            val commentText = commentEditText.text.toString()
            if (commentText.isNotEmpty() && commentText.length <= 30) {
                FirebaseDataHelper.addComment(postId, commentText,
                    onSuccess = {
                        Toast.makeText(this, "Comment added", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    },
                    onFailure = { error ->
                        Toast.makeText(this, "Failed to add comment: $error", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            } else {
                if (commentText.isEmpty()) {
                    Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Comment must be no more than 30 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}