package com.example.maps_map_seancostelloecacho.models

data class MethodsForAddingMarker(
    val changeShowBottomSheet: (Boolean) -> Unit,
    val addMarker: () -> Unit,
    val restartMarkerAtributes: () -> Unit
)
