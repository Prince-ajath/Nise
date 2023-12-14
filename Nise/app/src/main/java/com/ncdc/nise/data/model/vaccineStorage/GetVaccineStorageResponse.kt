package com.ncdc.nise.data.model.vaccineStorage

data class GetVaccineStorageResponse(
    val status: Boolean,
    val message: String,
    val `data`: VaccineData
)