package com.example.maps_map_seancostelloecacho.models

import android.graphics.Bitmap

data class MarkerData(
    val name: String,
    val tipe: String,
    val description: String,
    val photos: MutableList<Bitmap>,
    val location: Location
)
