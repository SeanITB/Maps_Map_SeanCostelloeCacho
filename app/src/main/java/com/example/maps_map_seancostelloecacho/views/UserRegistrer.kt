package com.example.maps_map_seancostelloecacho.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel

@Composable
fun UsernRegistrerContent(navController: NavController, markerVM: MarkerViewModel) {
    val userName by markerVM.userName.observeAsState("")
    val password by markerVM.password.observeAsState("")
    val goToNext by markerVM.goToNext.observeAsState(false)
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
    val isLoading by markerVM.isLoading.observeAsState(true)

    var passwordCheck by rememberSaveable {
        mutableStateOf("")
    }
    var passwordVisibilty by rememberSaveable {
        mutableStateOf(false)
    }
    var passwordCheckVisibilty by rememberSaveable {
        mutableStateOf(false)
    }
    UserRegistrerView(
        modifier = Modifier.fillMaxSize(),
        userName = userName,
        onUserNameChange = { markerVM.changeUserName(it) },
        password = password,
        onPasswordChange = { markerVM.changePassword(it) },
        passwordCheck = passwordCheck,
        onPasswordCheckChange = { passwordCheck = it },
        passwordVisibilty = passwordVisibilty,
        onPasswordVisibilityChange = { passwordVisibilty = it },
        passwordCheckVisibility = passwordCheckVisibilty,
        onPasswordCheckVisibilityChange = { passwordCheckVisibilty = it },
        register = { markerVM.register() },
        goToNext = goToNext,
        navController = navController,
        navigationItems = navigationItems,
        isLoading = isLoading,
        proveThatItsAEmail = {markerVM.proveThatIstAEmail(userName)},
        passwordVerification = {markerVM.passwordVerification(password)}
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegistrerView(
    modifier: Modifier = Modifier,
    userName: String,
    onUserNameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordCheck: String,
    onPasswordCheckChange: (String) -> Unit,
    passwordVisibilty: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    passwordCheckVisibility: Boolean,
    onPasswordCheckVisibilityChange: (Boolean) -> Unit,
    register: () -> Unit,
    goToNext: Boolean,
    navController: NavController,
    navigationItems: Map<String, String>,
    isLoading: Boolean,
    proveThatItsAEmail: (String) -> Boolean,
    passwordVerification: (String) -> Boolean
) {
    val context = LocalContext.current
    val arrPasswords = arrayOf(password, passwordCheck)
    val arrVisibilities = arrayOf(passwordVisibilty, passwordCheckVisibility)
    val arrChangePasswordFunctions = arrayOf(onPasswordChange, onPasswordCheckChange)
    val arrChangeVisibilityFunctions =
        arrayOf(onPasswordVisibilityChange, onPasswordCheckVisibilityChange)
    val arrPasswordLable = arrayOf("Password", "Repeat password")
    val arrVisibilityLable = arrayOf("Password Visibility ", "Repeat Password Visibility")
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = userName,
            onValueChange = { onUserNameChange(it) },
            placeholder = { Text(text = "Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.background
            )
        )
        for (index in arrPasswords.indices) {
            TextField(
                value = arrPasswords[index],
                onValueChange = { arrChangePasswordFunctions[index](it) },
                placeholder = { Text(text = arrPasswordLable[index]) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.background
                ),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisibilty) {
                        Icons.Filled.VisibilityOff
                    } else {
                        Icons.Filled.Visibility
                    }
                    IconButton(onClick = { arrChangeVisibilityFunctions[index](!arrVisibilities[index]) }) { //toDo: chacer que solo se muestre uno quando canvias la visibilidad
                        Icon(imageVector = image, contentDescription = arrVisibilityLable[index])
                    }
                },
                visualTransformation = if (passwordVisibilty) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }
            )
        }
        Button(onClick = {
            if (!password.equals(passwordCheck)) {
                Toast.makeText(context, "Passwords are not the same.", Toast.LENGTH_LONG).show()
            } else if (!passwordVerification(password)) {
                Toast.makeText(context, "Incorrect password.", Toast.LENGTH_LONG).show()
            } else if (!proveThatItsAEmail(userName)) {
                Toast.makeText(context, "Incorrect email.", Toast.LENGTH_LONG).show()
            } else {
                register()
            }
        }
        ) {
            Text(text = "Registresr")
        }
        if (!isLoading) {
            if (goToNext) {
                navController.navigate(navigationItems["mapGeolocalisationScreen"]!!)
            } else {
                Toast.makeText(context, "User already exists.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
