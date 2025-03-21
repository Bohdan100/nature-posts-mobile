package com.terra.natureposts.helpers

import android.content.Context
import android.net.Uri
import android.widget.Toast

import com.terra.natureposts.utils.Validator

object SettingsHelper {
    fun updateUsername(
        context: Context,
        newUsername: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        val nameError = Validator.validateName(newUsername)
        if (nameError != null) {
            Toast.makeText(context, nameError, Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuthHelper.updateUserName(newUsername, password,
            onSuccess = {
                Toast.makeText(context, "Username updated successfully", Toast.LENGTH_SHORT).show()
                onSuccess()
            },
            onFailure = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    fun updateAvatar(
        context: Context,
        imageUri: Uri?,
        onSuccess: () -> Unit
    ) {
        if (imageUri == null) {
            Toast.makeText(context, "Please select an image first", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = FirebaseAuthHelper.getCurrentUser()?.uid ?: ""
        FirebaseAuthHelper.updateUserAvatar(userId, imageUri,
            onSuccess = {
                Toast.makeText(context, "Avatar updated successfully", Toast.LENGTH_SHORT).show()
                onSuccess()
            },
            onFailure = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Обновление email пользователя
    fun updateEmail(
        context: Context,
        newEmail: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        if (!Validator.isValidEmail(newEmail)) {
            Toast.makeText(context, "Email must contain '@' and have at least 1 character before and 2 after '@'", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuthHelper.updateUserEmail(newEmail, password,
            onSuccess = {
                Toast.makeText(context, "Email updated successfully", Toast.LENGTH_SHORT).show()
                onSuccess()
            },
            onFailure = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Обновление пароля пользователя
    fun updatePassword(
        context: Context,
        newPassword: String,
        currentPassword: String,
        onSuccess: () -> Unit
    ) {
        if (!Validator.isValidPassword(newPassword)) {
            Toast.makeText(
                context,
                "Password must be at least 4 characters long and contain both letters and numbers",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (currentPassword.isEmpty()) {
            Toast.makeText(context, "Current password is required", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuthHelper.updateUserPassword(newPassword, currentPassword,
            onSuccess = {
                Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                onSuccess()
            },
            onFailure = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }
}