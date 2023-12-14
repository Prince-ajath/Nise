package com.ncdc.nise.ui.survey.model

data class SurveyDetailsItem(
    val id: String,
    val surveyId: String,
    val hfName: String,
    val hfCategory: String,
    val hfState: String,
    val hfDistrictName: String,
    val status:Int,
    val created: String)