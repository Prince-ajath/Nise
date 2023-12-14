package com.ncdc.nise.data.model.vaccineStorage

data class VaccineData(
    val createdDate: String,
    val id: String,
    val refrigeAvailable: String,
    val refrigeWorking: String,
    val solarPowered: String,
    val tempMaintained: String,
    val usagePattern: String
)