package com.ncdc.nise.ui.load.model.load

data class LoadItemResponse(
    val status: Boolean,
    val message: String,
    val items: ArrayList<LoadItem>


)