package com.ncdc.nise.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.vaccineStorage.GetVaccineStorageResponse
import com.ncdc.nise.repository.HealthRelatedRepo
import com.ncdc.nise.repository.VaccineStorageRepo


class VaccineStorageViewModel:ViewModel() {
    var servicesLiveData: MutableLiveData<SurveyorResponse>? = null
    var vaccineStorageResponse:MutableLiveData<GetVaccineStorageResponse>?=null
    var loading:MutableLiveData<Boolean>?=null

    fun addVaccineStorage(
        surveyId: Int,
        vRefrigrotrAvl: String,
        vRefrigrotrWorking: String,
        vSolorPwrd: String,
        vTempMentained: String,
        vUsagePattern: String,
    ) : LiveData<SurveyorResponse>? {
        servicesLiveData = VaccineStorageRepo.addVaccineStorageApi(surveyId,vRefrigrotrAvl,vRefrigrotrWorking,vSolorPwrd,vTempMentained,vUsagePattern)
        return servicesLiveData
    }

    fun updateVaccineStorage(
        surveyId: Int,
        vRefrigrotrAvl: String,
        vRefrigrotrWorking: String,
        vSolorPwrd: String,
        vTempMentained: String,
        vUsagePattern: String,
    ) : LiveData<SurveyorResponse>? {
        servicesLiveData = VaccineStorageRepo.updateVaccineStorageApi(surveyId,vRefrigrotrAvl,vRefrigrotrWorking,vSolorPwrd,vTempMentained,vUsagePattern)
        return servicesLiveData
    }
    fun getVaccineStorageApi(surveyId: Int):LiveData<GetVaccineStorageResponse>?{
        vaccineStorageResponse=VaccineStorageRepo.getVaccineStorageApi(surveyId)
        loading=VaccineStorageRepo.loading
        return  vaccineStorageResponse
    }

}