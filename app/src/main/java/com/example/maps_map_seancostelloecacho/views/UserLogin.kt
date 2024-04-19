package com.example.maps_map_seancostelloecacho.views

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.models.UserPrefs
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun UserLoginOnCreateContent(navController: NavController, markerVM: MapViewModel) {
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
    val userName by markerVM.userName.observeAsState("")
    val password by markerVM.password.observeAsState("")
    val goToNext by markerVM.goToNext.observeAsState(false)
    val isLoading by markerVM.isLoading.observeAsState(true)
    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())

    if (
        storedUserData.value.isNotEmpty() &&
        storedUserData.value.get(0) != "" &&
        storedUserData.value.get(1) != ""
    ) {
        markerVM.changeUserName(storedUserData.value.get(0))
        markerVM.changePassword(storedUserData.value.get(1))
        markerVM.login(context)
    }

    UserLoginView(
        navController = navController,
        navigationItems = navigationItems,
        userName = userName,
        password = password,
        goToNext = goToNext,
        onGoToNextChange = { markerVM.changeGoToNext(it) },
        isLoading = isLoading,
        context = context,
        userPrefs = userPrefs,
        storedUserData = storedUserData,
        onUserNameChange = { markerVM.changeUserName(it) },
        onPasswordChange = { markerVM.changePassword(it) },
        login = { markerVM.login(context) },
        modifyProcessing = { markerVM.modifiyProcessing() }
    )

}

@Composable
fun UserLoginOnLogOutContent(navControllerLR: NavController, markerVM: MapViewModel) {
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
    val userName by markerVM.userName.observeAsState("")
    val password by markerVM.password.observeAsState("")
    val goToNext by markerVM.goToNext.observeAsState(false)
    val isLoading by markerVM.isLoading.observeAsState(true)
    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())
    var firstTime by rememberSaveable {
        mutableStateOf(true)
    }

    if (
        firstTime &&
        storedUserData.value.isNotEmpty() &&
        storedUserData.value.get(0) != "" &&
        storedUserData.value.get(1) != ""
    ) {
        markerVM.changeUserName(storedUserData.value.get(0))
        markerVM.changePassword(storedUserData.value.get(1))
        firstTime = false
    }

    UserLoginView(
        navController = navControllerLR,
        navigationItems = navigationItems,
        userName = userName,
        password = password,
        goToNext = goToNext,
        onGoToNextChange = { markerVM.changeGoToNext(it) },
        isLoading = isLoading,
        context = context,
        userPrefs = userPrefs,
        storedUserData = storedUserData,
        onUserNameChange = { markerVM.changeUserName(it) },
        onPasswordChange = { markerVM.changePassword(it) },
        login = { markerVM.login(context) },
        modifyProcessing = { markerVM.modifiyProcessing() }
    )

}

@Composable
fun UserLoginView(
    navController: NavController?,
    navigationItems: Map<String, String>,
    userName: String,
    password: String,
    goToNext: Boolean,
    onGoToNextChange: (Boolean) -> Unit,
    isLoading: Boolean,
    context: Context,
    userPrefs: UserPrefs,
    storedUserData: State<List<String>>,
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    login: (Context) -> Unit,
    modifyProcessing: () -> Unit
) {
    var visability by rememberSaveable {
        mutableStateOf(false)
    }
    var show by rememberSaveable {
        mutableStateOf(false)
    }
    var check by rememberSaveable {
        mutableStateOf(
            false
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = userName,
            onValueChange = { onUserNameChange(it) },
            placeholder = { Text(text = "Email") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.05F))
        TextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            placeholder = { Text(text = "Password") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
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
        Spacer(modifier = Modifier.fillMaxHeight(0.05F))
        SaveAcountCheckBox(
            userPrefs,
            storedUserData,
            userName,
            password,
            check,
            { check = it }
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.05F))
        Text(
            text = "Don't have an account? Register!!",
            modifier = Modifier.clickable {
                onUserNameChange("")
                onPasswordChange("")
                navController?.navigate(Routes.RegisterScreen.route)
            }
        )
        Button(
            onClick = {
                login(context)
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
        Log.i("isChack", "isChack: $check")
        LaunchedEffect(key1 = goToNext) {
            if (goToNext) {
                onGoToNextChange(false)
                navController!!.navigate(Routes.MyDrawer.route)
                if (check) userPrefs.saveUserData(userName, password)
            }
        }
    }
}

@Composable
private fun SaveAcountCheckBox(
    userPrefs: UserPrefs,
    storedUserData: State<List<String>>,
    userName: String,
    password: String,
    check: Boolean,
    onCheckChange: (Boolean) -> Unit
) {
    LaunchedEffect(key1 = storedUserData.value.isNotEmpty()) {
        if (
            storedUserData.value.isNotEmpty() &&
            storedUserData.value.get(0) != "" &&
            storedUserData.value.get(1) != ""
        ) {
            onCheckChange(true)
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "Save the account: ", fontSize = 12.sp)
        Checkbox(
            checked = check,
            onCheckedChange = {
                onCheckChange(!check)
                CoroutineScope(Dispatchers.IO).launch {//toDo: eliminar registros de login si quito el check
                    if (!check) userPrefs.saveUserData("", "")
                }
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.background,
                uncheckedColor = MaterialTheme.colorScheme.secondary,
                checkmarkColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}


