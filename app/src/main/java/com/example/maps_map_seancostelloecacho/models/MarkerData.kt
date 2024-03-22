package com.example.maps_map_seancostelloecacho.models

import android.graphics.Bitmap

data class MarkerData(
    var id: String?,
    val name: String,
    val type: String,
    val description: String,
    val photos: MutableList<Bitmap>,
    val location: Location
) {
    constructor() : this(null, "", "", "", mutableListOf(), Location(0.0, 0.0))
}
