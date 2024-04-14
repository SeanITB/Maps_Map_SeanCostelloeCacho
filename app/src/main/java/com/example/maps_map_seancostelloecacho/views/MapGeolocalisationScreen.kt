package com.example.maps_map_seancostelloecacho.views

import android.Manifest
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
import androidx.navigation.NavHostController
import com.example.maps_map_seancostelloecacho.navigation.Routes

@Composable
fun MapGeolocalisationScreen(navController: NavHostController, markerVM: MapViewModel) {
    val context = LocalContext.current
    val isMapPermissionsGranted by markerVM.mapPermissionGranted.observeAsState(false)
    val shouldShowPermissionMapRationale by markerVM.shouldShowPermissionMapRationale.observeAsState(
        true
    )
    val showMapPermissionDenied by markerVM.showMapPermissionDenied.observeAsState(false)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            Log.i("booleano", "isGranded: $isGranted") //toDo: siempre me sale false
            if (isGranted)
                markerVM.setMapPermissionGranted(true)
            else {
                markerVM.setShouldShowMapPermissionRationale(
                    shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
                if (!shouldShowPermissionMapRationale) {
                    Log.i("MapGeolocalisationScreen", "We can't ask for mor permission")
                    markerVM.setShowMapPermissionDenied(true)
                }
            }
        }
    )
    if (!isMapPermissionsGranted) { //toDO: por eso no navega
        SideEffect {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    } else {
        markerVM.setShowMapPermissionDenied(false)
        navController.navigate(Routes.MapScreen.route)
    }

    if (showMapPermissionDenied)
        PermissionDeclinedMapGeolocalisationScreen()
}

@Composable
fun PermissionDeclinedMapGeolocalisationScreen() {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Permission required", fontWeight = FontWeight.Bold)
        Text(text = "This app needs access ti the geolocalisation")
        Button(onClick = {
            openAppSettingsMapGeolocalisationScreen(context as Activity)
        }) {
            Text(text = "Accept")
        }
    }
}

fun openAppSettingsMapGeolocalisationScreen(activity: Activity) {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_SETTINGS
        data = Uri.fromParts("package", activity.packageName, null)
    }
    activity.startActivity(intent)
}
