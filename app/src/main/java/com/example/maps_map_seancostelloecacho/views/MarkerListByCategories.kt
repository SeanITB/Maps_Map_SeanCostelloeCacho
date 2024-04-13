import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.maps_map_seancostelloecacho.navigation.NavigationItems
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel
import kotlinx.coroutines.launch

@Composable
fun MarkerListContent(
    navController: NavController,
    markerVM: MarkerViewModel
) {
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val getMarkersComplet by markerVM.markersComplet.observeAsState(false)

    markerVM.changeActualScreen("MarkerListByCategories")

    LaunchedEffect(key1 = typeMarker) {
        if (typeMarker.equals("All markers")) {
            markerVM.getMarkers()
        } else {
            markerVM.getFilterMarkers()
        }
    }

    LaunchedEffect(key1 = getMarkersComplet) {
        markerVM.createMapOfMarkers()
        markerVM.sortMarkerList()
        markerVM.setMarkerComplete(false)
    }

    MarkerListScreen(markerVM = markerVM, navController = navController)

}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun MarkerListScreen(
    navController: NavController,
    markerVM: MarkerViewModel
) {
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val categoryMarkerList by markerVM.categoryMarkerList.observeAsState(emptyList())
    if (categoryMarkerList.isNotEmpty()) {
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
                    CategoryItem(
                        id = element.id!!,
                        photo = element.photo,
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
                        markerVM = markerVM
                    )
                }
            }
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CategoryItem(
    id: String,
    photo: String,
    text: String,
    modifier: Modifier = Modifier,
    markerVM: MarkerViewModel
) {
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val defultIcons = mapOf(
        "Park" to Icons.Filled.Park,
        "Bookstore" to Icons.Filled.MenuBook,
        "Sports Center" to Icons.Filled.SportsBasketball,
        "Museum" to Icons.Filled.Museum,
        "Restaurant" to Icons.Filled.Restaurant
    )
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Log.i("image", "state of image: $photo")

            if (photo.equals(null)) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Defult images for marker",
                    modifier = Modifier
                        //.width(1000.dp)
                        //.height(1000.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()

                )
            } else {
                /*
                                Icon(
                                    imageVector = defultIcons[typeMarker]!!,
                                    contentDescription = "Close menu",
                                )

                                 */

                GlideImage(
                    model = photo.toUri(),
                    contentDescription = "$text image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.2F)
                        .fillMaxHeight()
                )


            }
            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
            Text(
                text = text,
                //modifier = modifier,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.background
            )

            //val methodIcons = arrayOf(Icons.Filled.Edit, Icons.Filled.DeleteSweep)
            //val arrMethods = arrayOf()
            //for (icon in methodIcons) {
            IconButton(
                onClick = { markerVM.changeShowBottomSheet(true) },
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
            IconButton(
                onClick = { markerVM.deleteMarker(id) },
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
            //}

        }
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