package com.example.maps_map_seancostelloecacho.models

import android.graphics.Bitmap

data class MarkerData(
    var id: String?,
    val name: String,
    val description: String?,
    val type: String,
    var uriUrl: String,
    val location: Location
) {
    constructor() : this(null, "", null,"", "", Location(0.0, 0.0))
}
