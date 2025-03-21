package com.terra.natureposts.helpers

import android.net.Uri
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage

import com.terra.natureposts.ui.MainActivity
import com.terra.natureposts.ui.LoginActivity
import com.terra.natureposts.ui.PostsActivity
import com.terra.natureposts.utils.Validator

object FirebaseAuthHelper {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun registerUser(
        activity: MainActivity,
        nickname: String,
        email: String,
        password: String,
        imageUri: Uri?
    ) {
        val nameError = Validator.validateName(nickname)
        if (nameError != null) {
            Toast.makeText(activity, nameError, Toast.LENGTH_SHORT).show()
            return
        }
        if (!Validator.isValidEmail(email)) {
            Toast.makeText(
                activity,
                "Email must contain '@' and have at least 1 character before and 2 after '@'",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (!Validator.isValidPassword(password)) {
            Toast.makeText(
                activity,
                "Password must be at least 4 characters long and contain both letters and numbers",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    if (imageUri != null) {
                        uploadImageAndUpdateProfile(activity, it.uid, nickname, imageUri)
                    } else {
                        updateProfileWithDefaultAvatar(activity, nickname)
                    }
                }
            } else {
                Toast.makeText(
                    activity,
                    "Registration failed: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun signIn(activity: LoginActivity, email: String, password: String) {
        if (!Validator.isValidEmail(email)) {
            Toast.makeText(
                activity,
                "Email must contain '@' and have at least 1 character before and 2 after '@'",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT).show()
                    activity.startActivity(Intent(activity, PostsActivity::class.java))
                    activity.finish()
                } else {
                    Toast.makeText(activity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun uploadImageAndUpdateProfile(
        activity: MainActivity,
        userId: String,
        nickname: String,
        imageUri: Uri
    ) {
        val storageRef = storage.reference.child("avatars/$userId.jpg")
        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                updateUserProfile(activity, nickname, uri)
            }
        }.addOnFailureListener {
            Toast.makeText(activity, "Image upload failed: ${it.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun updateProfileWithDefaultAvatar(
        activity: MainActivity,
        nickname: String,
    ) {
        val defaultAvatarRef = storage.reference.child("avatars/default-avatar.png")
        defaultAvatarRef.downloadUrl.addOnSuccessListener { uri ->
            updateUserProfile(activity, nickname, uri)
        }.addOnFailureListener {
            Toast.makeText(activity, "Failed to load default avatar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserProfile(
        activity: MainActivity,
        nickname: String,
        photoUrl: Uri
    ) {
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(nickname)
            .setPhotoUri(photoUrl)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Registration successful", Toast.LENGTH_SHORT).show()
                activity.startActivity(Intent(activity, PostsActivity::class.java))
                activity.finish()
            } else {
                Toast.makeText(
                    activity,
                    "Profile update failed: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun updateUserAvatar(
        userId: String,
        imageUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val storageRef = storage.reference.child("avatars/$userId.jpg")
        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val user = auth.currentUser
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build()

                user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure("Failed to update avatar: ${task.exception?.message}")
                    }
                }
            }
        }.addOnFailureListener {
            onFailure("Image upload failed: ${it.message}")
        }
    }

    fun updateUserName(
        newName: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val user = auth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, password)
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(newName)
                        .build()
                    user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            onFailure("Failed to update name: ${task.exception?.message}")
                        }
                    }
                } else {
                    onFailure("Reauthentication failed: ${reauthTask.exception?.message}")
                }
            }
        } else {
            onFailure("User not found")
        }
    }

    fun updateUserEmail(
        newEmail: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val user = auth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, password)
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updateEmail(newEmail).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            onFailure("Failed to update email: ${task.exception?.message}")
                        }
                    }
                } else {
                    onFailure("Reauthentication failed: ${reauthTask.exception?.message}")
                }
            }
        } else {
            onFailure("User not found")
        }
    }

    fun updateUserPassword(
        newPassword: String,
        currentPassword: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val user = auth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            onFailure("Failed to update password: ${task.exception?.message}")
                        }
                    }
                } else {
                    onFailure("Reauthentication failed: ${reauthTask.exception?.message}")
                }
            }
        } else {
            onFailure("User not found")
        }
    }
}