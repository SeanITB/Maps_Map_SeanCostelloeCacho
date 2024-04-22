package com.example.maps_map_seancostelloecacho.views

import android.content.Context
import android.util.Log
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.maps_map_seancostelloecacho.R
import com.example.maps_map_seancostelloecacho.models.Location
import com.example.maps_map_seancostelloecacho.models.MarkerData
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import com.google.android.gms.maps.model.LatLng


@Composable
fun MyBottomSheetFromMapContent(navigationController: NavController, markerVM: MapViewModel) {
    val actualMarker by markerVM.actualMarker.observeAsState(null)
    val name by markerVM.nameMarker.observeAsState("")
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val context = LocalContext.current
    val expandedBottomSheet by markerVM.expandedBottomSheet.observeAsState(false)
    val uriUrl by markerVM.uriUrl.observeAsState("")
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
    val listMarkerType by markerVM.listMarkerType.observeAsState(mutableListOf())
    val newListMarkersType = listMarkerType.drop(1).toMutableList()
    val lastPosition by markerVM.actualPosition.observeAsState()
    val description by markerVM.description.observeAsState("")
    var actualPosition: LatLng by rememberSaveable {
        mutableStateOf(LatLng(0.0, 0.0))
    }
    var isFirstTime by rememberSaveable {
        mutableStateOf(true)
    }
    if (isFirstTime) {
        actualPosition = lastPosition!!
        isFirstTime = false
    }

    MyBottomSheetScreen(
        description = description,
        onDescriptionChange = {markerVM.changeDescription(it)},
        onFirstTimeChange = {isFirstTime = it},
        actualMarker = actualMarker,
        name = name,
        onNameChange = { markerVM.changeNameMarke(it) },
        onShowBottomSheetChange = { markerVM.changeShowBottomFromMapSheet(it) },
        typeMarker = typeMarker,
        changeTypeMarker = { markerVM.changeTypeMarker(it) },
        listTypeMarker = newListMarkersType,
        expandedBottomSheet = expandedBottomSheet,
        onExpandedBottomSheetChange = { markerVM.changeExpandedBottomSheet(it) },
        navigationController = navigationController,
        uriUrl = uriUrl,
        navigationItems = navigationItems,
        context = context,
        changeNewMarker = {markerVM.changeNewMarker(it)},
        whenAddMarker = { markerVM.whenAddMarkerFromMap(it) },
        actualPosition = actualPosition,
        fromWhere = "cameraFromMapScreen"
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MyBottomSheetScreen(
    description: String,
    onDescriptionChange: (String) -> Unit,
    onFirstTimeChange: (Boolean) -> Unit,
    actualMarker : MarkerData?,
    name: String,
    onNameChange: (String) -> Unit,
    onShowBottomSheetChange: (Boolean) -> Unit,
    typeMarker: String,
    changeTypeMarker: (String) -> Unit,
    listTypeMarker: MutableList<String>,
    expandedBottomSheet: Boolean,
    onExpandedBottomSheetChange: (Boolean) -> Unit,
    navigationController: NavController,
    uriUrl: String,
    navigationItems: Map<String, String>,
    context: Context,
    changeNewMarker: (MarkerData) -> Unit,
    whenAddMarker: (Context) -> Unit,
    actualPosition: LatLng?,
    fromWhere: String
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
                    onNameChange = { onNameChange(it) },
                    modifier = Modifier.fillMaxWidth(0.5f),
                )
                TypeMarkerScreen(
                    arrTypeMarkers = listTypeMarker,
                    typeMarker = typeMarker,
                    onTypeMarkerChange = { changeTypeMarker(it) },
                    expanded = expandedBottomSheet,
                    onExpandedChange = { onExpandedBottomSheetChange(it) },
                    modifier = Modifier.clickable {
                        onExpandedBottomSheetChange(!expandedBottomSheet)
                    }
                        .fillMaxWidth(0.6f)
                )

            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            DescriptionOfMarker(
                description = description,
                onDescriptionChange = {onDescriptionChange(it)},
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            ImageItem(navigationController, uriUrl, navigationItems, fromWhere)
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            WhenAddMarkerScreen(
                onFirstTimeChange = { onFirstTimeChange(it) },
                actualMarker = actualMarker,
                name = name,
                typeMarker = typeMarker,
                uriUrl = uriUrl,
                whenAddMarker = { whenAddMarker(it) },
                changeNewMarker = { changeNewMarker(it) },
                context = context,
                actualPosition = actualPosition,
                description = description
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        }
    }
}

@Composable
fun DescriptionOfMarker(
    description: String,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = description,
        onValueChange = { onDescriptionChange(it) },
        placeholder = { Text(text = "Description (optional)") },
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.primary
        ),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageItem(navController: NavController, uriUrl: String, navigationItems: Map<String, String>, fromWhere: String) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth(0.25f)
            .fillMaxHeight(0.25f)
    ) {
        if (uriUrl != "") {
            GlideImage(
                model = uriUrl.toUri(),
                contentDescription = "Image from the new marker",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { navController.navigate(navigationItems[fromWhere]!!) }
            )
        } else {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Defult images for marker",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(navigationItems[fromWhere]!!) }
            )
        }
    }
}

@Composable
fun WhenAddMarkerScreen(
    onFirstTimeChange: (Boolean) -> Unit,
    description: String?,
    actualMarker: MarkerData?,
    name: String,
    typeMarker: String,
    uriUrl: String,
    whenAddMarker: (Context) -> Unit,
    changeNewMarker: (MarkerData) -> Unit,
    context: Context,
    actualPosition: LatLng?
) {
    Button(
        onClick = {
            val newMarker = MarkerData(
                id = actualMarker?.id,
                name = name,
                type = typeMarker,
                uriUrl = uriUrl,
                location = Location(latitude = actualPosition!!.latitude, longitude = actualPosition!!.longitude),
                description = description
            )
            Log.i("nooo", "nooo editant amb valors image $uriUrl , name $name id: ${actualMarker?.id}, type $typeMarker, location: ${Location(latitude = actualPosition.latitude, longitude = actualPosition.longitude)}")
            changeNewMarker(newMarker)
            whenAddMarker(context)
            onFirstTimeChange(true)
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
        onValueChange = { onNameChange(it) },
        placeholder = { Text(text = "Name") },
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.primary
        ),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
    )

}


@Composable
fun TypeMarkerScreen(
    arrTypeMarkers: MutableList<String>,
    typeMarker: String,
    onTypeMarkerChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        TextField(
            value = typeMarker,
            onValueChange = { onTypeMarkerChange(it) },
            label = { Text(text = "Marker Type", color = MaterialTheme.colorScheme.primary) },
            enabled = false,
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
            modifier = modifier

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




/* // todo: no me funciona el preview
@PreviewParameter
@Composable
fun BottomSheetScreenPreview(markerVM: MarkerViewModel, navigationController: NavController){

    Maps_Map_SeanCostelloeCachoTheme {
        MyBottomSheet(navigationController, markerVM)
    }
}

 */
