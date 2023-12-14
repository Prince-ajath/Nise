package com.ncdc.nise.ui.survey.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.ui.survey.model.getSurveyDetailsResponse
import com.ncdc.nise.ui.survey.repo.SurveyDetailsRepo

class SurveyDetailsViewModel:ViewModel() {
    var surveyResponseData: MutableLiveData<getSurveyDetailsResponse>?=null
    var loading:MutableLiveData<Boolean>?=null

    fun getSurveyorApi(surveyorId:Int): LiveData<getSurveyDetailsResponse>? {
        surveyResponseData = SurveyDetailsRepo.getSurveyDataApi(surveyorId)
        loading=SurveyDetailsRepo.loading
        return surveyResponseData
    }
}