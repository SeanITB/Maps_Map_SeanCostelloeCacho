package com.example.maps_map_seancostelloecacho.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.navigation.NavigationItems


// todo (done): setting expanding sizes in sub-composables
@Composable
fun NameMarkerContent(
    modifier: Modifier = Modifier,
    name: String,
    onNameChange: (String) -> Unit,
    ) {
    TextField(
        value = name,
        onValueChange =  {onNameChange(it)} ,
        placeholder = { Text(text = "Name") },
        modifier = modifier
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeMarkerContent(
    actualScreen: String,
    typeMarker: String,
    onTypeMarkerChange: (String) -> Unit,
    expandedTopBar: Boolean,
    onExpandedTopBarChange: (Boolean) -> Unit,
    expandedBottomSheet: Boolean,
    onExpandedBottomSheetChange: (Boolean) -> Unit,
    whenMarkerTypedChanged: (String) -> Unit
    ) {
    val genders =
        if (actualScreen != "BottomSheet") {
            arrayOf(
                "All markers",
                "Park",
                "Bookstore",
                "Sports Center",
                "Museum",
                "Restaurant"
            )
        } else {
            arrayOf(
                "Park",
                "Bookstore",
                "Sports Center",
                "Museum",
                "Restaurant"
            )
        }
    Box {
        OutlinedTextField(
            value = typeMarker,
            onValueChange =  onTypeMarkerChange,
            label = { Text(text = "Marker Type", color = MaterialTheme.colorScheme.primary) },
            enabled = false,
            readOnly = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .clickable { //toDo: canbiar el estado de un booleano
                    if (actualScreen != "BottomSheet") { //toDo: para quando habrel el bottom sheet no sea habra el top bar, pero no funciona
                        onExpandedTopBarChange(!expandedTopBar)
                    } else {
                        onExpandedBottomSheetChange(!expandedBottomSheet)
                    }
                }
                .fillMaxWidth(0.6f)
        )
        DropdownMenu(
            expanded =
            if (actualScreen != "BottomSheet") {
                expandedTopBar
            } else {
                expandedBottomSheet
            },
            onDismissRequest = {
                if (actualScreen != "BottomSheet") {
                    onExpandedTopBarChange(!expandedTopBar)
                } else {
                    onExpandedBottomSheetChange(!expandedBottomSheet)
                }
            },
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = gender,
                            color = MaterialTheme.colorScheme.background
                        )
                    },
                    onClick = {
                        whenMarkerTypedChanged(gender)
                    }
                )
            }
        }
    }
}

@Composable
fun DescriptionMarkerContent(
    modifier: Modifier = Modifier,
    descriptionMarker: String,
    onDescriptionMarkerChange: (String) -> Unit
) {
    TextField(
        value = descriptionMarker,
        onValueChange = { onDescriptionMarkerChange(it) },
        placeholder = { Text(text = "Description (optional)") },
        modifier = modifier
    )
}

@Composable
fun NavigateToPhotoContent(navController: NavController, navigationItems: Map<String, String>) {
    Row{
        Button(
            onClick = {
                navController.navigate(navigationItems["cameraScreen"]!!) // Camera screen
            }
        ) {
            Text(text = "photo")
        }
        Button(
            onClick = {
                navController.navigate(navigationItems["galleryScreen"]!!) // Gallery Screen
            }
        ) {
            Text(text = "galeria")
        }
    }

}

@Composable
fun WhenAddMarkerContent(
    whenAddMarker: (Context) -> Unit,
    context: Context
) {
    Button(
        onClick = {
            whenAddMarker(context)
        }
    ) {
        Text(text = "Add Marker")
    }
}

/*
@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
private fun BottomSheetPreview(
    @PreviewParameter(BottomSheetParameterProviader::class) photo: Int
) {
    Maps_Map_SeanCostelloeCachoTheme {
        PhotoDefultItem(photo = photo)
    }
}


 */