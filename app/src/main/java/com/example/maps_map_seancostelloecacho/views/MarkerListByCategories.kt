package com.example.maps_map_seancostelloecacho.views

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel

@Composable
fun MarkerListScreen(markerVM: MarkerViewModel) {
    markerVM.changeActualScreen("MarkerListByCategories")

    val isFiltered by markerVM.isFiltered.observeAsState(false)

    Log.i("filtrar: ", "$isFiltered")


    if (isFiltered) {
        MarkerFilterCategoriesListScreen(markerVM)
    } else {
        MarkerCategoriesListScreen(markerVM)
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarkerFilterCategoriesListScreen(markerVM: MarkerViewModel) {
    val categoryMarkerList by markerVM.categoryMarkerList.observeAsState(emptyList())
    val getMarkersComplet by markerVM.getMarkersComplet.observeAsState(false)
    val typeMarker by markerVM.typeMarker.observeAsState("")

    /*
    LaunchedEffect(key1 = typeMarker) {
        markerVM.getFilterMarkers()
    }

     */

    LaunchedEffect(key1 = getMarkersComplet) {
        markerVM.createMapOfMarkers()
        markerVM.sortMarkerList()
    }

    if (categoryMarkerList.isNotEmpty()) {
        LazyColumn() {
            stickyHeader {
                CategoryHeader(
                    text = categoryMarkerList.get(0).name,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                )
            }
            items(categoryMarkerList.get(0).items) { element ->
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
            msg = "For the moment, for the type ${markerVM.typeMarker} there isn't any marker.",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarkerCategoriesListScreen(markerViewModel: MarkerViewModel) {
    val categoryMarkerList by markerViewModel.categoryMarkerList.observeAsState(emptyList())
    val getMarkersComplet by markerViewModel.getMarkersComplet.observeAsState(false)


    markerViewModel.getMarkers()
    LaunchedEffect(getMarkersComplet) {
        markerViewModel.createMapOfMarkers()
        markerViewModel.sortMarkerList()
    }

    if (categoryMarkerList.isNotEmpty()) {
        Log.i("makerList", "markers size: ${categoryMarkerList[0].items.size}")
        LazyColumn() {
            categoryMarkerList.forEach { category ->
                stickyHeader {
                    CategoryHeader(
                        text = category.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
                items(category.items) { marker ->

                    CategoryItem(
                        text = marker.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                }
            }
        }
    } else {
        ErrorMsg(
            msg = "For the moment, you don't have any marker.",
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
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun ErrorMsg(msg: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = msg)
    }
}