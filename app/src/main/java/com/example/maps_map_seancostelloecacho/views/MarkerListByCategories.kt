import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel

@Composable
fun MarkerListContent(
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

    MarkerListScreen(markerVM = markerVM)

}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun MarkerListScreen(
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
                        photo = element.photo,
                        text = element.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(16.dp)
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
    photo: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable {
                    //toDo
                }
        ) {
            GlideImage(
                model = photo.toUri(),
                contentDescription = "$text image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Text(
                text = text,
                //modifier = modifier,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.background
            )
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