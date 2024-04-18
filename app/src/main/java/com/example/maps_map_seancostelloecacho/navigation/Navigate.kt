package com.example.maps_map_seancostelloecacho.navigation

import MarkerListContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import com.example.maps_map_seancostelloecacho.views.*

@Composable
fun Navigate(
    navController: NavController,
    navControllerLR: NavController,
    TIME: Int,
    markerVM: MapViewModel
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Routes.MapGeolocalisationScreen.route,
        enterTransition = {
            fadeIn(animationSpec = tween(TIME)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, tween(TIME)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(TIME)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down, tween(TIME)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(TIME)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up, tween(TIME)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(TIME)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, tween(TIME)
            )
        }
    ) {
        composable(Routes.LoginScreen.route) {
            UserLoginOnLogOutContent(
                navController = navControllerLR,
                markerVM = markerVM
            )
        }
        composable(Routes.MapGeolocalisationScreen.route) {
            MapGeolocalisationScreen(
                navController,
                markerVM
            )
        }
        composable(Routes.MapScreen.route) { MapScreen(navController, markerVM) }
        composable(Routes.MarkerListScreen.route) { MarkerListContent(navController, markerVM) }
        composable(Routes.CameraScreen.route) { CameraScreen(markerVM, navController) }
        composable(Routes.GalleryScreen.route) { GalleryScreen(navController, markerVM) }
        composable(Routes.TakePhotoScreen.route) { TakePhotoScreen(markerVM, navController) }
    }
}