package com.example.maps_map_seancostelloecacho.models

import android.content.Context

data class RegisterValidationContent (
    val password: String,
    val passwordCheck: String,
    val context: Context,
    val email: String,
)
