package com.example.maps_map_seancostelloecacho.views

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maps_map_seancostelloecacho.models.MarkerData
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel

@Composable
fun MarkerListScreen(markerVM: MarkerViewModel) {
    markerVM.changeActualScreen("MarkerListByCategories")
    if (markerVM.isFiltered) {
        MarkerFilterListScreen(markerVM)
    } else {
        MarkerListByCategoriesScreen(markerVM)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarkerFilterListScreen(markerVM: MarkerViewModel) {
    val filterMarkerList: List<MarkerData> by markerVM.filterMarkerList.observeAsState(emptyList<MarkerData>())
    println("FilterMarkerList" + filterMarkerList.size)
    if (filterMarkerList.isNotEmpty()) {
        LazyColumn() {
            stickyHeader {
                CategoryHeader(text = markerVM.typeMarker.value!!)
            }
            items(filterMarkerList) { element ->
                categoryItem(text = element.name)
            }
        }
    } else {
        errorMsg(msg = "For the moment, for the type ${markerVM.typeMarker} there isn't any marker.")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarkerListByCategoriesScreen(markerViewModel: MarkerViewModel) {
    println("size of sorted list: "+ markerViewModel.categoryMarkerList.value!!.size)
    if (markerViewModel.categoryMarkerList.value!!.isNotEmpty()) {
        LazyColumn() {
            markerViewModel.categoryMarkerList.value!!.forEach { category ->
                stickyHeader {
                    CategoryHeader(text = category.name)
                }
                items(category.items) { marker ->
                    categoryItem(text = marker.name)
                }
            }
        }
    } else {
        errorMsg(msg = "For the moment, you don't have any marker.")
    }
}

@Composable
fun CategoryHeader(
    text: String,
    //modifier: Modifier = Modifier()
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
}

@Composable
fun categoryItem(
    text: String,
    //modifier: Modifier = Modifier()
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp),
        fontSize = 14.sp
    )
}

@Composable
fun errorMsg (msg: String) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = msg)
    }
}