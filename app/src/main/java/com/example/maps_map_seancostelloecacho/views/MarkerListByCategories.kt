import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.maps_map_seancostelloecacho.R
import com.example.maps_map_seancostelloecacho.models.MarkerData
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import com.example.maps_map_seancostelloecacho.views.MyBottomSheetContent

@Composable
fun MarkerListContent(
    navController: NavController,
    markerVM: MapViewModel
) {
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val getMarkersComplet by markerVM.markersComplet.observeAsState(false)
    val turnOnSecondProcess by markerVM.turnOnSeconProcess.observeAsState(false)
    val finishSort by markerVM.finishSort.observeAsState(false)
    val justDelete by markerVM.justDelete.observeAsState(false)
    val markerList by markerVM.markerList.observeAsState(emptyList())

    Log.i("ActualType", "ActualType in list: $typeMarker")
    LaunchedEffect(key1 = typeMarker, key2 = justDelete, key3 = markerList) {
        if (typeMarker.equals("All markers")) {
            markerVM.getMarkers()
        } else {
            markerVM.getFilterMarkers()
        }
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
            justDelete = justDelete
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
    justDelete: Boolean
) {
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val categoryMarkerList by markerVM.categoryMarkerList.observeAsState(emptyList())
    val actualMarker by markerVM.actualMarker.observeAsState(null)
    val showBottomSheet by markerVM.showBottomSheet.observeAsState(false)

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
                        val actualUri = element.photo.toUri()
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
                                    markerVM.getMarker(element.id!!)
                                    navController.navigate(Routes.MapGeolocalisationScreen.route)
                                },
                            markerVM = markerVM,
                            justDelete = justDelete,
                        )
                    }
                }
            }
            if (showBottomSheet)
                MyBottomSheetContent(navigationController = navController, markerVM = markerVM)
        }
    } else {
        Log.i("MARKER", "actual type: $typeMarker")

        ErrorMsg(
            msg = if (typeMarker.equals("All markers")) "For the moment, you don't have any marker." else "For the moment, for the type ${typeMarker} there isn't any marker.",
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun CategoryHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Log.i("image", "in category: $text")
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
}

@Composable
fun CategoryItem(
    actualMarker : MarkerData?,
    navController: NavController,
    id: String,
    uri: Uri,
    text: String,
    justDelete: Boolean,
    modifier: Modifier = Modifier,
    markerVM: MapViewModel
) {
    Log.i("idMarker", "idMarker: $id name: $text")
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ImageMarker(uri, text)
            NameMarker(text)
            EditButton(
                actualMarker = actualMarker,
                navController = navController,
                markerVM = markerVM,
                id = id,
            )
            DeleteButtom(markerVM, justDelete, id)
        }

    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun ImageMarker(uri: Uri, text: String) {
    GlideImage(
        model = uri,
        contentDescription = "$text image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth(0.2F)
            .fillMaxHeight()
    )
}

@Composable
private fun NameMarker(text: String) {
    Text(
        text = text,
        //modifier = modifier,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.background
    )
}

@Composable
private fun EditButton(
    actualMarker: MarkerData?,
    navController: NavController,
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
            markerVM.changeIsEditing(true)
            Log.i("MarkerDataÑ", "MarkerData id: ${actualMarker?.id} name: ${actualMarker?.name}")
            markerVM.changeShowBottomSheet(true)
            onEdit = false
        }
    }
}

@Composable
private fun DeleteButtom(
    markerVM: MapViewModel,
    justDelete: Boolean,
    id: String
) {
    IconButton(
        onClick = {
            markerVM.deleteMarker(id)
            markerVM.changeJustDelete(!justDelete)
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
        Text(text = msg, color = MaterialTheme.colorScheme.primary)
    }
}