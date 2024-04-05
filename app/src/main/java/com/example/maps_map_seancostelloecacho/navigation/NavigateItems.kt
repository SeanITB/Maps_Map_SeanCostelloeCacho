package com.example.maps_map_seancostelloecacho.navigation

sealed class NavigationItems(val route: String, val label: String) {
    object CameraScreen: NavigationItems(Routes.CameraScreen.route, "cameraScreen")
    object GalleryScreen: NavigationItems(Routes.GalleryScreen.route, "galleryScreen")
    object MapGeolocalisationScreen: NavigationItems(Routes.MapGeolocalisationScreen.route, "mapGeolocalisationScreen")
}