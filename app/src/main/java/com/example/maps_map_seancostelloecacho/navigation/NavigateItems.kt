package com.example.maps_map_seancostelloecacho.navigation

sealed class NavigationItems(val route: String, val label: String) {

    object CameraFromMapScreen: NavigationItems(Routes.CameraFromMapScreen.route, "cameraFromMapScreen")
    object CameraFromMarkerListScreen: NavigationItems(Routes.CameraFromMarkerListScreen.route, "cameraFromMarkerListScreen")

    object GalleryScreen: NavigationItems(Routes.GalleryScreen.route, "galleryScreen")
    object MapGeolocalisationScreen: NavigationItems(Routes.MapGeolocalisationScreen.route, "mapGeolocalisationScreen")
    object MapScreen: NavigationItems(Routes.MapScreen.route, "mapScreen")
    object MarkerCategoriesListScreen: NavigationItems(Routes.MarkerListScreen.route, "markerListScreen")
    object TakePhotoFromMapScreen: NavigationItems(Routes.TakePhotoFromMapScreen.route, "takePhotoFromMapScreen")
    object TakePhotoFromMarkerListScreen: NavigationItems(Routes.TakePhotoFromMarkerListScreen.route, "takePhotoFromMarkerListScreen")
}