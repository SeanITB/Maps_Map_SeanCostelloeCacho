import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.maps_map_seancostelloecacho.models.MarkerData
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import com.example.maps_map_seancostelloecacho.views.MyBottomSheetScreen
import com.google.android.gms.maps.model.LatLng

@Composable
fun MarkerListContent(
    navController: NavController,
    markerVM: MapViewModel
) {
    val typeMarkerForFilter by markerVM.typeMarkerForFilter.observeAsState("")
    val getMarkersComplet by markerVM.markersComplet.observeAsState(false)
    val turnOnSecondProcess by markerVM.turnOnSeconProcess.observeAsState(false)
    val finishSort by markerVM.finishSort.observeAsState(false)

    if (typeMarkerForFilter == "All markers") {
        markerVM.getMarkers()
    } else {
        markerVM.getFilterMarkers(typeMarkerForFilter)
    }

    LaunchedEffect(key1 = turnOnSecondProcess) {
        markerVM.createMapOfMarkers()
        markerVM.sortMarkerList()
        markerVM.changeFinishSort(true)
    }

    if (getMarkersComplet && finishSort) {
        MarkerListScreen(
            markerVM = markerVM,
            navController = navController,
            typeMarker = typeMarkerForFilter
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                "Waiting for a response...",
            )
        }
    }

}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun MarkerListScreen(
    navController: NavController,
    markerVM: MapViewModel,
    typeMarker: String,
) {
    val categoryMarkerList by markerVM.categoryMarkerList.observeAsState(emptyList())
    val actualMarker by markerVM.actualMarker.observeAsState(null)
    val showBottomFromListSheet by markerVM.showBottomFromListSheet.observeAsState(false)

    if (categoryMarkerList.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            LazyColumn() {
                for (m in categoryMarkerList) {
                    stickyHeader {
                        CategoryHeader(
                            text = m.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(16.dp)
                        )
                    }
                    items(m.items) { element ->
                        val actualUri = element.uriUrl.toUri()
                        CategoryItem(
                            actualMarker = actualMarker,
                            navController = navController,
                            id = element.id!!,
                            uri = actualUri,
                            text = element.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(MaterialTheme.colorScheme.secondary)
                                .padding(16.dp)
                                .clickable {
                                    markerVM.changeActualPosition(
                                        LatLng(
                                            element.location.latitude,
                                            element.location.longitude
                                        )
                                    )
                                    navController.navigate(Routes.MapScreen.route)

                                },
                            markerVM = markerVM,
                        )
                    }
                }
            }
            if (showBottomFromListSheet)
                MyBottomSheetFromListContent(
                    navigationController = navController,
                    markerVM = markerVM
                )
        }
    } else {
        ErrorMsg(
            msg = if (typeMarker.equals("All markers")) "For the moment, you don't have any marker." else "For the type ${typeMarker}, by the moment, there isn't any marker.",
            modifier = Modifier.fillMaxSize()
        )
    }


}


@Composable
fun CategoryHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
}

@Composable
fun CategoryItem(
    actualMarker: MarkerData?,
    navController: NavController,
    id: String,
    uri: Uri,
    text: String,
    modifier: Modifier = Modifier,
    markerVM: MapViewModel
) {
    var showConfimDelete by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageMarker(uri, text, modifier = Modifier)
            NameMarker(text, modifier = Modifier.fillMaxSize(0.5F))

            EditButton(
                actualMarker = actualMarker,
                markerVM = markerVM,
                id = id,
            )
            DeleteButtom { showConfimDelete = it }

            MyAlertDialog(
                showConfimDelete,
                { showConfimDelete = false },
                { showConfimDelete = false },
                id,
                { markerVM.deleteMarker(it) })
        }

    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun ImageMarker(uri: Uri, text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        GlideImage(
            model = uri,
            contentDescription = "$text image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.2F)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun NameMarker(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = text,
            //modifier = modifier,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.background,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EditButton(
    actualMarker: MarkerData?,
    markerVM: MapViewModel,
    id: String,
) {
    var onEdit by rememberSaveable {
        mutableStateOf(false)
    }
    IconButton(
        onClick = {
            onEdit = !onEdit
        },
        colors = IconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "Edit Marker",
        )
    }
    if (onEdit) {
        markerVM.getMarker(id)
        LaunchedEffect(key1 = actualMarker) {
            markerVM.changeShowBottomSheetFromList(true)
            onEdit = false
        }
    }
}

@Composable
private fun DeleteButtom(
    onShowConfirmDelete: (Boolean) -> Unit
) {
    IconButton(
        onClick = {
            onShowConfirmDelete(true)
        },
        colors = IconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Icon(
            imageVector = Icons.Filled.DeleteSweep,
            contentDescription = "Edit Marker",
        )
    }
}

@Composable
fun ErrorMsg(msg: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = msg,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(0.8F)
        )
    }
}

@Composable
fun MyBottomSheetFromListContent(navigationController: NavController, markerVM: MapViewModel) {
    val actualMarker by markerVM.actualMarker.observeAsState(null)
    val name by markerVM.nameMarker.observeAsState("")
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val description by markerVM.description.observeAsState("")
    val context = LocalContext.current
    val expandedBottomSheet by markerVM.expandedBottomSheet.observeAsState(false)
    val uriUrl by markerVM.uriUrl.observeAsState("")
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
    val listMarkerType by markerVM.listMarkerType.observeAsState(mutableListOf())
    val newListMarkersType = listMarkerType.drop(1).toMutableList()
    var isFirstTime by rememberSaveable {
        mutableStateOf(true)
    }
    val actualPosition by markerVM.actualPosition.observeAsState()
    if (isFirstTime && actualMarker != null) {
        markerVM.changeNameMarke(actualMarker!!.name)
        markerVM.changeTypeMarker(actualMarker!!.type)
        markerVM.changeUriUrl(actualMarker!!.uriUrl)
        isFirstTime = false
    }
    Log.i(
        "MarkerDataÑ",
        "in bottom Sheet id: ${actualMarker?.id}, name: ${actualMarker?.name} photo: $uriUrl"
    )


    markerVM.changeActualScreen("BottomSheet")
    MyBottomSheetScreen(
        actualMarker = actualMarker,
        name = name,
        onNameChange = { markerVM.changeNameMarke(it) },
        onShowBottomSheetChange = { markerVM.changeShowBottomSheetFromList(it) },
        typeMarker = typeMarker,
        changeTypeMarker = { markerVM.changeTypeMarker(it) },
        listTypeMarker = newListMarkersType,
        expandedBottomSheet = expandedBottomSheet,
        onExpandedBottomSheetChange = { markerVM.changeExpandedBottomSheet(it) },
        navigationController = navigationController,
        uriUrl = uriUrl,
        navigationItems = navigationItems,
        context = context,
        changeNewMarker = { markerVM.changeNewMarker(it) },
        whenAddMarker = { markerVM.whenEditMarkerFromList(it) },
        actualPosition = actualPosition,
        onFirstTimeChange = { isFirstTime = it },
        description = description,
        onDescriptionChange = {markerVM.changeDescription(it)}
    )
}

@Composable
fun MyAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    id: String,
    deleteMarker: (String) -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = {},
            text = {
                Text(
                    "Are you sure that you want to delete.",
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        deleteMarker(id)
                    }
                ) { Text(text = "OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                ) { Text(text = "Cancel") }
            }
        )
    }
}
