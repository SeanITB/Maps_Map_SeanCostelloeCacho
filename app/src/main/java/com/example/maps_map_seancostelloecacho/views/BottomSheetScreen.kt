package com.example.maps_map_seancostelloecacho.views

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.maps_map_seancostelloecacho.models.FieldToAddMarker
import com.example.maps_map_seancostelloecacho.models.Location
import com.example.maps_map_seancostelloecacho.models.MarkerData
import com.example.maps_map_seancostelloecacho.models.MethodsForAddingMarker
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import com.google.android.gms.maps.model.LatLng


@Composable
fun MyBottomSheetFromMapContent(navigationController: NavController, markerVM: MapViewModel) {
    val actualMarker by markerVM.actualMarker.observeAsState(null)
    val name by markerVM.nameMarker.observeAsState("")
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val context = LocalContext.current
    val expandedBottomSheet by markerVM.expandedBottomSheet.observeAsState(false)
    val uri by markerVM.uri.observeAsState(null)
    val uriUrl by markerVM.uriUrl.observeAsState("")
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
    val listMarkerType by markerVM.listMarkerType.observeAsState(mutableListOf())
    val newListMarkersType = listMarkerType.drop(1).toMutableList()
    val lastPosition by markerVM.actualPosition.observeAsState()
    val description by markerVM.description.observeAsState("")
    val fromWhere by markerVM.fromWhere.observeAsState("")
    val isUpload by markerVM.isUpload.observeAsState(false)
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
    Log.i("map","map entor en bottom sheet")
    val methodsAddMarker = MethodsForAddingMarker(
        changeShowBottomFromMapSheet = { markerVM.changeShowBottomFromMapSheet(false) },
        addMarker = { markerVM.addMarker() },
        restartMarkerAtributes = { markerVM.restartMarkerAtributes() }
    )
    val fieldToAddMarkr = FieldToAddMarker(
        context = context,
        name = name,
        type = typeMarker,
        photo = uri
    )
    markerVM.changeFromWhere("cameraFromMapScreen")
    MyBottomSheetScreen(
        description = description,
        onDescriptionChange = { markerVM.changeDescription(it) },
        onFirstTimeChange = { isFirstTime = it },
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
        uri = uri,
        navigationItems = navigationItems,
        context = context,
        changeNewMarker = { markerVM.changeNewMarker(it) },
        whenAddMarker = { markerVM.whenAddMarkerFromMap(it) },
        actualPosition = actualPosition,
        fromWhere = fromWhere,
        methodsForAddingMarker = methodsAddMarker,
        isUpload = isUpload,
        uriUrl = uriUrl,
        fieldToAddMarker = fieldToAddMarkr,
        onIsPhotoEditedChange = {false},
        isUploadChange = {markerVM.changeIsUriUrlUpload(it)}
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MyBottomSheetScreen(
    description: String,
    onDescriptionChange: (String) -> Unit,
    onFirstTimeChange: (Boolean) -> Unit,
    actualMarker: MarkerData?,
    name: String,
    onNameChange: (String) -> Unit,
    onShowBottomSheetChange: (Boolean) -> Unit,
    typeMarker: String,
    changeTypeMarker: (String) -> Unit,
    listTypeMarker: MutableList<String>,
    expandedBottomSheet: Boolean,
    onExpandedBottomSheetChange: (Boolean) -> Unit,
    navigationController: NavController,
    uri: Uri?,
    uriUrl: String,
    navigationItems: Map<String, String>,
    context: Context,
    changeNewMarker: (MarkerData) -> Unit,
    whenAddMarker: (FieldToAddMarker) -> Unit,
    actualPosition: LatLng?,
    fromWhere: String,
    methodsForAddingMarker: MethodsForAddingMarker,
    isUpload: Boolean,
    isUploadChange: (Boolean) -> Unit,
    fieldToAddMarker: FieldToAddMarker,
    onIsPhotoEditedChange: (Boolean) -> Unit,
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
            NameMarkerScreen(
                name = name,
                onNameChange = { onNameChange(it) },
                modifier = Modifier.fillMaxWidth(0.9f),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            TypeMarkerScreen(
                arrTypeMarkers = listTypeMarker,
                typeMarker = typeMarker,
                onTypeMarkerChange = { changeTypeMarker(it) },
                expanded = expandedBottomSheet,
                onExpandedChange = { onExpandedBottomSheetChange(it) },
                modifier = Modifier
                    .clickable {
                        onExpandedBottomSheetChange(!expandedBottomSheet)
                    }
                    .fillMaxWidth(0.9f)
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            DescriptionOfMarker(
                description = description,
                onDescriptionChange = { onDescriptionChange(it) },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            ImageItem(navigationController, uri, navigationItems, fromWhere, onIsPhotoEditedChange)
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            WhenAddMarkerScreen(
                onFirstTimeChange = { onFirstTimeChange(it) },
                actualMarker = actualMarker,
                uriUrl = uriUrl,
                whenAddMarker = { whenAddMarker(it) },
                changeNewMarker = { changeNewMarker(it) },
                actualPosition = actualPosition,
                description = description,
                methodsForAddingMarker = methodsForAddingMarker,
                isUpload = isUpload,
                fieldToAddMarker = fieldToAddMarker,
                isUploadChange = {isUploadChange(it)}
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
fun ImageItem(
    navController: NavController,
    uri: Uri?,
    navigationItems: Map<String, String>,
    fromWhere: String,
    onIsPhotoEditedChange: (Boolean) -> Unit
) {
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
                    .clickable { navController.navigate(navigationItems[fromWhere]!!) }
            )
            if (fromWhere.equals("cameraFromMarkerListScreen")) {
                onIsPhotoEditedChange(true)
            }
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
    uriUrl: String,
    whenAddMarker: (FieldToAddMarker) -> Unit,
    changeNewMarker: (MarkerData) -> Unit,
    actualPosition: LatLng?,
    isUpload: Boolean,
    methodsForAddingMarker: MethodsForAddingMarker,
    fieldToAddMarker: FieldToAddMarker,
    isUploadChange: (Boolean) -> Unit
    ) {
    Button(
        onClick = {
            try {
                 whenAddMarker(fieldToAddMarker)
            } catch (e: NullPointerException) {
                println("Any image upload")
            }
        }
    ) {
        Text(text = "Add Marker")
    }
    if (isUpload) {
        val newMarker = MarkerData(
            id = actualMarker?.id,
            name = fieldToAddMarker.name,
            type = fieldToAddMarker.type,
            uriUrl = uriUrl,
            location = Location(
                latitude = actualPosition!!.latitude,
                longitude = actualPosition.longitude
            ),
            description = description
        )
        changeNewMarker(newMarker)
        methodsForAddingMarker.addMarker()
        methodsForAddingMarker.restartMarkerAtributes()
        methodsForAddingMarker.changeShowBottomFromMapSheet(false)
        onFirstTimeChange(true)
        isUploadChange(false)
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
            modifier = modifier
        ) {
            arrTypeMarkers.forEach { typeMarker ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = typeMarker,
                            color = MaterialTheme.colorScheme.primary
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


