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
import androidx.navigation.compose.rememberNavController
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import com.example.maps_map_seancostelloecacho.views.LunchScreen
import com.example.maps_map_seancostelloecacho.views.MyDrawer
import com.example.maps_map_seancostelloecacho.views.UserLoginContent
import com.example.maps_map_seancostelloecacho.views.UsernRegistrerContent

@Composable
fun NavigateLoginAndRegister(navControllerLR: NavController, TIME: Int, markerVM: MapViewModel) {
    NavHost(
        navController = navControllerLR as NavHostController,
        startDestination = Routes.LunchScreen.route,
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
        composable(Routes.LunchScreen.route) { LunchScreen(navControllerLR) }
        composable(Routes.RegisterScreen.route,) { UsernRegistrerContent(navControllerLR, markerVM) }
        composable(Routes.LoginScreen.route,) { UserLoginContent(navControllerLR, markerVM) }
        composable(Routes.MyDrawer.route) {
            val navController = rememberNavController()
            markerVM.changeTypeMarker("All markers") // toDo: ns si es un poco guarro hacerlo aqui
            MyDrawer(navController = navController, navControllerLR = navControllerLR, markerVM = markerVM, TIME = TIME)
        }

    }
}