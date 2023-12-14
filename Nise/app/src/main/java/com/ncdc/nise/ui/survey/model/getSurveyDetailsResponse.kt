package com.ncdc.nise.ui.survey.model

data class getSurveyDetailsResponse(
    val status: Boolean,
    val message: String,
    val items: ArrayList<SurveyDetailsItem>


)