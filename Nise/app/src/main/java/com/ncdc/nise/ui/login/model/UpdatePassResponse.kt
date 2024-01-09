package com.ncdc.nise.ui.login.model

data class updatePassResponse(
    val status: Boolean,
    val message: String,
    val newPassword:String,
)
