package com.terra.natureposts.models

data class Comment(
    val id: String = "",
    val author: String = "",
    val text: String = "",
    val userAvatar: String = "",
    val timestamp: Long = 0
)