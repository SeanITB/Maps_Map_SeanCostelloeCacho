package com.example.maps_map_seancostelloecacho.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel
import com.example.maps_map_seancostelloecacho.views.CameraScreen
import com.example.maps_map_seancostelloecacho.views.GalleryScreen
import com.example.maps_map_seancostelloecacho.views.LunchScreen
import com.example.maps_map_seancostelloecacho.views.MapGeolocalisationScreen
import com.example.maps_map_seancostelloecacho.views.MapScreen
import com.example.maps_map_seancostelloecacho.views.MarkerListByCategoriesScreen
import com.example.maps_map_seancostelloecacho.views.MarkerListScreen
import com.example.maps_map_seancostelloecacho.views.TakePhotoScreen
import com.example.maps_map_seancostelloecacho.views.UserLoginContent

@Composable
fun Navigate(navController: NavController, TIME: Int, markerVM: MarkerViewModel) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Routes.LoginScreen.route,
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
        composable(Routes.LunchScreen.route) { LunchScreen(navController) }
        composable(Routes.LoginScreen.route,) { UserLoginContent(markerVM) }
        composable(Routes.MapGeolocalisationScreen.route) { MapGeolocalisationScreen(navController, markerVM) }
        composable(Routes.MapScreen.route) { MapScreen(navController, markerVM) }
        composable(Routes.MarkerListScreen.route,) { MarkerListScreen(markerVM) }
        composable(Routes.CameraScreen.route,) { CameraScreen(markerVM, navController) }
        composable(Routes.GalleryScreen.route,) { GalleryScreen(markerVM) }
        composable(Routes.TakePhotoScreen.route,) { TakePhotoScreen(markerVM, navController) }
    }
}