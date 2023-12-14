package com.ncdc.nise.ui.login.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.ui.register.model.AuthResponse
import com.ncdc.nise.ui.register.repository.RegisterRepo

class LoginViewModel :ViewModel() {
    var servicesLiveData: MutableLiveData<AuthResponse>? = null
    var loading:MutableLiveData<Boolean>?=null

    fun loginDetails(sEmail:String,sPassword:String) : LiveData<AuthResponse>? {
        servicesLiveData = RegisterRepo.getLoginApi(sEmail,sPassword)
        loading=RegisterRepo.loading
        return servicesLiveData
    }
}