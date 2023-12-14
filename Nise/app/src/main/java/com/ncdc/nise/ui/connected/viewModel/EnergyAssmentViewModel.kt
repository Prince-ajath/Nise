package com.ncdc.nise.ui.connected.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.energyAssments.GetEnergyAssments
import com.ncdc.nise.data.model.healthrelatedinfo.GetHealthInfoResponse
import com.ncdc.nise.repository.ElectricalDataRepo
import com.ncdc.nise.repository.HealthRelatedRepo
import com.ncdc.nise.ui.connected.repository.EnergyAssmentRepo

class EnergyAssmentViewModel:ViewModel() {

    var servicesLiveData: MutableLiveData<SurveyorResponse>? = null
    var energyAssResponse:MutableLiveData<GetEnergyAssments>?=null
    var loading:MutableLiveData<Boolean>?=null

    fun addEnergyAssmentApi(surveyId:Int,enFuelsUsed:String,enTtlBuiltUpArea :String,enTtlNoBuilding:String,enTtlNoFloors:String,enTtlNoWimdows:String,enTtlInstlCapcity:String,enHotWtrConsumption:String,enThermostatic:String,enFanSpeed:String,enIncandescent:String,
                            enEquipments:String,enRooms:String,enSensors:String,enLabelling:String,enRefrigerators:String,enInsulatedWell:String,enLightingLoad:String,enEnergyUsage:String,enEnergyEfficiency:String,enLightingCircuits:String,enHumidity:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = EnergyAssmentRepo.addEnergyAssApi(surveyId,enFuelsUsed,enTtlBuiltUpArea,enTtlNoBuilding,enTtlNoFloors,enTtlNoWimdows,enTtlInstlCapcity,enHotWtrConsumption,enThermostatic,enFanSpeed,enIncandescent,
            enEquipments,enRooms,enSensors,enLabelling,enRefrigerators,enInsulatedWell,enLightingLoad,enEnergyUsage,enEnergyEfficiency,enLightingCircuits,enHumidity)
        return servicesLiveData
    }
    fun updateEnergyAssmentApi(surveyId:Int,enFuelsUsed:String,enTtlBuiltUpArea :String,enTtlNoBuilding:String,enTtlNoFloors:String,enTtlNoWimdows:String,enTtlInstlCapcity:String,enHotWtrConsumption:String,enThermostatic:String,enFanSpeed:String,enIncandescent:String,
                            enEquipments:String,enRooms:String,enSensors:String,enLabelling:String,enRefrigerators:String,enInsulatedWell:String,enLightingLoad:String,enEnergyUsage:String,enEnergyEfficiency:String,enLightingCircuits:String,enHumidity:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = EnergyAssmentRepo.updateEnergyAssApi(surveyId,enFuelsUsed,enTtlBuiltUpArea,enTtlNoBuilding,enTtlNoFloors,enTtlNoWimdows,enTtlInstlCapcity,enHotWtrConsumption,enThermostatic,enFanSpeed,enIncandescent,
            enEquipments,enRooms,enSensors,enLabelling,enRefrigerators,enInsulatedWell,enLightingLoad,enEnergyUsage,enEnergyEfficiency,enLightingCircuits,enHumidity)
        return servicesLiveData
    }
    fun getEnergyAssApi(surveyId:Int) : LiveData<GetEnergyAssments>? {
        energyAssResponse = EnergyAssmentRepo.getEnergyAssApi(surveyId)
        loading=EnergyAssmentRepo.loading
        return energyAssResponse
    }

}