package com.ncdc.nise.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.data.model.GetWaterSourceResponse
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.repository.HealthRelatedRepo


import com.ncdc.nise.repository.WaterDetailsRepo

class WaterDetailsViewModel :ViewModel() {
    var servicesLiveData: MutableLiveData<SurveyorResponse>? = null
    var waterResoponse:MutableLiveData<GetWaterSourceResponse>?=null
    var loading:MutableLiveData<Boolean>?=null

    fun addWaterDetails(surveyId:Int,wDrinking:String,wStorage:String,wPumpingMechnism:String,wMoterPumpSet:String,wOperationalHrs:String,wQWReqiurd:String,wQuality:String,wTable:String,wTank:String,wAvlbility:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = WaterDetailsRepo.addWaterDetailsApi(surveyId,wDrinking,wStorage,wPumpingMechnism,wMoterPumpSet,wOperationalHrs,wQWReqiurd,wQuality,wTable,wTank,wAvlbility)
        return servicesLiveData
    }

    fun updateWaterDetails(surveyId:Int,wDrinking:String,wStorage:String,wPumpingMechnism:String,wMoterPumpSet:String,wOperationalHrs:String,wQWReqiurd:String,wQuality:String,wTable:String,wTank:String,wAvlbility:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = WaterDetailsRepo.updateWaterDetailsApi(surveyId,wDrinking,wStorage,wPumpingMechnism,wMoterPumpSet,wOperationalHrs,wQWReqiurd,wQuality,wTable,wTank,wAvlbility)
        return servicesLiveData
    }

    fun getWaterDetails(surveyId:Int) : LiveData<GetWaterSourceResponse>? {
        waterResoponse = WaterDetailsRepo.getWaterDetailsApi(surveyId)
        loading=WaterDetailsRepo.loading
        return waterResoponse
    }


}