package com.example.maps_map_seancostelloecacho.views

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.MainActivity
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(navigationController: NavController, markerVM: MapViewModel) {
    val markerList by markerVM.markerList.observeAsState(emptyList())
    val showBottomSheet by markerVM.showBottomSheet.observeAsState(false)
    val getMarkersComlet by markerVM.markersComplet.observeAsState(false)
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val context = LocalContext.current
    val recentMarker by markerVM.recentMarker.observeAsState(null)
    val actualPosition by markerVM.actualPosition.observeAsState(LatLng(0.0, 0.0))



    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(actualPosition, 18f)
        }
    val uiSetings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }
    markerVM.changeActualScreen("mapScreen")
    LaunchedEffect(key1 = typeMarker) {
        if (typeMarker.equals("All markers")) {
            markerVM.getMarkers()
        } else {
            markerVM.getFilterMarkers()
        }
        markerVM.changeMarkerComplete(true)
    }
    LaunchedEffect(key1 = getMarkersComlet) {
        markerVM.changeMarkerComplete(false)
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
                    description = "Hello!!!",
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            },
            onMapLongClick = {
                markerVM.changeActualPosition(it)
                markerVM.restartMarkerAtributes()
                markerVM.changeLatitude(it.latitude)
                markerVM.changeLongitude(it.longitude)
                markerVM.changeShowBottomSheet(true)
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
                        snippet = "Marker at ${markerVM.nameMarker.value}",
                        icon = BitmapDescriptorFactory.defaultMarker(HUE_VIOLET)
                    )
                }
            }
            Log.i("RECENT_MARKER", "Recent marker: $recentMarker")
            if (recentMarker != null) {
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            recentMarker!!.location.latitude,
                            recentMarker!!.location.longitude
                        )
                    ),
                    title = recentMarker!!.name,
                    snippet = recentMarker!!.description,
                    icon = defaultMarker(HUE_RED)
                )
            }
        }
        //AddActualPositionMarkerContent(markerVM)
        if (showBottomSheet)
            MyBottomSheetContent(navigationController = navigationController, markerVM = markerVM)
    }
}

/*
@Composable
fun AddActualPositionMarkerContent(markerVM: MapViewModel) {
    ConstraintLayout {
        val (addActualPossition) = createRefs()
        Button(
            onClick = {
                markerVM.restartMarkerAtributes()
                markerVM.changeLatitude(0.0)
                markerVM.changeLongitude(0.0)
                markerVM.changeShowBottomSheet(true)
            },
            modifier = Modifier.constrainAs(addActualPossition) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "New Marker",
            )
        }
    }
}

 */


