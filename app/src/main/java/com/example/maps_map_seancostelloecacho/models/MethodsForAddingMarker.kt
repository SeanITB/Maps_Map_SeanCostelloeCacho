package com.example.maps_map_seancostelloecacho.models

data class MethodsForAddingMarker(
    val changeShowBottomFromMapSheet: (Boolean) -> Unit,
    val addMarker: () -> Unit,
    val restartMarkerAtributes: (Boolean) -> Unit
)
