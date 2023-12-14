package com.ncdc.nise.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.electrical.getElectricalResponse
import com.ncdc.nise.data.model.spaceavilability.GetSpaceResponse
import com.ncdc.nise.repository.ElectricalDataRepo
import com.ncdc.nise.repository.HealthRelatedRepo
import com.ncdc.nise.repository.SpaceAvilabRepo

class SpaceViewModel:ViewModel() {
    var servicesLiveData: MutableLiveData<SurveyorResponse>? = null
    var getSpaceResponse:MutableLiveData<GetSpaceResponse>?=null
    var loading:MutableLiveData<Boolean>?=null

    fun addSpaceAvlData(surveyId:Int,spFreeAreaRoof:String,spOpenGround :String,spHost:String,spScurStorage:String,spAvlblErthng:String,spInstallation:String,spCpu:String,spSlrPVSystm:String,
                          spTheftSite:String,spInstltionLoc:String,spTypeRoof:String,spOrientation:String,spTiltAngle:String,spTotlArea:String,spTotalShadow:String,spObstructions:String,
                          spGnrlShape:String,spAccessRoof:String,spWeightRestricton:String,spObstruction:String,spConstruction:String,spEcondition:String,spRoofConditons:String,
                          spSupportConditons:String,spRoofMtrial:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = SpaceAvilabRepo.getSpaceAvlApi(surveyId,spFreeAreaRoof,spOpenGround,spHost,spScurStorage,spAvlblErthng,spInstallation,spCpu,spSlrPVSystm,spTheftSite,spInstltionLoc,spTypeRoof,spOrientation,spTiltAngle,
            spTotlArea,spTotalShadow,spObstructions,spGnrlShape,spAccessRoof,spWeightRestricton,spObstruction,spConstruction,spEcondition,spRoofConditons,spSupportConditons,spRoofMtrial)
        return servicesLiveData
    }
    fun updateSpaceAvlData(surveyId:Int,spFreeAreaRoof:String,spOpenGround :String,spHost:String,spScurStorage:String,spAvlblErthng:String,spInstallation:String,spCpu:String,spSlrPVSystm:String,
                        spTheftSite:String,spInstltionLoc:String,spTypeRoof:String,spOrientation:String,spTiltAngle:String,spTotlArea:String,spTotalShadow:String,spObstructions:String,
                        spGnrlShape:String,spAccessRoof:String,spWeightRestricton:String,spObstruction:String,spConstruction:String,spEcondition:String,spRoofConditons:String,
                        spSupportConditons:String,spRoofMtrial:String) : LiveData<SurveyorResponse>? {
        servicesLiveData = SpaceAvilabRepo.updateSpaceAvlApi(surveyId,spFreeAreaRoof,spOpenGround,spHost,spScurStorage,spAvlblErthng,spInstallation,spCpu,spSlrPVSystm,spTheftSite,spInstltionLoc,spTypeRoof,spOrientation,spTiltAngle,
            spTotlArea,spTotalShadow,spObstructions,spGnrlShape,spAccessRoof,spWeightRestricton,spObstruction,spConstruction,spEcondition,spRoofConditons,spSupportConditons,spRoofMtrial)
        return servicesLiveData
    }

    fun getSpaceAvilableApi(surveyId:Int) : LiveData<GetSpaceResponse>? {
        getSpaceResponse = SpaceAvilabRepo.getSpaceAvilableApi(surveyId)
        loading=SpaceAvilabRepo.loading
        return getSpaceResponse
    }

}