package com.example.maps_map_seancostelloecacho.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.MainActivity
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

@SuppressLint("MissingPermission")
@Composable
fun CameraScreen(markerVM: MapViewModel, navController: NavController) {
    val context = LocalContext.current
    val isCameraPermissionsGranted by markerVM.camaeraPermissionGranted.observeAsState(false)
    val shouldShowPermissionRationale by markerVM.shouldShowCameraPermissionRationale.observeAsState(false)
    val showPermissionDenied by markerVM.showCameraPermissionDenied.observeAsState(false)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted ->
            if (isGranted) {
                markerVM.setCamaeraPermissionGranted(true)
            } else {
                markerVM.setShouldShowCameraPermissionRationale(
                    shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.CAMERA
                    )
                )
                if (!shouldShowPermissionRationale) {
                    Log.i("CameraScreen", "We can't ask for mor permission")
                    markerVM.setShowCameraPermissionDenied(true)
                }
            }
        }
    )
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
    val locationResult = fusedLocationProviderClient.getCurrentLocation(100, null)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                if (!isCameraPermissionsGranted) {
                    launcher.launch(Manifest.permission.CAMERA)
                } else {
                    locationResult.addOnCompleteListener(context as MainActivity) { task ->
                        if (task.isSuccessful) {
                            lastKnowLocation = task.result
                            deviceLatLng = LatLng(lastKnowLocation!!.latitude, lastKnowLocation!!.longitude)

                        } else {
                            Log.e("ERROR", "Exception: %s", task.exception)
                        }
                    }
                    navController.navigate(Routes.TakePhotoScreen.route)
                }
            }) {
            Text(text = "Take photo")
        }
    }
    if (showPermissionDenied)
        PermissionDeclinedScreen()
}

@Composable
fun PermissionDeclinedScreen() {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Permission required", fontWeight = FontWeight.Bold)
        Text(text = "This app needs access ti the camera to take photos")
        Button(onClick = {
            openAppSettings(context as Activity)
        }) {
            Text(text = "Accept")
        }
    }
}

fun openAppSettings(activity: Activity) {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_SETTINGS
        data = Uri.fromParts("package", activity.packageName, null)
    }
    activity.startActivity(intent)
}
