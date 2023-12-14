package com.ncdc.nise.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.repository.SurveyorDetailsRepository




class SurveyorViewModel : ViewModel() {


    var servicesLiveData: MutableLiveData<SurveyorResponse>? = null

    fun getUser(sName:String,sDesignation :String,sOrganistion:String,sDate:String,sTime:String,sContactNo:String,sOtherInfo:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = SurveyorDetailsRepository.getServicesApiCall(sName,sDesignation,sOrganistion,sDate,sTime,sContactNo,sOtherInfo)
        return servicesLiveData
    }



}