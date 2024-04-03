package com.example.maps_map_seancostelloecacho.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel
import com.google.android.gms.auth.api.identity.SignInPassword

@Composable
fun UserLoginContent(markerVM: MarkerViewModel) {
    val userId by markerVM.userId.observeAsState("")
    val loggedUser by markerVM.loggedUser.observeAsState("")
    val isLoading by markerVM.isLoading.observeAsState(true)
    var userName by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var passwordCheck by rememberSaveable {
        mutableStateOf("")
    }
    var passwordVisibilty by rememberSaveable {
        mutableStateOf(false)
    }
    var passwordCheckVisibilty by rememberSaveable {
        mutableStateOf(false)
    }
    UserLoginView(
        modifier = Modifier.fillMaxSize(),
        userId = userId,
        onUserIdChange = {markerVM.chngeUserId(it)},
        loggedUser = loggedUser,
        onLoggedUserChange = {markerVM.changeLoggedUser(it)},
        isLoading = isLoading,
        onIsLoadingChange = {markerVM.modifiyProcessing()},
        userName = userName,
        onUserNameChange = {userName = it},
        password = password,
        onPasswordChange = {password = it},
        passwordCheck = passwordCheck,
        onPasswordCheckChange = {passwordCheck = it}
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLoginView(
    modifier: Modifier = Modifier,
    userId: String,
    onUserIdChange: (String) -> Unit,
    loggedUser: String,
    onLoggedUserChange: (String) -> Unit,
    isLoading: Boolean,
    onIsLoadingChange: () -> Unit,
    userName: String,
    onUserNameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordCheck: String,
    onPasswordCheckChange: (String) -> Unit,
    passwordVisibilty: Boolean
) {
    val arrValues = arrayOf(password, passwordCheck)
    val arrFunctions = arrayOf(onPasswordChange, onPasswordCheckChange)
    val arrLabel = arrayOf("Password", "Repeat password")
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = userName,
            onValueChange = {onUserNameChange(it)},
            placeholder = { Text(text = "Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.background
            )
        )
        for (index in arrValues.indices) {
            TextField(
                value = arrValues[index],
                onValueChange = {arrFunctions[index](it)},
                placeholder = { Text(text = arrLabel[index]) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.background
                )
            )
        }

    }
}
