package com.example.maps_map_seancostelloecacho.navigation

sealed class Routes (val route: String) {
    object LunchScreen: Routes("lunchScreen")
    object RegisterScreen: Routes("registerScreen")
    object LoginScreen: Routes("loginScreen")
    object MapGeolocalisationScreen: Routes("mapGeolocalisationScreen")
    object MapScreen: Routes("mapScreen")
    object MarkerListScreen: Routes("markerListScreen")
    object MarkerFilterListScreen: Routes("markerFilterListScreen")
    object CameraScreen: Routes("cameraScreen")
    object GalleryScreen: Routes("galleryScreen")
    object TakePhotoScreen: Routes("takePhotoScreen")
    object MyDrawer: Routes("myDrawer")
}