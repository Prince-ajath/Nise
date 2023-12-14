package com.ncdc.nise.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.electrical.getElectricalResponse
import com.ncdc.nise.data.model.health.HealthDetailsModel
import com.ncdc.nise.repository.ElectricalDataRepo
import com.ncdc.nise.repository.HealthDetailsRepo
import com.ncdc.nise.repository.HealthRelatedRepo


class ElectricalViewModel:ViewModel() {

    var servicesLiveData: MutableLiveData<SurveyorResponse>? = null
    var electricalResponse:MutableLiveData<getElectricalResponse>?=null
    var loading:MutableLiveData<Boolean>?=null

    fun addElectricalData(surveyId:Int,epAccess:String,epConnection :String,epBill:String,epPeakLoadBill:String,epConnctionType:String,epAverage:String,epAvlMainLine:String,epVoltage:String,
                          epAvgPower:String,epPSSupply:String,epSSSupply:String,epMtrConnAvlbl:String,epAcVltag:String,epExpnLoads:String,epOutdrsLightng:String,
                          epSolrSystm:String,epConsumption:String,epPrvsionsExistng:String,epEnrgyEffcncy:String,epPwrFactr:String,epDistbtionBord:String,epNoMcbFive:String,
                          epNoMCBUptoTen:String,epNoMCBUpToTwnty:String,epWiringConditns:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = ElectricalDataRepo.getElectricalDataApi(surveyId,epAccess,epConnection,epBill,epPeakLoadBill,epConnctionType,epAverage,epAvlMainLine,epVoltage,epAvgPower,epPSSupply,epSSSupply,epMtrConnAvlbl,
            epAcVltag,epExpnLoads,epOutdrsLightng,epSolrSystm,epConsumption,epPrvsionsExistng,epEnrgyEffcncy,epPwrFactr,epDistbtionBord,epNoMcbFive,epNoMCBUptoTen,epNoMCBUpToTwnty,epWiringConditns)
        return servicesLiveData
    }

    fun updateElectricalData(surveyId:Int,epAccess:String,epConnection :String,epBill:String,epPeakLoadBill:String,epConnctionType:String,epAverage:String,epAvlMainLine:String,epVoltage:String,
                          epAvgPower:String,epPSSupply:String,epSSSupply:String,epMtrConnAvlbl:String,epAcVltag:String,epExpnLoads:String,epOutdrsLightng:String,
                          epSolrSystm:String,epConsumption:String,epPrvsionsExistng:String,epEnrgyEffcncy:String,epPwrFactr:String,epDistbtionBord:String,epNoMcbFive:String,
                          epNoMCBUptoTen:String,epNoMCBUpToTwnty:String,epWiringConditns:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = ElectricalDataRepo.updateElectricalDataApi(surveyId,epAccess,epConnection,epBill,epPeakLoadBill,epConnctionType,epAverage,epAvlMainLine,epVoltage,epAvgPower,epPSSupply,epSSSupply,epMtrConnAvlbl,
            epAcVltag,epExpnLoads,epOutdrsLightng,epSolrSystm,epConsumption,epPrvsionsExistng,epEnrgyEffcncy,epPwrFactr,epDistbtionBord,epNoMcbFive,epNoMCBUptoTen,epNoMCBUpToTwnty,epWiringConditns)
        return servicesLiveData
    }
    fun getElectricalDetails(surveyId:Int) : LiveData<getElectricalResponse>? {
        electricalResponse = ElectricalDataRepo.getElectricalApi(surveyId)
        loading= ElectricalDataRepo.loading
        return electricalResponse
    }


}