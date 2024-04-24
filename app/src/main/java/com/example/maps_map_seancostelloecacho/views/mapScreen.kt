package com.example.maps_map_seancostelloecacho.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(navigationController: NavController, markerVM: MapViewModel) {
    val markerList by markerVM.markerList.observeAsState(emptyList())
    val showBottomSheet by markerVM.showBottomSheetFromMap.observeAsState(false)
    val recentMarker by markerVM.recentMarker.observeAsState(null)
    val actualPosition by markerVM.actualPosition.observeAsState(LatLng(0.0, 0.0))
    val typeMarkerForFilter by markerVM.typeMarkerForFilter.observeAsState("")
    val darkThem by markerVM.darkThem.observeAsState(false)
    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(actualPosition, 18f)
        }
    val uiSetings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }

    if (typeMarkerForFilter == "All markers") {
        markerVM.getMarkers()
    } else {
        markerVM.getFilterMarkers(typeMarkerForFilter)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = markerVM.state.properties,
            uiSettings = uiSetings,
            onMapClick = {
                markerVM.changeActualPosition(it)
                markerVM.initializeRecentMarker(
                    name = "New Marker",
                    type = "Recent Marker",
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            },
            onMapLongClick = {
                markerVM.changeActualPosition(it)
                markerVM.restartMarkerAtributes()
                markerVM.changeShowBottomFromMapSheet(true)
                Log.i("map", "map q passa with $showBottomSheet")
            },
        ) {
            if (markerList.isNotEmpty()) {
                markerList.forEach { marker ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                marker.location.latitude,
                                marker.location.longitude
                            )
                        ),
                        title = marker.name,
                        snippet = if (marker.description != null) marker.description else "Any description",
                        icon = defaultMarker(if (darkThem) HUE_VIOLET else HUE_BLUE)
                    )
                }
            }
            if (recentMarker != null) {
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            recentMarker!!.location.latitude,
                            recentMarker!!.location.longitude
                        )
                    ),
                    title = recentMarker!!.name,
                    icon = defaultMarker(HUE_RED)
                )
            }
        }
        if (showBottomSheet)
            MyBottomSheetFromMapContent(navigationController = navigationController, markerVM = markerVM)
    }
}


