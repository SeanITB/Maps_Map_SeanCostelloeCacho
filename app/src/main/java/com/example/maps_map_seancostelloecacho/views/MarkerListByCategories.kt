
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel

@Composable
fun MarkerListContent(
    navController: NavHostController,
    markerVM: MarkerViewModel
) {
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val categoryMarkerList by markerVM.categoryMarkerList.observeAsState(emptyList())
    val getMarkersComplet by markerVM.markersComplet.observeAsState(false)
    val finishSort by markerVM.finishSort.observeAsState(false)

    markerVM.changeActualScreen("MarkerListByCategories")

    Log.i("MARKER", "actual type: $typeMarker")
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

    Log.i("MARKERS", "markers $categoryMarkerList")
    LaunchedEffect(key1 = finishSort) {
        navController.navigate(Routes.MarkerListScreen.route)
        markerVM.changeFinishSort(false)
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun MarkerListScreen(
    markerVM: MarkerViewModel
) {
    val typeMarker by markerVM.typeMarker.observeAsState("")
    val categoryMarkerList by markerVM.categoryMarkerList.observeAsState(emptyList())
    if (categoryMarkerList.size > 0) {
        LazyColumn() {
            stickyHeader {
                CategoryHeader(
                    text = categoryMarkerList[0].name,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                )
            }
            items(categoryMarkerList[0].items) { element ->
                CategoryItem(
                    text = element.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(16.dp)
                )
            }
        }
    } else {
        ErrorMsg(
            msg = if (typeMarker.equals("All markers")) "For the moment, you don't have any marker." else "For the moment, for the type ${markerVM.typeMarker} there isn't any marker.",
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
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.background
    )
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