package com.ncdc.nise.data.model

data class GetWaterSourceResponse(
    val status: Boolean,
    val message: String,
    val `data`: WaterSourceData


)