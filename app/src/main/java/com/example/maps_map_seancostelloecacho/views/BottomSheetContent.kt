package com.example.maps_map_seancostelloecacho.views

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.R
import com.example.maps_map_seancostelloecacho.navigation.Navigation
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.ui.theme.Maps_Map_SeanCostelloeCachoTheme
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel



// todo: setting expanding sizes in sub-composables
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
fun NavigateToTakePhotoContent(navController: NavController, navigateToCameraScreen: Navigation.CameraScreen) {
    Button(
        onClick = {
            navController.navigate(navigateToCameraScreen.route)
        }
    ) {
        Text(text = "photo")
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