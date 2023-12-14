package com.ncdc.nise.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.data.model.GetWaterSourceResponse
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.healthrelatedinfo.GetHealthInfoResponse

import com.ncdc.nise.repository.HealthRelatedRepo
import com.ncdc.nise.repository.WaterDetailsRepo

class HealthInfoViewModel:ViewModel() {

    var servicesLiveData: MutableLiveData<SurveyorResponse>? = null
    var healthRelatedResponse:MutableLiveData<GetHealthInfoResponse>?=null
    var loading:MutableLiveData<Boolean>?=null

    fun addHealthRelatedApi(
        surveyorId: Int,
        hrTtlPapulation: String,
        hrAnnualTarget: String,
        hrPrgnentWomanTarget:String,
        hrAverageNoOpd: String,
        hrTtlIpd: String,
        hrAverageSurgeries: String,
        hrAvrNoDeliveries: String,
        hrEmServices: String,
        hrOpdServices: String,
        hrAntiRabiesVaccine: String,
        hrBabiesManaged: String,
        hrImmunization: String,
        hrBloodExamination: String,
        hrTeleMedicine: String,
        hrBloodStorage: String,
        hrColdChain: String,
        hrNoofIpds: String,

    )
            : LiveData<SurveyorResponse>?
    {
        servicesLiveData = HealthRelatedRepo.addHealthRelatedInfoApi(surveyorId,hrTtlPapulation,hrAnnualTarget,hrPrgnentWomanTarget,hrAverageNoOpd,hrTtlIpd,hrAverageSurgeries, hrAvrNoDeliveries,hrEmServices,hrOpdServices,hrAntiRabiesVaccine,hrBabiesManaged,
            hrImmunization,hrBloodExamination,hrTeleMedicine,hrBloodStorage,hrColdChain,hrNoofIpds)
        return servicesLiveData
    }
    fun updateHealthRelatedApi(
        surveyorId: Int,
        hrTtlPapulation: String,
        hrAnnualTarget: String,
        hrPrgnentWomanTarget:String,
        hrAverageNoOpd: String,
        hrTtlIpd: String,
        hrAverageSurgeries: String,
        hrAvrNoDeliveries: String,
        hrEmServices: String,
        hrOpdServices: String,
        hrAntiRabiesVaccine: String,
        hrBabiesManaged: String,
        hrImmunization: String,
        hrBloodExamination: String,
        hrTeleMedicine: String,
        hrBloodStorage: String,
        hrColdChain: String,
        hrNoofIpds: String,

        )
            : LiveData<SurveyorResponse>?
    {
        servicesLiveData = HealthRelatedRepo.updateHealthRelatedInfoApi(surveyorId,hrTtlPapulation,hrAnnualTarget,hrPrgnentWomanTarget,hrAverageNoOpd,hrTtlIpd,hrAverageSurgeries, hrAvrNoDeliveries,hrEmServices,hrOpdServices,hrAntiRabiesVaccine,hrBabiesManaged,
            hrImmunization,hrBloodExamination,hrTeleMedicine,hrBloodStorage,hrColdChain,hrNoofIpds)
        return servicesLiveData
    }
    fun getHealthRelatedApi(surveyId:Int) : LiveData<GetHealthInfoResponse>? {
        healthRelatedResponse = HealthRelatedRepo.getHealthInfoApi(surveyId)
        loading=HealthRelatedRepo.loading
        return healthRelatedResponse
    }

}