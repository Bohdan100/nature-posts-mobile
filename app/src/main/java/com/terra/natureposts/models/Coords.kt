package com.terra.natureposts.models

import java.io.Serializable

data class Coords(
    val accuracy: Double = 0.0,
    val altitude: Double = 0.0,
    val altitudeAccuracy: Double = 0.0,
    val heading: Double = 0.0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val speed: Double = 0.0
) : Serializable