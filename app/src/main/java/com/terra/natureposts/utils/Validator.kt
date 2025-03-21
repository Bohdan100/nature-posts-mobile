package com.terra.natureposts.utils

import android.util.Patterns

object Validator {
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                email.split("@")[0].isNotEmpty() &&
                email.split("@")[1].length >= 2
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 4 && password.any { it.isLetter() } && password.any { it.isDigit() }
    }

    fun validateName(name: String): String? {
        if (name.length < 2 || name.length > 10) {
            return "Name must be between 2 and 10 characters."
        }
        if (!name[0].isLetter()) {
            return "Name must start with a letter."
        }
        if (!name[0].isUpperCase()) {
            return "The first letter of the name must be uppercase."
        }
        if (name.substring(1).any { it.isUpperCase() }) {
            return "Only the first letter of the name can be uppercase."
        }
        if (!name.all { it.isLetterOrDigit() }) {
            return "Name can only contain letters and numbers."
        }
        return null
    }
}