package com.terra.natureposts.models

import java.io.Serializable

data class Post(
    var id: String = "",
    val name: String = "",
    val photo: String = "",
    val title: String = "",
    val description: String = "",
    val location: Coords = Coords(),
    val userId: String = "",
    val timestamp: Long = 0
): Serializable