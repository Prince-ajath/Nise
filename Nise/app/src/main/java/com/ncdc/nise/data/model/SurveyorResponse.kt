package com.ncdc.nise.data.model

import com.google.gson.annotations.SerializedName

data class SurveyorResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("message") val message : String,
    @SerializedName("id") val surveyorId : String,

)
