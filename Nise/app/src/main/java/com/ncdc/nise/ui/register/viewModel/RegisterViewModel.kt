package com.ncdc.nise.ui.register.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.ui.register.model.AuthResponse
import com.ncdc.nise.ui.register.repository.RegisterRepo


class RegisterViewModel :ViewModel() {
    var servicesLiveData: MutableLiveData<AuthResponse>? = null
    var registerResponse:MutableLiveData<String>?=null
    var loading:MutableLiveData<Boolean>?=null

    fun addSurveyorDetails(sName:String, sEmail:String,sDesignation :String,sOrganistion:String,sContactNo:String,sPassword:String,sActive:Int) : LiveData<AuthResponse>? {
        servicesLiveData = RegisterRepo.getRegisterApi(sName,sEmail,sDesignation,sOrganistion,sContactNo, sPassword,sActive)
        loading=RegisterRepo.loading
        registerResponse=RegisterRepo.statusResponse
        return servicesLiveData
    }
}