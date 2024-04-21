package com.example.maps_map_seancostelloecacho.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.TextStyle
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
import com.example.maps_map_seancostelloecacho.models.RegisterValidationContent
import com.example.maps_map_seancostelloecacho.models.TextFieldContent
import com.example.maps_map_seancostelloecacho.navigation.Routes
import com.example.maps_map_seancostelloecacho.ui.theme.Maps_Map_SeanCostelloeCachoTheme
import com.example.maps_map_seancostelloecacho.viewModel.MapViewModel


@Composable
fun UsernRegistrerContent(navController: NavController, markerVM: MapViewModel) { //toDo: ahora (con los canvios de mejorar el estate hosting) el canviar los campos de textField no funciona
    val email by markerVM.userName.observeAsState("")
    val password by markerVM.password.observeAsState("")
    val goToNext by markerVM.goToNext.observeAsState(false)
    val isLoading by markerVM.isLoading.observeAsState(true)
    var passwordCheck by rememberSaveable {
        mutableStateOf("")
    }
    val gmailContent = TextFieldContent(title = email, onTitleChange = { markerVM.changeUserName(it) })
    val passwordContent = TextFieldContent(title = password, onTitleChange = {markerVM.changePassword(it)})
    val passwordCheckContent = TextFieldContent(title = passwordCheck, onTitleChange = {passwordCheck = it})
    val context = LocalContext.current
    val registerValidationContent = RegisterValidationContent(password, passwordCheck, context, email)
    UserRegisterView(
        registerValidation = { markerVM.registerValidation(validation = registerValidationContent)},
        //onGamigChange = { markerVM.changeUserName(it) },
        validationContent = registerValidationContent,
        gmailContent = gmailContent,
        passwordContent = passwordContent,
        passwordCheckContent = passwordCheckContent,
        goToNext = goToNext,
        navController = navController,
        isLoading = isLoading,
    )
}

@Composable
fun UserRegisterView(
    registerValidation: (RegisterValidationContent) -> Boolean,
    //onGamigChange: (String) -> Unit,
    validationContent: RegisterValidationContent,
    gmailContent: TextFieldContent,
    passwordContent: TextFieldContent,
    passwordCheckContent: TextFieldContent,
    goToNext: Boolean,
    navController: NavController?,
    isLoading: Boolean,
) {
    var show by rememberSaveable {
        mutableStateOf(false)
    }
    var showPasswordInfo by rememberSaveable {
        mutableStateOf(false)
    }
    var registerContentIsCorrect by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.05F))
        TextField(
            value = gmailContent.title,
            onValueChange = { gmailContent.onTitleChange(it) },
            placeholder = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Name ", style = TextStyle(fontWeight = FontWeight.Bold))
                    Text(text = "(required)")
                }
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.6F)
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.05F))
        Password(
            modifier = Modifier.fillMaxWidth(0.6F),
            passwordContent = passwordContent,
            passwordCheckContent = passwordCheckContent,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(0.5F)
                .fillMaxHeight(0.05F),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Password info",
                modifier = Modifier
                    .clickable {
                        showPasswordInfo = true
                    }
            )
        }
        MyDialog(show = showPasswordInfo) {
            showPasswordInfo = false
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.03F))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Do you have an account? ",
            )
            Text(
                text = "Login!!!",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable {
                    navController?.navigate(Routes.LoginScreen.route)
                }
            )
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.05F))
        RegisterButton(
            registerContentIsCorrect = registerContentIsCorrect,
            onRegisterContentCorrescChange = { registerContentIsCorrect = it},
            registerValidation = {registerValidation(validationContent)},
            validationContent = validationContent,
            modifier = Modifier.fillMaxWidth(0.6F),
            onShowChange = { show = it },
        )
        if (isLoading) {
            WhileLoading(
                show = show,
                onDismiss = { show = false },
                isLoading = isLoading
            )
        } else {
            LaunchedEffect(key1 = goToNext) {
                if (goToNext) {
                    navController!!.navigate(Routes.MyDrawer.route)
                }
            }
        }
    }
}

@Composable
fun MyDialog(show: Boolean, onDismiss: (Boolean) -> Unit) {
    if (show) {
        Dialog(
            onDismissRequest = { onDismiss(false) },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = true)
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.onSurface)
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                ShowPasswordInstructions(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

        }
    }
}


@Composable
fun ShowPasswordInstructions(modifier: Modifier = Modifier) {
    Text(
        text = "Password characteristics: ",
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        color = MaterialTheme.colorScheme.primary,

        )
    Text(
        text = """
            - 1 digit
            - 1 letter
            - 6 characters
        """.trimIndent(),
        fontSize = 15.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    )
}
@Composable
fun Password(
    modifier: Modifier,
    passwordContent: TextFieldContent,
    passwordCheckContent: TextFieldContent
) {
    var visability by rememberSaveable {
        mutableStateOf(false)
    }
    var visabilityCheck by rememberSaveable {
        mutableStateOf(false)
    }
    val arrItems = arrayOf(passwordContent.title, passwordCheckContent.title)
    val arrChangeItems = arrayOf(passwordContent.onTitleChange, passwordCheckContent.onTitleChange)
    val arrPasswordLable = arrayOf("Password ", "Repeat password ")
    val requiredText =  "(required)"
    val arrVisibility = arrayOf(visability, visabilityCheck)
    val arrChangeVisibility =
        arrayOf({ visability = !visability }, { visabilityCheck = !visabilityCheck })
    for (index in arrItems.indices) {
        TextField(
            value = arrItems[index],
            onValueChange = { arrChangeItems[index](it) },
            placeholder = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = arrPasswordLable[index], style = TextStyle(fontWeight = FontWeight.Bold))
                    if (index == 0) Text(text = requiredText)
                }
                 },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
            singleLine = false,
            maxLines = 2,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (arrVisibility[index]) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                IconButton(onClick = arrChangeVisibility[index]) {
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
        if (index < 2) Spacer(modifier = Modifier.fillMaxHeight(0.05F))
    }
}

@Composable
fun RegisterButton(
    registerContentIsCorrect: Boolean,
    onRegisterContentCorrescChange: (Boolean) -> Unit,
    modifier: Modifier,
    registerValidation: (RegisterValidationContent) -> Boolean,
    validationContent: RegisterValidationContent,
    onShowChange: (Boolean) -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = {
            onRegisterContentCorrescChange(registerValidation(validationContent)) 
            if (registerContentIsCorrect) onShowChange(true)
        }
    ) {
        Text(text = "Register")
    }
}




@Composable
fun WhileLoading(
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
                Column(
                    Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.primary
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
        UserRegisterView(
            registerValidation = {false},
            validationContent = RegisterValidationContent("", "", LocalContext.current, ""),
            gmailContent = TextFieldContent("", {}),
            passwordContent = TextFieldContent("", {}),
            passwordCheckContent = TextFieldContent("", {}),
            goToNext = false,
            navController = null,
            isLoading = false
        )
    }
}


