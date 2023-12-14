package com.ncdc.nise.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.data.model.GetWaterSourceResponse
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.health.HealthDetailsModel
import com.ncdc.nise.repository.HealthDetailsRepo
import com.ncdc.nise.repository.WaterDetailsRepo


class HealthDetailsViewModel :ViewModel() {

    var servicesLiveData: MutableLiveData<SurveyorResponse>? = null
    var getHealthResponse:MutableLiveData<HealthDetailsModel>?=null
    var loading:MutableLiveData<Boolean>?=null

    fun addHealthDetails(surveyorId:Int,hfName:String, hfCategory:String,hfHWC :String,hfDistrictName:String,hfStateName:String,hfMobNetwork:String,hfDistrictCate:String,
                         hfLocationDtls:String,hfLattitudeSite:String,hfLongituteSite:String, hfCountry:String,hfPincode:String,hfNameCntctPerson:String,hfEmailCntctPerson:String,
                         hfMobCntctPerson:String,hfWorkingHrs:String,hfStartTime:String,hfEndTime:String,hfExtndWorkingHrs:String,hfLfcltyApproSyt:String,hfDistMainRod:String,hfApprochSyt:String,hfElctBrdName:String,
                         hfCnsmrId:String,hfCnsmrName:String,hfTotalNoStff:String,hfNoBeds:String,hfNoQrtrs:String,hfDistQrtrs:String,hfAmbulance:String,hfDrnkngWatr:String,
                         hfAgeBulding:String,hfMzrRenovation:String,hfNoFloors:String,hfNoBulding:String,hfAtLocation:String,hfCorcialPlaceDist:String,hfStateId:Int,hfStatus:Int)
    : LiveData<SurveyorResponse>?
    {
        servicesLiveData = HealthDetailsRepo.getHealthDetailsApi(surveyorId,hfName,hfCategory,hfHWC,hfDistrictName,hfStateName, hfMobNetwork,hfDistrictCate,hfLocationDtls,hfLattitudeSite,hfLongituteSite,
            hfCountry,hfPincode,hfNameCntctPerson,hfEmailCntctPerson,hfMobCntctPerson,hfWorkingHrs,hfStartTime,hfEndTime,hfExtndWorkingHrs,hfLfcltyApproSyt,hfDistMainRod,hfApprochSyt,hfElctBrdName,
            hfCnsmrId,hfCnsmrName,hfTotalNoStff,hfNoBeds,hfNoQrtrs,hfDistQrtrs,hfAmbulance,hfDrnkngWatr,hfAgeBulding,hfMzrRenovation,hfNoFloors,hfNoBulding,hfAtLocation,hfCorcialPlaceDist,hfStateId,hfStatus)
        return servicesLiveData
    }

    fun updateHealthDetails(surveyId:Int,surveyorId:Int,hfName:String, hfCategory:String,hfHWC :String,hfDistrictName:String,hfStateName:String,hfMobNetwork:String,hfDistrictCate:String,
                         hfLocationDtls:String,hfLattitudeSite:String,hfLongituteSite:String, hfCountry:String,hfPincode:String,hfNameCntctPerson:String,hfEmailCntctPerson:String,
                         hfMobCntctPerson:String,hfWorkingHrs:String,hfStartTime:String,hfEndTime:String,hfExtndWorkingHrs:String,hfLfcltyApproSyt:String,hfDistMainRod:String,hfApprochSyt:String,hfElctBrdName:String,
                         hfCnsmrId:String,hfCnsmrName:String,hfTotalNoStff:String,hfNoBeds:String,hfNoQrtrs:String,hfDistQrtrs:String,hfAmbulance:String,hfDrnkngWatr:String,
                         hfAgeBulding:String,hfMzrRenovation:String,hfNoFloors:String,hfNoBulding:String,hfAtLocation:String,hfCorcialPlaceDist:String,hfStateId:Int,hfStatus:Int)
            : LiveData<SurveyorResponse>?
    {
        servicesLiveData = HealthDetailsRepo.updateHealthDetailsApi(surveyId,surveyorId,hfName,hfCategory,hfHWC,hfDistrictName,hfStateName, hfMobNetwork,hfDistrictCate,hfLocationDtls,hfLattitudeSite,hfLongituteSite,
            hfCountry,hfPincode,hfNameCntctPerson,hfEmailCntctPerson,hfMobCntctPerson,hfWorkingHrs,hfStartTime,hfEndTime,hfExtndWorkingHrs,hfLfcltyApproSyt,hfDistMainRod,hfApprochSyt,hfElctBrdName,
            hfCnsmrId,hfCnsmrName,hfTotalNoStff,hfNoBeds,hfNoQrtrs,hfDistQrtrs,hfAmbulance,hfDrnkngWatr,hfAgeBulding,hfMzrRenovation,hfNoFloors,hfNoBulding,hfAtLocation,hfCorcialPlaceDist,hfStateId,hfStatus)
        return servicesLiveData
    }

    fun getHealthDetails(surveyId:Int) : LiveData<HealthDetailsModel>? {
        getHealthResponse = HealthDetailsRepo.getHealthDetailsApi(surveyId)
        loading=HealthDetailsRepo.loading
        return getHealthResponse
    }
}