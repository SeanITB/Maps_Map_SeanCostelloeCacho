package com.example.maps_map_seancostelloecacho.views

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.R
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.ui.theme.Maps_Map_SeanCostelloeCachoTheme
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomSheet(navigationController: NavController, markerVM: MarkerViewModel) {
    //val spaceBetween = 50
    markerVM.changeActualScreen("BottomSheet")
    val context = LocalContext.current
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
                TextField(
                    value = markerVM.nameMarker.value!!,
                    onValueChange = { markerVM.changeNameMarker(it) },
                    placeholder = { Text(text = "Name") },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                TypeOptions(markerVM = markerVM)
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            TextField(
                value = markerVM.descriptionMarker.value!!,
                onValueChange = { markerVM.changeDescription(it) },
                placeholder = { Text(text = "Description (optional)") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            RowOfImages(markerVM)
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Button(
                onClick = {
                    navigationController.navigate(Routes.CameraScreen.route)
                }
            ) {
                Text(text = "photo")
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Button(
                onClick = {
                    markerVM.addMarker()
                    if (markerVM.proveThatMarkerIsCorrect()) {
                        markerVM.changeShowBottomSheet(false)
                        markerVM.restartMarkerAtributes()
                        markerVM.addMarker()
                    } else
                        Toast.makeText(context, "There are unfinished fields.", Toast.LENGTH_LONG)
                            .show()
                }
            ) {
                Text(text = "Add Marker")
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeOptions(markerVM: MarkerViewModel) {
    val genders =
        if (markerVM.actualScreen != "BottomSheet") {
            arrayOf(
                "All markers",
                "Park",
                "Bookstore",
                "Sports Center",
                "Museum",
                "Restaurant"
            )
        } else {
            arrayOf(
                "Park",
                "Bookstore",
                "Sports Center",
                "Museum",
                "Restaurant"
            )
        }
    Box(
        //modifier = Modifier.fillMaxWidth(0.35f)
    ) {
        OutlinedTextField(
            value = markerVM.typeMarker.value!!,
            onValueChange = { markerVM.changeTypeMarker(it) },
            label = { Text(text = "Marker Type", color = MaterialTheme.colorScheme.primary) },
            enabled = false,
            readOnly = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .clickable {
                    if (markerVM.actualScreen != "BottomSheet") { //toDo: para quando habrel el bottom sheet no sea habra el top bar, pero no funciona
                        markerVM.changeExpandedOptionsTopBar(true)
                    } else {
                        markerVM.changeExpandedOptions(true)
                    }
                }
                .fillMaxWidth(0.6f)
        )
        DropdownMenu(
            expanded =
            if (markerVM.actualScreen != "BottomSheet") {
                markerVM.expandedOptionsTopBar
            } else {
                markerVM.expandedOptions
            },
            onDismissRequest = {
                if (markerVM.actualScreen != "BottomSheet") {
                    markerVM.changeExpandedOptionsTopBar(true)
                } else {
                    markerVM.changeExpandedOptions(true)
                }
            },
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = gender,
                            color = MaterialTheme.colorScheme.background
                        )
                    },
                    onClick = {
                        markerVM.changeTypeMarker(gender)
                        if (markerVM.actualScreen != "BottomSheet") {
                            markerVM.changeIsFiltred(true)
                            markerVM.createFilerList()
                            if (markerVM.typeMarker.equals("All markers"))
                                markerVM.changeIsFiltred(false)
                            markerVM.changeExpandedOptionsTopBar(false)
                        } else if (markerVM.actualScreen == "mapScreen") {
                            markerVM.createFilerList()
                        } else {
                            markerVM.changeExpandedOptions(false)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RowOfImages(markerVM: MarkerViewModel) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (markerVM.photosMarker.value!!.isNotEmpty()) {
            items(markerVM.photosMarker.value!!) { photo ->
                PhotoItem(photo)
            }
        } else {
            items(3) {
                PhotoDefultItem(R.drawable.ic_launcher_foreground)
            }
        }
    }
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

class BottomSheetParameterProviader: PreviewParameterProvider<Int> {
    override val values: Sequence<Int>
        get() = sequenceOf(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background)
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
private fun BottomSheetPreview(
    @PreviewParameter(BottomSheetParameterProviader::class) photo: Int
) {
    Maps_Map_SeanCostelloeCachoTheme {
        PhotoDefultItem(photo = photo)
    }
}
