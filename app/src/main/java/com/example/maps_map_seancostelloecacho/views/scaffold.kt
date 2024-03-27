package com.example.maps_map_seancostelloecacho.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.maps_map_seancostelloecacho.navigation.Navigate
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel
import kotlinx.coroutines.launch

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyScaffold(
    navController: NavController,
    TIME : Int,
    markerVM: MarkerViewModel, state: DrawerState
) {
    Scaffold(
        topBar = {
            MyTopAppBarList(state = state, markerVM = markerVM)
        }
    ) { paddingValues ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Navigate(navController, TIME, markerVM)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarList(state: DrawerState, markerVM: MarkerViewModel) {
    val actualScreen by markerVM.actualScreen.observeAsState("")
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val expandedTopBar by markerVM.expandedTopBar.observeAsState(false)
    val expandedBottomSheet by markerVM.expandedBottomSheet.observeAsState(false)
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(text = "My SuperApp")},
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
                Icon( imageVector = Icons.Filled.Menu, tint = MaterialTheme.colorScheme.background, contentDescription = "Menu")
            }
        },
        actions = {
            TypeMarkerContent(
                actualScreen = actualScreen,
                typeMarker = typeMarker,
                onTypeMarkerChange = {markerVM.changeTypeMarker(it)},
                expandedTopBar = expandedTopBar,
                onExpandedTopBarChange = {markerVM.changeExpandedTopBar(it)},
                expandedBottomSheet = expandedBottomSheet,
                onExpandedBottomSheetChange = {markerVM.changeExpandedBottomSheet(it)},
                whenMarkerTypedChanged = {markerVM.whenMarkerTypedChanged(it)}
            )
        }
    )
}
