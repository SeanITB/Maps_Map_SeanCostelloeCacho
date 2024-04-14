package com.example.maps_map_seancostelloecacho.models

import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(isMyLocationEnabled = true),
    val isFollautMap: Boolean = false
)
