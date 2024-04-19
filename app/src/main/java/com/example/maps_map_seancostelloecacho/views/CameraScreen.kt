package com.example.maps_map_seancostelloecacho.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.navigation.Routes

@Composable
fun CameraFromMapContent(markerVM: MapViewModel, navController: NavController) {
    CameraScreen(markerVM = markerVM, navController = navController, whereToNavigate = "takePhotoFromMapScreen")
}

@Composable
fun CameraFromMarkerListContent(markerVM: MapViewModel, navController: NavController) {
    CameraScreen(markerVM = markerVM, navController = navController, whereToNavigate = "takePhotoFromMarkerListScreen")
}

@SuppressLint("MissingPermission")
@Composable
fun CameraScreen(markerVM: MapViewModel, navController: NavController, whereToNavigate: String) {
    val context = LocalContext.current
    val isCameraPermissionsGranted by markerVM.camaeraPermissionGranted.observeAsState(false)
    val shouldShowPermissionRationale by markerVM.shouldShowCameraPermissionRationale.observeAsState(false)
    val showPermissionDenied by markerVM.showCameraPermissionDenied.observeAsState(false)
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
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


    if (!isCameraPermissionsGranted) {
        SideEffect {
            launcher.launch(Manifest.permission.CAMERA)
        }
    } else {
        navController.navigate(navigationItems[whereToNavigate]!!)
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
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", activity.packageName, null)
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    activity.startActivity(intent)

}
