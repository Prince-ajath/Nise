package com.ncdc.nise.ui.connected.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.energyAssments.GetEnergyAssments
import com.ncdc.nise.data.model.inputsAssments.GetInputAssments
import com.ncdc.nise.ui.connected.repository.EnergyAssmentRepo

class InputAssmentViewModel:ViewModel() {
    var servicesLiveData: MutableLiveData<SurveyorResponse>? = null
    var  inputAssResponse:MutableLiveData<GetInputAssments>?=null
    var loading:MutableLiveData<Boolean>?=null

    fun addInputAssmentApi(surveyId:Int,InRefrigeratorsRating:String,InAirconditionersRating :String,InWaterPump:String,InElectricalLoads:String,InHcf:String,InConservation:String,InMequipment:String,InLowEnergy:String,InLightingControl:String,InEnergyEfficient:String,InStrategies:String,InCarbonFeatures:String,InAditionalRemarks:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = EnergyAssmentRepo.addInputAssApi(surveyId,InRefrigeratorsRating,InAirconditionersRating,InWaterPump,InElectricalLoads,InHcf,InConservation,InMequipment,InLowEnergy,InLightingControl,InEnergyEfficient,InStrategies,InCarbonFeatures,InAditionalRemarks)
        return servicesLiveData
    }
    fun updateInputAssmentApi(surveyId:Int,InRefrigeratorsRating:String,InAirconditionersRating :String,InWaterPump:String,InElectricalLoads:String,InHcf:String,InConservation:String,InMequipment:String,InLowEnergy:String,InLightingControl:String,InEnergyEfficient:String,InStrategies:String,InCarbonFeatures:String,InAditionalRemarks:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = EnergyAssmentRepo.updateInputAssApi(surveyId,InRefrigeratorsRating,InAirconditionersRating,InWaterPump,InElectricalLoads,InHcf,InConservation,InMequipment,InLowEnergy,InLightingControl,InEnergyEfficient,InStrategies,InCarbonFeatures,InAditionalRemarks)
        return servicesLiveData
    }
    fun getInputAssApi(surveyId:Int) : LiveData<GetInputAssments>? {
        inputAssResponse = EnergyAssmentRepo.getInputAssApi(surveyId)
        loading=EnergyAssmentRepo.loading
        return inputAssResponse
    }

}