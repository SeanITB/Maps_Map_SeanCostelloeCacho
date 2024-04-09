package com.example.maps_map_seancostelloecacho.views

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
import androidx.compose.runtime.SideEffect
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

    if (isFiltered) {
        MarkerFilterListScreen(markerVM)
    } else {
        MarkerListByCategoriesScreen(markerVM)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarkerFilterListScreen(markerVM: MarkerViewModel) {
    val filterMarkerList by markerVM.markerList.observeAsState(emptyList())
    if (filterMarkerList.isNotEmpty()) {
        LazyColumn() {
            stickyHeader {
                CategoryHeader(
                    text = markerVM.typeMarker.value!!,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                )
            }
            items(filterMarkerList) { element ->
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
        ErrorMsg(msg = "For the moment, for the type ${markerVM.typeMarker} there isn't any marker.", modifier = Modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarkerListByCategoriesScreen(markerViewModel: MarkerViewModel) {
    val categoryMarkerList by markerViewModel.categoryMarkerList.observeAsState(emptyList())
    val getMarkersComplet by markerViewModel.getMarkersComplet.observeAsState(false)

    markerViewModel.getMarkers()
    LaunchedEffect(getMarkersComplet) {
        markerViewModel.createMapOfMarkers()
        markerViewModel.sortMarkerList()
    }

    Log.i("makerList", "${categoryMarkerList.size}")
    if (categoryMarkerList.isNotEmpty()) {
        LazyColumn() {
            categoryMarkerList.forEach { category ->
                stickyHeader {
                    CategoryHeader(text = category.name)
                }
                items(category.items) { marker ->
                    CategoryItem(text = marker.name)
                }
            }
        }
    } else {
        ErrorMsg(msg = "For the moment, you don't have any marker.", modifier = Modifier.fillMaxSize())
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
        fontSize = 14.sp
    )
}

@Composable
fun ErrorMsg (msg: String, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = msg)
    }
}