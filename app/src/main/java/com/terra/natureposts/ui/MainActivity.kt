package com.terra.natureposts.ui

import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts

import com.terra.natureposts.R
import com.terra.natureposts.helpers.KeyboardHelper.setupHideKeyboardOnTouchOutside
import com.terra.natureposts.helpers.ImagePermissionHelper
import com.terra.natureposts.helpers.FirebaseAuthHelper

class MainActivity : AppCompatActivity() {
    private var imageUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            findViewById<ImageView>(R.id.userImage).setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupHideKeyboardOnTouchOutside(findViewById(R.id.main))

        findViewById<Button>(R.id.btnPhoto).setOnClickListener {
            ImagePermissionHelper.checkPermissionsAndPickImage(this) {
                pickImage.launch("image/*")
            }
        }

        findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            val nickname = findViewById<EditText>(R.id.nicknameInput).text.toString()
            val email = findViewById<EditText>(R.id.emailInput).text.toString()
            val password = findViewById<EditText>(R.id.passwordInput).text.toString()
            FirebaseAuthHelper.registerUser(this, nickname, email, password, imageUri)
        }

        findViewById<TextView>(R.id.loginText).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("grantResults[0] ${grantResults[0]}")
        ImagePermissionHelper.handlePermissionResult(
            requestCode,
            grantResults,
            onPermissionGranted = { pickImage.launch("image/*") },
            onPermissionDenied = { Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show() }
        )
    }
}