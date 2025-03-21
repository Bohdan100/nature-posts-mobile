package com.terra.natureposts.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.terra.natureposts.R
import com.terra.natureposts.helpers.FirebaseAuthHelper
import com.terra.natureposts.helpers.KeyboardHelper.setupHideKeyboardOnTouchOutside

class LoginActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupHideKeyboardOnTouchOutside(findViewById(R.id.main))

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)

            findViewById<Button>(R.id.btnSignIn).setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuthHelper.signIn(this, email, password)
        }

        findViewById<TextView>(R.id.registerText).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}