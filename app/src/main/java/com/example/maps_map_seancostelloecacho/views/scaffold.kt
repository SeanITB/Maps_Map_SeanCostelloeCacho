package com.example.maps_map_seancostelloecacho.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.models.MapEvent
import com.example.maps_map_seancostelloecacho.navigation.Navigate
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import kotlinx.coroutines.launch

@Composable
fun MyScaffold(
    navController: NavController,
    navControllerLR: NavController,
    TIME: Int,
    markerVM: MapViewModel, state: DrawerState
) {
    Scaffold(
        topBar = {
            MyTopAppBar(state = state, markerVM = markerVM)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    markerVM.onEvent(MapEvent.ToggleFalloutMap)
                }) {
                Icon(
                    imageVector = if (markerVM.state.isFollautMap) {
                        Icons.Default.ToggleOff
                    } else {
                        Icons.Default.ToggleOn
                    }, contentDescription = "Toggle Fallout map"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Navigate(navController, navControllerLR, TIME, markerVM)
            Button(onClick = {
                //Navegas a take photo
            }) {
                Icon(
                    imageVector = Icons.Filled.AddCircleOutline,
                    contentDescription = "Add marker with actual position",
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(state: DrawerState, markerVM: MapViewModel) {
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val expandedTopBar by markerVM.expandedTopBar.observeAsState(false)
    val listMarkerType by markerVM.listMarkerType.observeAsState(mutableListOf())
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = { Text(text = "My SuperApp") },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    state.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    tint = MaterialTheme.colorScheme.background,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            TypeMarkerScreen(
                arrTypeMarkers = listMarkerType,
                typeMarker = typeMarker,
                onTypeMarkerChange = { markerVM.changeTypeMarker(it) },
                expanded = expandedTopBar,
                onExpandedChange = { markerVM.changeExpandedTopBar(it) }
            )
        }
    )
}
