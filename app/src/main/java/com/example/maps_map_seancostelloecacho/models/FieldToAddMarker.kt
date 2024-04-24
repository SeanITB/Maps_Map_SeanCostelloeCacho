package com.example.maps_map_seancostelloecacho.models

import android.content.Context
import android.net.Uri

data class FieldToAddMarker(
    val context: Context,
    val name: String,
    val type: String,
    val photo: Uri?
)
