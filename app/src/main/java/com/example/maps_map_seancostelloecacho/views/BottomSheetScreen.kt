@file:Suppress("UNUSED_EXPRESSION")

package com.example.maps_map_seancostelloecacho.views

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomSheetScreen(navigationController: NavController, markerVM: MarkerViewModel) {
    markerVM.changeActualScreen("BottomSheet")
    ModalBottomSheet(
        onDismissRequest = {
            markerVM.changeShowBottomSheet(false)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                //horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NameMarkerScreen(markerVM = markerVM)
                TypeMarkerScreen(markerVM = markerVM)
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            DescriptionMarkerScreen(markerVM = markerVM)
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Image(markerVM)
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            NavigateToCameraScreen(navigationController = navigationController, markerVM = markerVM)
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            WhenAddMarkerScreen(markerVM = markerVM)
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        }
    }
}

@Composable
private fun WhenAddMarkerScreen(
    markerVM: MarkerViewModel,
) {
    val context = LocalContext.current
    WhenAddMarkerContent(
        whenAddMarker = {markerVM.whenAddMarker(it) },
        context = context
    )
}


@Composable
private fun NameMarkerScreen(markerVM: MarkerViewModel) {
    val name by markerVM.nameMarker.observeAsState("")
    NameMarkerContent(modifier = Modifier.fillMaxWidth(0.5f), name = name, onNameChange = {markerVM.changeNameMarker(it)})
}



@Composable
private fun TypeMarkerScreen(markerVM: MarkerViewModel) {
    val actualScreen by markerVM.actualScreen.observeAsState("")
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val expandedTopBar by markerVM.expandedTopBar.observeAsState(false)
    val expandedBottomSheet by markerVM.expandedBottomSheet.observeAsState(false)
    TypeMarkerContent(
        actualScreen = actualScreen,
        typeMarker = typeMarker,
        onTypeMarkerChange = {markerVM.changeTypeMarker(it)},
        expandedTopBar = expandedTopBar,
        onExpandedTopBarChange = {markerVM.changeExpandedTopBar(it)},
        expandedBottomSheet = expandedBottomSheet,
        onExpandedBottomSheetChange = {markerVM.changeExpandedBottomSheet(it)},
        whenMarkerTypedChanged = {markerVM.whenMarkerTypedChanged() }
        )
}

@Composable
private fun DescriptionMarkerScreen(markerVM: MarkerViewModel) {
    val descriptionMarker by markerVM.descriptionMarker.observeAsState("")
    DescriptionMarkerContent(
        modifier = Modifier.fillMaxWidth(0.9f),
        descriptionMarker = descriptionMarker,
        onDescriptionMarkerChange = { markerVM.changeDescription(it) }
    )
}

@Composable
private fun NavigateToCameraScreen(navigationController: NavController, markerVM: MarkerViewModel) {
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
    NavigateToPhotoContent(navigationController, navigationItems)
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Image(markerVM: MarkerViewModel) {
    val photoMarker by markerVM.photoMarker.observeAsState("")
    Log.i("IMAGE", "image in add marker $photoMarker")
    GlideImage(
        model = photoMarker.toUri(),
        contentDescription = "Image from the new marker",
    )
}

@Composable
fun PhotoItem(photo: Bitmap) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight(0.25f)
    ) {
        Image(bitmap = photo.asImageBitmap(), contentDescription = "Image from the new marker")
    }
}


@Composable
fun PhotoDefultItem(photo: Int) {
    //LocalInspectionMode.current // bolean q compruba q estes en preview
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight(0.25f)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = photo),
            contentDescription = "Defult images for marker"
        )
    }
}


/* // todo: no me funciona el preview
@PreviewParameter
@Composable
fun BottomSheetScreenPreview(markerVM: MarkerViewModel, navigationController: NavController){

    Maps_Map_SeanCostelloeCachoTheme {
        MyBottomSheet(navigationController, markerVM)
    }
}

 */
