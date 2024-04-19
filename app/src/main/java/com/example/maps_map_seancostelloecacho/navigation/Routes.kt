package com.example.maps_map_seancostelloecacho.navigation

sealed class Routes (val route: String) {
    object LunchScreen: Routes("lunchScreen")
    object RegisterScreen: Routes("registerScreen")
    object LoginScreen: Routes("loginScreen")
    object UserLoginOnLogOutContent: Routes("userLoginOnLogOutContent")
    object MapGeolocalisationScreen: Routes("mapGeolocalisationScreen")
    object AuthNavigation: Routes("authNavigation")
    object MapScreen: Routes("mapScreen")
    object MarkerListScreen: Routes("markerListScreen")
    object CameraFromMapScreen: Routes("cameraFromMapScreen")
    object CameraFromMarkerListScreen: Routes("cameraFromMarkerListScreen")
    object GalleryScreen: Routes("galleryScreen")
    object TakePhotoFromMapScreen: Routes("takePhotoFromMapScreen")
    object TakePhotoFromMarkerListScreen: Routes("takePhotoFromMarkerListScreen")
    object MyDrawer: Routes("myDrawer")
}