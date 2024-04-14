package com.example.maps_map_seancostelloecacho

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.maps_map_seancostelloecacho.navigation.NavigateLoginAndRegister
import com.example.maps_map_seancostelloecacho.ui.theme.Maps_Map_SeanCostelloeCachoTheme
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Maps_Map_SeanCostelloeCachoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val markerVM by viewModels<MapViewModel>()
                    val TIME : Int = 1000
                    val navControllerLR = rememberNavController()
                    NavigateLoginAndRegister(navControllerLR = navControllerLR, TIME = TIME, markerVM = markerVM)
                }
            }
        }
    }
}



