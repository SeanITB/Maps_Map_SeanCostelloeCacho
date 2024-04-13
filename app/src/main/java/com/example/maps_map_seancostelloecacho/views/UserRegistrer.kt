package com.example.maps_map_seancostelloecacho.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.maps_map_seancostelloecacho.models.UserPrefs
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.ui.theme.Maps_Map_SeanCostelloeCachoTheme
import com.example.maps_map_seancostelloecacho.viewModel.MarkerViewModel

@Composable
fun UsernRegistrerContent(navController: NavController, markerVM: MarkerViewModel) {
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
    var passwordCheck by rememberSaveable {
        mutableStateOf("")
    }
    var show by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowPaswordInstructions(modifier = Modifier.fillMaxWidth(0.6F))
        NameAndPassword(
            modifier = Modifier.fillMaxWidth(0.6F),
            userName = userName,
            password = password,
            passwordCheck = passwordCheck,
            onUserNameChange = { onUserNameChange(it) },
            onPasswordChange = { onPasswordChange(it) },
            onPasswordChaeckChange = { passwordCheck = it }
        )
        Text(
            text = "Do you have an account? Login!!",
            modifier = Modifier.clickable {
                navController?.navigate(Routes.LoginScreen.route)
            }
        )
        RegisterButton(
            modifier = Modifier.fillMaxWidth(0.6F),
            userName = userName,
            password = password,
            passwordCheck = passwordCheck,
            context = context,
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
            if (goToNext) {
                navController!!.navigate(navigationItems["mapGeolocalisationScreen"]!!)
            } else {//toDo: quando entra por aqui que se salga del dialog
                Toast.makeText(context, "User already exists.", Toast.LENGTH_LONG).show()
            }
        }

    }
}

@Composable
fun ShowPaswordInstructions(modifier: Modifier = Modifier) {
    Text(text = "Password characteristics: ", modifier = modifier, fontWeight = FontWeight.Bold)
    Text(
        text = """
            - 1 digit
            - 1 postmark expression
            - 1 capital letter
            - 6 characters
        """.trimIndent(),
        modifier = modifier
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameAndPassword(
    modifier: Modifier,
    userName: String,
    password: String,
    passwordCheck: String,
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordChaeckChange: (String) -> Unit
) {
    val arrItems = arrayOf(userName, password, passwordCheck)
    val arrChangeItems = arrayOf(onUserNameChange, onPasswordChange, onPasswordChaeckChange)
    val arrPasswordLable = arrayOf("Name", "Password", "Repeat password")

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
            modifier = modifier
        )
    }

}

@Composable
fun RegisterButton(
    modifier: Modifier = Modifier,
    userName: String,
    password: String,
    passwordCheck: String,
    context: Context,
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
