package com.terra.natureposts.ui

import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.bumptech.glide.Glide

import com.terra.natureposts.R
import com.terra.natureposts.helpers.FirebaseAuthHelper
import com.terra.natureposts.helpers.KeyboardHelper.setupHideKeyboardOnTouchOutside
import com.terra.natureposts.helpers.SettingsHelper

class SettingsActivity : AppCompatActivity() {
    private lateinit var userAvatar: ImageView
    private lateinit var usernameEditText: EditText
    private lateinit var passwordNameEditText: EditText
    private lateinit var newEmailEditText: EditText
    private lateinit var passwordEmailEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var currentPasswordEditText: EditText
    private lateinit var userIdText: TextView
    private lateinit var userText: TextView
    private lateinit var currentEmailText: TextView

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            Glide.with(this).load(it).into(userAvatar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupHideKeyboardOnTouchOutside(findViewById(R.id.main))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        userAvatar = findViewById(R.id.userAvatar)
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordNameEditText = findViewById(R.id.passwordNameEditText)
        newEmailEditText = findViewById(R.id.newEmailEditText)
        passwordEmailEditText = findViewById(R.id.passwordEmailEditText)
        newPasswordEditText = findViewById(R.id.newPasswordEditText)
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText)
        userIdText = findViewById(R.id.userIdText)
        userText = findViewById(R.id.userText)
        currentEmailText = findViewById(R.id.currentEmailText)

        loadUserData()
    }

    private fun loadUserData() {
        val user = FirebaseAuthHelper.getCurrentUser()
        user?.let {
            if (selectedImageUri != null) {
                Glide.with(this).load(selectedImageUri).into(userAvatar)
            } else {
                Glide.with(this).load(it.photoUrl).into(userAvatar)
            }

            userText.text = it.displayName
            usernameEditText.setText(it.displayName)

            currentEmailText.text = it.email ?: "Email not available"
            userIdText.text = "My ID: ${it.uid}"
        }
    }

    fun onChangeUsernameClick(view: android.view.View) {
        val newUsername = usernameEditText.text.toString()
        val password = passwordNameEditText.text.toString()

        SettingsHelper.updateUsername(
            this,
            newUsername,
            password,
            onSuccess = {
                userText.text = newUsername
                usernameEditText.text.clear()
                passwordNameEditText.text.clear()
            }
        )
    }

    fun onAddPhotoClick(view: android.view.View) {
        pickImageLauncher.launch("image/*")
    }

    fun onChangeAvatarClick(view: android.view.View) {
        SettingsHelper.updateAvatar(
            this,
            selectedImageUri,
            onSuccess = {
                loadUserData()
            }
        )
    }

    fun onChangeEmailClick(view: android.view.View) {
        val newEmail = newEmailEditText.text.toString()
        val password = passwordEmailEditText.text.toString()

        SettingsHelper.updateEmail(
            this,
            newEmail,
            password,
            onSuccess = {
                currentEmailText.text = newEmail
                newEmailEditText.text.clear()
                passwordEmailEditText.text.clear()
            }
        )
    }

    fun onChangePasswordClick(view: android.view.View) {
        val newPassword = newPasswordEditText.text.toString()
        val currentPassword = currentPasswordEditText.text.toString()

        SettingsHelper.updatePassword(
            this,
            newPassword,
            currentPassword,
            onSuccess = {
                newPasswordEditText.text.clear()
                currentPasswordEditText.text.clear()
            }
        )
    }

    fun onReturnClick(view: android.view.View) {
        finish()
    }
}