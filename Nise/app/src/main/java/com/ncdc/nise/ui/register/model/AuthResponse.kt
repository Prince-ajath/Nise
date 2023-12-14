package com.ncdc.nise.ui.register.model

data class AuthResponse(
    val status: Boolean,
    val message: String,
    val `data`: AuthData


)