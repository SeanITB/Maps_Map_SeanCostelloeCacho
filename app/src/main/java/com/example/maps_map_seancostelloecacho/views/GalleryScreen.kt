package com.example.maps_map_seancostelloecacho.views

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import com.example.maps_map_seancostelloecacho.R
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel


@Composable
fun GalleryScreen(navController: NavHostController, markerVM: MapViewModel) {
    val context = LocalContext.current
    val img: Bitmap? =
        ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground)?.toBitmap()
    var bitmap by remember {
        mutableStateOf(img)
    }
    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            try {
                bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = it?.let { it1 ->
                        ImageDecoder.createSource(context.contentResolver, it1)
                    }
                    source?.let { it1 ->
                        ImageDecoder.decodeBitmap(it1)
                    }!!
                }
                markerVM.changeUri(it)
            } catch (e: NullPointerException) {
                println("Gallery close")
            }
        }
    )
    var galleryOpened by remember {
        mutableStateOf(false)
    }
    val navigationItems by markerVM.navigationItems.observeAsState(emptyMap())
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = "Image from gallery",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(250.dp)
                .background(MaterialTheme.colorScheme.background)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
        )
        Button(
            onClick = {

                if (!galleryOpened) {
                    launchImage.launch("image/*")
                    galleryOpened = true
                } else {
                    navController.navigate(navigationItems["mapGeolocalisationScreen"]!!)
                }
            }
        ) {
            Text(text = if (!galleryOpened) "Open Gallery" else "add photo")
        }
    }
}




