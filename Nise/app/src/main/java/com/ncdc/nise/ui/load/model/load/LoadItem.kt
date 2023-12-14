package com.ncdc.nise.ui.load.model.load

data class LoadItem(
    val id: String,
    val created: String,
    val equipment: String,
    val hoursUse: String,
    val loadconsumption: String,
    val quantity: String,
    val surveyId: String,
    val totalload: String,
    val wattage: String
)