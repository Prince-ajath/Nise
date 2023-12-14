package com.ncdc.nise.ui.load.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.ui.load.model.load.LoadItemResponse

import com.ncdc.nise.ui.load.repo.ConnectedLoadRepo

class CLoadViewModel : ViewModel() {

    var servicesLiveData: MutableLiveData<SurveyorResponse>? = null
    var loadResponseData:MutableLiveData<LoadItemResponse>?=null
    var loading:MutableLiveData<Boolean>?=null


    fun addConnectedLoadApi(surveyId:Int,clCategory:String,clName :String,clWattage:String,clQuantity:String,clOperationalDay:String,clOperationalNight:String,clCriticalLoad:String,clLoadType:String,clEnergyRating:String,clYearInstallation:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = ConnectedLoadRepo.getConnectedLoadApi(surveyId,clCategory,clName,clWattage,clQuantity,clOperationalDay,clOperationalNight,clCriticalLoad,clLoadType,clEnergyRating,clYearInstallation)
        return servicesLiveData
    }

    fun addLoadApi(surveyId:Int): LiveData<LoadItemResponse>? {
        loadResponseData = ConnectedLoadRepo.getLoadDataApi(surveyId)
        loading=ConnectedLoadRepo.loading
        return loadResponseData
    }

}