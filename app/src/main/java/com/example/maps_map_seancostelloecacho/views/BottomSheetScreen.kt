@file:Suppress("UNUSED_EXPRESSION")

package com.example.maps_map_seancostelloecacho.views

import android.content.Context
import android.net.Uri
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.maps_map_seancostelloecacho.R
import com.example.maps_map_seancostelloecacho.models.Location
import com.example.maps_map_seancostelloecacho.models.MarkerData
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel


@Composable
fun MyBottomSheetContent(navigationController: NavController, markerVM: MapViewModel) {
    val actualMarker by markerVM.actualMarker.observeAsState(null)
    var name by rememberSaveable {
        mutableStateOf("")
    }
    var description by rememberSaveable {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val expandedBottomSheet by markerVM.expandedBottomSheet.observeAsState(false)
    val uri by markerVM.uri.observeAsState(null)
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
    val listMarkerType by markerVM.listMarkerType.observeAsState(mutableListOf())
    val newListMarkersType = listMarkerType.drop(1).toMutableList()


    markerVM.changeActualScreen("BottomSheet")
    MyBottomSheetScreen(
        actualMarker = actualMarker,
        name = name,
        description = description,
        onNameChange = {name = it},
        onDescriptionChange = {description = it},
        onShowBottomSheetChange = { markerVM.changeShowBottomSheet(it) },
        typeMarker = typeMarker,
        changeTypeMarker = { markerVM.changeTypeMarker(it) },
        listTypeMarker = newListMarkersType,
        expandedBottomSheet = expandedBottomSheet,
        onExpandedBottomSheetChange = { markerVM.changeExpandedBottomSheet(it) },
        navigationController = navigationController,
        uri = uri,
        navigationItems = navigationItems,
        context = context,
        changeNewMarker = {markerVM.changeNewMarker(it)},
        whenAddMarker = { markerVM.whenAddMarker(it) }
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MyBottomSheetScreen(
    actualMarker : MarkerData?,
    name: String,
    description: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onShowBottomSheetChange: (Boolean) -> Unit,
    typeMarker: String,
    changeTypeMarker: (String) -> Unit,
    listTypeMarker: MutableList<String>,
    expandedBottomSheet: Boolean,
    onExpandedBottomSheetChange: (Boolean) -> Unit,
    navigationController: NavController,
    uri: Uri?,
    navigationItems: Map<String, String>,
    context: Context,
    changeNewMarker: (MarkerData) -> Unit,
    whenAddMarker: (Context) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            onShowBottomSheetChange(false)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
            ) {
                NameMarkerScreen(
                    name = name,
                    onNameChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(0.5f),
                )
                TypeMarkerScreen(
                    arrTypeMarkers = listTypeMarker,
                    typeMarker = typeMarker,
                    onTypeMarkerChange = { changeTypeMarker(it) },
                    expanded = expandedBottomSheet,
                    onExpandedChange = { onExpandedBottomSheetChange(it) },
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            DescriptionMarkerScreen(
                description = description,
                onDescriptionChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(0.9f),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            ImageItem(navigationController, uri, navigationItems)
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            WhenAddMarkerScreen(
                actualMarker = actualMarker,
                name = name,
                typeMarker = typeMarker,
                description = description,
                photo = uri,
                whenAddMarker = { whenAddMarker(it) },
                changeNewMarker = { changeNewMarker(it) },
                context = context
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageItem(navController: NavController, uri: Uri?, navigationItems: Map<String, String>) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth(0.25f)
            .fillMaxHeight(0.25f)
    ) {
        if (uri != null) {
            GlideImage(
                model = uri,
                contentDescription = "Image from the new marker",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { navController.navigate(navigationItems["cameraScreen"]!!) }
            )
        } else {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Defult images for marker",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(navigationItems["cameraScreen"]!!) }
            )
        }
    }
}

@Composable
fun WhenAddMarkerScreen(
    actualMarker: MarkerData?,
    name: String,
    typeMarker: String,
    description: String,
    photo: Uri?,
    whenAddMarker: (Context) -> Unit,
    changeNewMarker: (MarkerData) -> Unit,
    context: Context
) {
    Button(
        onClick = {
            val newMarker = MarkerData(
                id = if (actualMarker!!.id != null) actualMarker.id else null,
                name = name,
                type = typeMarker,
                description = description,
                photo = photo.toString(),
                location = Location(latitude = actualMarker.location.latitude, longitude = actualMarker.location.longitude)
            )
            changeNewMarker(newMarker)
            whenAddMarker(context)
        }
    ) {
        Text(text = "Add Marker")
    }
}


// todo (done): setting expanding sizes in sub-composables
@Composable
fun NameMarkerScreen(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    TextField(
        value = name,
        onValueChange = { onNameChange(name) },
        placeholder = { Text(text = "Name") },
        modifier = modifier
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeMarkerScreen(
    arrTypeMarkers: MutableList<String>,
    typeMarker: String,
    onTypeMarkerChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Box {
        OutlinedTextField(
            value = typeMarker,
            onValueChange = { onTypeMarkerChange(it) },
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
                    onExpandedChange(!expanded)
                }
                .fillMaxWidth(0.6f)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onExpandedChange(!expanded)
            },
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
        ) {
            arrTypeMarkers.forEach { typeMarker ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = typeMarker,
                            color = MaterialTheme.colorScheme.background
                        )
                    },
                    onClick = {
                        onExpandedChange(false)
                        onTypeMarkerChange(typeMarker)
                    }
                )
            }
        }
    }
}

@Composable
fun DescriptionMarkerScreen(
    description: String,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,

) {
    TextField(
        value = description,
        onValueChange = { onDescriptionChange(description) },
        placeholder = { Text(text = "Description (optional)") },
        modifier = modifier
    )
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
