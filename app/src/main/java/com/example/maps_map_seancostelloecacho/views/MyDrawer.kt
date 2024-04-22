package com.example.maps_map_seancostelloecacho.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.maps_map_seancostelloecacho.models.UserPrefs
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MyDrawer(navControllerLR: NavController, markerVM: MapViewModel, TIME: Int) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    var logUtCompleat by rememberSaveable {
        mutableStateOf(false)
    }
    val userName by markerVM.email.observeAsState("")
    val password by markerVM.password.observeAsState("")
    ModalNavigationDrawer(
        drawerState = state,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close menu",
                        modifier = Modifier.clickable {
                            scope.launch {
                                state.close()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(0.3f))
                    Text(text = "Drawer title")
                }
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Map") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            state.close()
                            navController.navigate(Routes.MapScreen.route)
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Marker list") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            state.close()
                            navController.navigate(Routes.MarkerListScreen.route)
                            //markerVM.getMarkers()
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Logout") },
                    selected = false,
                    onClick = {
                        val userPrefs = UserPrefs(context)
                        markerVM.logout()
                        CoroutineScope(Dispatchers.IO).launch {
                            logUtCompleat = userPrefs.saveUserData("", "")
                        }
                        markerVM.changeUserName("")
                        markerVM.changePassword("")
                    }
                )
            }
        }
    ) {
        LaunchedEffect(key1 = logUtCompleat) {
            if (logUtCompleat) {
                navControllerLR.navigate(Routes.AuthNavigation.route)
                logUtCompleat = false
            }
        }
        MyScaffold(navController, TIME, markerVM, state)
    }
}


