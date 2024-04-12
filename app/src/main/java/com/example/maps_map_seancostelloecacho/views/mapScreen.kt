package com.example.maps_map_seancostelloecacho.views

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.MainActivity
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED
import com.google.android.gms.maps.model.BitmapDescriptorFactory.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(navigationController: NavController, markerVM: MarkerViewModel) {
    val markerList by markerVM.markerList.observeAsState(emptyList())
    val showBottomSheet by markerVM.showBottomSheet.observeAsState(false)
    val getMarkersComlet by markerVM.markersComplet.observeAsState(false)
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val context = LocalContext.current
    val fusedLocationProviderClient =
        remember {
            LocationServices.getFusedLocationProviderClient(context)
        }
    var lastKnowLocation by remember {
        mutableStateOf<Location?>(null)
    }
    var deviceLatLng by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }
    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(deviceLatLng, 18f)
        }
    val locationResult = fusedLocationProviderClient.getCurrentLocation(100, null)
    locationResult.addOnCompleteListener(context as MainActivity) { task ->
        if (task.isSuccessful) {
            lastKnowLocation = task.result
            deviceLatLng = LatLng(lastKnowLocation!!.latitude, lastKnowLocation!!.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLatLng, 18f)
        } else {
            Log.e("ERROR", "Exception: %s", task.exception)
        }
    }
    markerVM.changeActualScreen("mapScreen")
    Log.i("markerType", "marker type: $typeMarker")
    LaunchedEffect(key1 = typeMarker) {
        if (typeMarker.equals("All markers")) {
            markerVM.getMarkers()
        } else {
            markerVM.getFilterMarkers()

        }
    }
    Log.i("MARKERS", "$getMarkersComlet")
    LaunchedEffect(key1 = getMarkersComlet) {
        Log.i("MARKERS", "Markers in map $markerList")
        markerVM.setMarkerComplete(false)
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {},
            onMapLongClick = {
                markerVM.changeLatitude(it.latitude)
                markerVM.changeLongitude(it.longitude)
                markerVM.changeTypeMarker("")
                markerVM.changeShowBottomSheet(true) },
            properties = MapProperties(isMyLocationEnabled = true)
        ){
            if(markerList.isNotEmpty()){
                markerList.forEach { marker ->
                    Marker(
                        state = MarkerState(position = LatLng(marker.location.latitude, marker.location.longitude)),
                        title = marker.name,
                        snippet = "Marker at ${markerVM.nameMarker.value}",
                        icon = BitmapDescriptorFactory.defaultMarker(HUE_VIOLET)
                    )
                }
            }

        }
        if (showBottomSheet)
            MyBottomSheetScreen(navigationController = navigationController, markerVM = markerVM)
    }
}


