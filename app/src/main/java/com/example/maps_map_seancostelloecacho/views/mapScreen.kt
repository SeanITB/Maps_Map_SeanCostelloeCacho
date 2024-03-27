package com.example.maps_map_seancostelloecacho.views

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
    markerVM.changeActualScreen("mapScreen")
    //val markerList by markerVM.filterMarkerList.observeAsState(emptyList())
    val markerList2 by markerVM.markerList.observeAsState(emptyList())
    val showBottomSheet by markerVM.showBottomSheet.observeAsState(false)
    markerVM.getMarkers()
    //markerVM.createFilerList()
    Log.i("TAMAÑO", "VIEW " + markerList2.size)
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
            if(markerList2.isNotEmpty()){
                Log.i("CONTENIDO", "$markerList2")
                markerList2.forEach { marker ->
                    Marker(
                        state = MarkerState(position = LatLng(marker.location.latitude, marker.location.longitude)),
                        title = marker.name,
                        snippet = "Marker at ${markerVM.nameMarker.value}"
                    )
                }
            }

        }
        if (showBottomSheet)
            MyBottomSheetScreen(navigationController = navigationController, markerVM = markerVM)
    }
}


