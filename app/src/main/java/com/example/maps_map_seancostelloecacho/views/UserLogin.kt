package com.example.maps_map_seancostelloecacho.views

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun UserLoginContent() {
    
}
/*
@Composable
fun UserLoginView() {
    val arrVisibilityLable = arrayOf("Password Visibility ", "Repeat Password Visibility")
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

 */