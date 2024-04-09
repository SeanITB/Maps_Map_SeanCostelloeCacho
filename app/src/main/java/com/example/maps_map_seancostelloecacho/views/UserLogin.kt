package com.example.maps_map_seancostelloecacho.views

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.TextField
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.models.UserPrefs
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun UserLoginContent(navController: NavController, markerVM: MarkerViewModel) {
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
    val userName by markerVM.userName.observeAsState("")
    val password by markerVM.password.observeAsState("")
    val goToNext by markerVM.goToNext.observeAsState(false)
    val isLoading by markerVM.isLoading.observeAsState(true)
    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())
    var thereIsData by rememberSaveable {
        mutableStateOf(false)
    }

    //only enters the first time
    if (!thereIsData && storedUserData.value.isNotEmpty() && storedUserData.value.get(0) != "" && storedUserData.value.get(1) != "") {
        Log.i("USERÑ", "Que esta passando")
        markerVM.changeUserName(storedUserData.value.get(0))
        markerVM.changePassword(storedUserData.value.get(1))
        thereIsData = true
    }

    UserLoginView(
        navController = navController,
        navigationItems = navigationItems,
        userName = userName,
        password = password,
        goToNext = goToNext,
        isLoading = isLoading,
        context = context,
        userPrefs = userPrefs,
        storedUserData = storedUserData,
        onUserNameChange = { markerVM.changeUserName(it) },
        onPasswordChange = { markerVM.changePassword(it) },
        login = { markerVM.login() },
        modifyProcessing = { markerVM.modifiyProcessing() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLoginView(
    navController: NavController?,
    navigationItems: Map<String, String>,
    userName: String,
    password: String,
    goToNext: Boolean,
    isLoading: Boolean,
    context: Context,
    userPrefs: UserPrefs,
    storedUserData: State<List<String>>,
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    login: () -> Unit,
    modifyProcessing: () -> Unit
) {
    var visability by rememberSaveable {
        mutableStateOf(false)
    }
    var show by rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = userName,
            onValueChange = {
                onUserNameChange(it)
            },
            placeholder = { Text(text = "Email") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.background
            ),
            singleLine = true,
        )
        //println("user: "+ storeUserData.value.get(0))
        TextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            placeholder = { Text(text = "Password") },
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
                IconButton(onClick = { visability = !visability }) {
                    Icon(imageVector = image, contentDescription = "Visibility password")
                }
            },
            visualTransformation = if (visability) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }
        )
        //Log.i("USER", storeUserData.value.get(1))
        Button(
            onClick = {
                if (storedUserData.value.isEmpty() || storedUserData.value.get(0) == "" && storedUserData.value.get(1) == "") {
                    CoroutineScope(Dispatchers.IO).launch {
                        userPrefs.saveUserData(userName, password)
                    }
                }
                onUserNameChange(storedUserData.value.get(0))
                onPasswordChange(storedUserData.value.get(1))
                login()
            }
        ) {
            Text(text = "Login")
        }
    }
    if (isLoading) {
        WhileLoding(
            show = show,
            onDismiss = { show = false },
            isLoading = isLoading
        )
    } else {
        if (goToNext) {
            navController!!.navigate(navigationItems["mapGeolocalisationScreen"]!!)
        } else {//toDo: quando entra por aqui que se salga del dialog
            Toast.makeText(context, "User already exists.", Toast.LENGTH_LONG).show()
        }
    }
}
