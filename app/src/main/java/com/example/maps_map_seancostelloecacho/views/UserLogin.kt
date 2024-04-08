package com.example.maps_map_seancostelloecacho.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.TextField
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.runtime.setValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel
import javax.annotation.Untainted


@Composable
fun UserLoginContent(navController: NavController, markerVM: MarkerViewModel) {
    val userName by markerVM.userName.observeAsState("")
    val password by markerVM.password.observeAsState("")
    val goToNext by markerVM.goToNext.observeAsState(false)
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
    val isLoading by markerVM.isLoading.observeAsState(true)

    UserLoginView(userName = userName, password = , onUserNameChange = )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLoginView(
    userName: String,
    password: String,
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
) {
    var visability by rememberSaveable {
        mutableStateOf(false)
    }
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = userName,
            onValueChange = { onUserNameChange(it) },
            placeholder = { Text(text = "email") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.background
            ),
            singleLine = true,
        )
        TextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            placeholder = { Text(text = "Password" )},
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.background
            ),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (visability) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                IconButton(onClick = { visability = !visability }) { //toDo: chacer que solo se muestre uno quando canvias la visibilidad
                    Icon(imageVector = image, contentDescription = "Visibility password")
                }
            },
            visualTransformation = if (visability) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }
        )
    }
}

