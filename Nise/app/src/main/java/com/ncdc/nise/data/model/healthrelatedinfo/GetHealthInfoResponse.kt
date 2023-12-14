package com.ncdc.nise.data.model.healthrelatedinfo

data class GetHealthInfoResponse(
    val status: Boolean,
    val message: String,
    val `data`: HealthRelatedData


)