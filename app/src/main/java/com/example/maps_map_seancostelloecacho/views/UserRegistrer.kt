package com.example.maps_map_seancostelloecacho.views

import android.content.Context
import android.view.WindowInsets.Side
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.ui.theme.Maps_Map_SeanCostelloeCachoTheme
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel

@Composable
fun UsernRegistrerContent(navController: NavController, markerVM: MapViewModel) {
    val navigationItems by markerVM.navigationItems.observeAsState(mapOf())
    val userName by markerVM.userName.observeAsState("")
    val password by markerVM.password.observeAsState("")
    val goToNext by markerVM.goToNext.observeAsState(false)
    val isLoading by markerVM.isLoading.observeAsState(true)
    val context = LocalContext.current


    UserRegistrerView(
        userName = userName,
        onUserNameChange = { markerVM.changeUserName(it) },
        password = password,
        onPasswordChange = { markerVM.changePassword(it) },
        register = { markerVM.register() },
        goToNext = goToNext,
        navController = navController,
        navigationItems = navigationItems,
        isLoading = isLoading,
        proveThatItsAEmail = { markerVM.proveThatItsAEmail(userName) },
        passwordVerification = { markerVM.passwordVerification(password) },
        //registerCheck = { markerVM.registerCheck(context, "") } //toDo: no se hacer un estate hoistig con metodos con mas de un parametro
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegistrerView(
    userName: String,
    onUserNameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    register: () -> Unit,
    goToNext: Boolean,
    navController: NavController?,
    navigationItems: Map<String, String>,
    isLoading: Boolean,
    proveThatItsAEmail: (String) -> Boolean,
    passwordVerification: (String) -> Boolean,
) {
    var show by rememberSaveable {
        mutableStateOf(false)
    }
    var passwordCheck by rememberSaveable {
        mutableStateOf("")
    }
    val context = LocalContext.current
    var passwordIncorrect by rememberSaveable {
        mutableStateOf(true)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!passwordIncorrect) {
            ShowPaswordInstructions(modifier = Modifier.fillMaxWidth(0.6F))
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.05F))
        TextField(
            value = userName,
            onValueChange = { onUserNameChange(it) },
            placeholder = { Text(text = "Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.background
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.6F)
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.05F))
        Password(
            modifier = Modifier.fillMaxWidth(0.6F),
            password = password,
            passwordCheck = passwordCheck,
            onPasswordChange = { onPasswordChange(it) },
            onPasswordCheckChange = { passwordCheck = it}
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.05F))

        Text(
            text = "Do you have an account? Login!!",
            modifier = Modifier.clickable {
                navController?.navigate(Routes.LoginScreen.route)
            }
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.05F))
        RegisterButton(
            modifier = Modifier.fillMaxWidth(0.6F),
            passwordIncorrect = passwordIncorrect,
            userName = userName,
            password = password,
            passwordCheck = passwordCheck,
            context = context,
            onPasswordIncorrectChange = {passwordIncorrect = it},
            onShowChange = { show = it },
            passwordVerification = { passwordVerification(password) },
            proveThatItsAEmail = { proveThatItsAEmail(userName) },
            register = register
        )
        if (isLoading) {
            WhileLoding(
                show = show,
                onDismiss = { show = false },
                isLoading = isLoading
            )
        } else {
            LaunchedEffect(key1 = goToNext) {
                if (goToNext) {
                    navController!!.navigate(Routes.MyDrawer.route)
                } else {//toDo: quando entra por aqui que se salga del dialog
                    Toast.makeText(context, "User already exists.", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}

@Composable
fun ShowPaswordInstructions(modifier: Modifier = Modifier) {
    Text(
        text = "Password characteristics: ",
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
        )
    Text(
        text = """
            - 1 digit
            - 1 postmark expression
            - 1 capital letter
            - 6 characters
        """.trimIndent(),
        fontSize = 12.sp,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(
    modifier: Modifier,
    password: String,
    passwordCheck: String,
    onPasswordChange: (String) -> Unit,
    onPasswordCheckChange: (String) -> Unit,
) {
    var visability by rememberSaveable {
        mutableStateOf(false)
    }
    var visabilityCheck by rememberSaveable {
        mutableStateOf(false)
    }
    val arrItems = arrayOf(password, passwordCheck)
    val arrChangeItems = arrayOf(onPasswordChange,onPasswordCheckChange)
    val arrPasswordLable = arrayOf("Password", "Repeat password")
    val arrVisibility = arrayOf(visability, visabilityCheck)
    val arrChangeVisibility = arrayOf({visability = !visability}, {visabilityCheck = !visabilityCheck})
    for (index in arrItems.indices) {
        TextField(
            value = arrItems[index],
            onValueChange = { arrChangeItems[index](it) },
            placeholder = { Text(text = arrPasswordLable[index]) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.background
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (arrVisibility[index]) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                IconButton(onClick =  arrChangeVisibility[index] ) {
                    Icon(imageVector = image, contentDescription = "Visibility password")
                }
            },
            visualTransformation = if (arrVisibility[index]) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            modifier = modifier
        )
    }

}

@Composable
fun RegisterButton(
    modifier: Modifier = Modifier,
    passwordIncorrect: Boolean,
    userName: String,
    password: String,
    passwordCheck: String,
    context: Context,
    onPasswordIncorrectChange: (Boolean) -> Unit,
    onShowChange: (Boolean) -> Unit,
    passwordVerification: (String) -> Boolean,
    proveThatItsAEmail: (String) -> Boolean,
    register: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = {
            onShowChange(true)
            if (!password.equals(passwordCheck)) {
                Toast.makeText(context, "Passwords are not the same.", Toast.LENGTH_LONG).show()
            } else if (!passwordVerification(password)) {
                onPasswordIncorrectChange(!passwordIncorrect)
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
}

@Composable
fun WhileLoding(
    show: Boolean,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    if (show) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = true)
        ) {
            if (isLoading) {
                Column {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Waiting for a response...",
                        modifier = Modifier
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun UserRegisterPreview() {
    Maps_Map_SeanCostelloeCachoTheme {
        UserRegistrerView(
            userName = "Jose Antonio",
            onUserNameChange = {},
            password = "",
            onPasswordChange = {},
            register = { /*TODO*/ },
            goToNext = false,
            navController = null,
            navigationItems = mapOf(),
            isLoading = false,
            proveThatItsAEmail = { false },
            passwordVerification = { false }
        )
    }
}
