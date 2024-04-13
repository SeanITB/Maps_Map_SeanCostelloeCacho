package com.example.maps_map_seancostelloecacho.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel
import kotlinx.coroutines.launch

@Composable
fun MyDrawer(navController: NavController, markerVM: MarkerViewModel, TIME: Int) {

    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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
                            //markerVM.changeTypeMarker("All markers") toDo: ahora me da paereza mejorarlo
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
                            markerVM.changeTypeMarker("All markers")
                            navController.navigate(Routes.MarkerListScreen.route)
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Log aut") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            state.close()
                            markerVM.logout()
                            navController.navigate(Routes.LoginScreen.route)
                        }
                    }
                )
            }
        }
    ) {
        MyScaffold(navController, TIME, markerVM, state)
    }
}


