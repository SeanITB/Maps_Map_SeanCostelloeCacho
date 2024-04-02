package com.example.maps_map_seancostelloecacho.navigation

sealed class Navigation(val route: String, val label: String) {
    object CameraScreen: Navigation(Routes.CameraScreen.route, "cameraScreen")
    object GalleryScreen: Navigation(Routes.GalleryScreen.route, "galleryScreen")
}