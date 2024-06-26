package com.example.maps_map_seancostelloecacho.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel

@Composable
fun TakePhotoFromMapScreen(markerVM: MapViewModel, navController: NavController) {
    TakePhotoView(navController = navController, whereToNavigate = "mapScreen", markerVM = markerVM)
}

@Composable
fun TakePhotoMarkerListScreen(markerVM: MapViewModel, navController: NavController) {
    TakePhotoView(navController = navController, whereToNavigate = "markerListScreen", markerVM = markerVM)
}

@Composable
private fun TakePhotoView(
    navController: NavController,
    whereToNavigate: String,
    markerVM: MapViewModel
) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            CameraController.IMAGE_CAPTURE
        }
    }
    val navigationItems by markerVM.navigationItems.observeAsState(emptyMap())
    //val isUriUpload by markerVM.isUriUpload.observeAsState(false)
    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(controller, Modifier.fillMaxSize())
        IconButton(
            onClick = {
                controller.cameraSelector =
                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
            },
            modifier = Modifier.offset(16.dp, 16.dp)
        ) {
            Icon(imageVector = Icons.Default.Cameraswitch, contentDescription = "Switch camera")
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            IconButton(
                onClick = {
                    navController.navigate(Routes.GalleryScreen.route)
                }
            ) {
                Icon(imageVector = Icons.Default.Photo, contentDescription = "Open gallery")

            }
            IconButton(
                onClick = {
                    takePhoto(context, controller) { photo ->
                        val uri = markerVM.bitmapToUri(context, photo)
                        markerVM.changeUri(uri!!)
                        navController.navigate(navigationItems[whereToNavigate]!!)
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = "Take photo")
            }
        }
    }
}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                onPhotoTaken(image.toBitmap())
                val matri = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matri,
                    true
                )
                onPhotoTaken(rotatedBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("camera", "Error taken photo", exception)
            }
        }
    )
}

@Composable
fun CameraPreview(controller: LifecycleCameraController, modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}
