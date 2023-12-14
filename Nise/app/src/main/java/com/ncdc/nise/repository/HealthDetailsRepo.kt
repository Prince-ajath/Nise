package com.ncdc.nise.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ncdc.nise.data.model.GetWaterSourceResponse
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.health.HealthDetailsModel
import com.ncdc.nise.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object HealthDetailsRepo {

    val surveyorResponse = MutableLiveData<SurveyorResponse>()
    val getHealthResponse=MutableLiveData<HealthDetailsModel>()
    var loading=MutableLiveData<Boolean>()
    var status:Boolean=false
    var msg:String=""
    var id:String=""

    fun getHealthDetailsApi(surveyorId:Int,
                            hfName:String,
                            hfCategory:String,
                            hfHWC :String,
                            hfDistrictName:String,
                            hfStateName:String,
                            hfMobNetwork:String,
                            hfDistrictCate:String,
                            hfLocationDtls:String,
                            hfLattitudeSite:String,
                            hfLongituteSite:String,
                            hfCountry:String,
                            hfPincode:String,
                            hfNameCntctPerson:String,
                            hfEmailCntctPerson:String,
                            hfMobCntctPerson:String,
                            hfWorkingHrs:String,
                            hfStartTime:String,
                            hfEndTime:String,
                            hfExtndWorkingHrs:String,
                            hfLfcltyApproSyt:String,
                            hfDistMainRod:String,
                            hfApprochSyt:String,
                            hfElctBrdName:String,
                            hfCnsmrId:String,
                            hfCnsmrName:String,
                            hfTotalNoStff:String,
                            hfNoBeds:String,
                            hfNoQrtrs:String,
                            hfDistQrtrs:String,
                            hfAmbulance:String,
                            hfDrnkngWatr:String,
                            hfAgeBulding:String,
                            hfMzrRenovation:String,
                            hfNoFloors:String,
                            hfNoBulding:String,
                            hfAtLocation:String,
                            hfCorcialPlaceDist:String,
                            hfStateId:Int,hfStatus:Int):MutableLiveData<SurveyorResponse> {

            val call = RetrofitClient.apiInterface.addHealthDetailsApi(surveyorId,hfName,hfCategory,hfHWC,hfDistrictName,hfStateName, hfMobNetwork,hfDistrictCate,hfLocationDtls,hfLattitudeSite,hfLongituteSite,
            hfCountry,hfPincode,hfNameCntctPerson,hfEmailCntctPerson,hfMobCntctPerson,hfWorkingHrs,hfStartTime,hfEndTime,hfExtndWorkingHrs,hfLfcltyApproSyt,hfDistMainRod,hfApprochSyt,hfElctBrdName,
            hfCnsmrId,hfCnsmrName,hfTotalNoStff,hfNoBeds,hfNoQrtrs,hfDistQrtrs,hfAmbulance,hfDrnkngWatr,hfAgeBulding,hfMzrRenovation,hfNoFloors,hfNoBulding,hfAtLocation,hfCorcialPlaceDist,hfStateId,hfStatus)

        call.enqueue(object: Callback<SurveyorResponse> {
            override fun onFailure(call: Call<SurveyorResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<SurveyorResponse>,
                response: Response<SurveyorResponse>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    status =data!!.status
                    if(status){
                        msg = data.message
                        id = data.surveyorId
                        surveyorResponse.value = SurveyorResponse(status, msg, id)
                    }else{
                         msg = data.message
                         id =""
                        surveyorResponse.value = SurveyorResponse(status,msg, id)
                    }
                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Respnse Server Erroe","")
                }




            }
        })

        return surveyorResponse
    }

    fun updateHealthDetailsApi(surveyId:Int,surveyorId:Int,
                            hfName:String,
                            hfCategory:String,
                            hfHWC :String,
                            hfDistrictName:String,
                            hfStateName:String,
                            hfMobNetwork:String,
                            hfDistrictCate:String,
                            hfLocationDtls:String,
                            hfLattitudeSite:String,
                            hfLongituteSite:String,
                            hfCountry:String,
                            hfPincode:String,
                            hfNameCntctPerson:String,
                            hfEmailCntctPerson:String,
                            hfMobCntctPerson:String,
                            hfWorkingHrs:String,
                               hfStartTime:String,
                               hfEndTime:String,
                            hfExtndWorkingHrs:String,
                            hfLfcltyApproSyt:String,
                            hfDistMainRod:String,
                            hfApprochSyt:String,
                            hfElctBrdName:String,
                            hfCnsmrId:String,
                            hfCnsmrName:String,
                            hfTotalNoStff:String,
                            hfNoBeds:String,
                            hfNoQrtrs:String,
                            hfDistQrtrs:String,
                            hfAmbulance:String,
                            hfDrnkngWatr:String,
                            hfAgeBulding:String,
                            hfMzrRenovation:String,
                            hfNoFloors:String,
                            hfNoBulding:String,
                            hfAtLocation:String,
                            hfCorcialPlaceDist:String,
                               hfStateId:Int,hfStatus:Int):MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.updateHealthDetailsApi(surveyId,surveyorId,hfName,hfCategory,hfHWC,hfDistrictName,hfStateName, hfMobNetwork,hfDistrictCate,hfLocationDtls,hfLattitudeSite,hfLongituteSite,
            hfCountry,hfPincode,hfNameCntctPerson,hfEmailCntctPerson,hfMobCntctPerson,hfWorkingHrs,hfStartTime,hfEndTime,hfExtndWorkingHrs,hfLfcltyApproSyt,hfDistMainRod,hfApprochSyt,hfElctBrdName,
            hfCnsmrId,hfCnsmrName,hfTotalNoStff,hfNoBeds,hfNoQrtrs,hfDistQrtrs,hfAmbulance,hfDrnkngWatr,hfAgeBulding,hfMzrRenovation,hfNoFloors,hfNoBulding,hfAtLocation,hfCorcialPlaceDist,hfStateId,hfStatus)

        call.enqueue(object: Callback<SurveyorResponse> {
            override fun onFailure(call: Call<SurveyorResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<SurveyorResponse>,
                response: Response<SurveyorResponse>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    status =data!!.status
                    if(status){
                        msg = data.message
                        id = data.surveyorId
                        surveyorResponse.value = SurveyorResponse(status, msg, id)
                    }else{
                        msg = data.message
                        id =""
                        surveyorResponse.value = SurveyorResponse(status,msg, id)
                    }
                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Respnse Server Erroe","")
                }




            }
        })

        return surveyorResponse
    }

    fun getHealthDetailsApi(surveyId:Int): MutableLiveData<HealthDetailsModel> {
         loading.value=false
        val call = RetrofitClient.apiInterface.getHealthDetails(surveyId)

        call.enqueue(object: Callback<HealthDetailsModel> {
            override fun onFailure(call: Call<HealthDetailsModel>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<HealthDetailsModel>,
                response: Response<HealthDetailsModel>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                     status=data!!.status
                     msg = data.message
                    if(status){
                        loading.value=true
                        val list=data.data
                        getHealthResponse.value = HealthDetailsModel(status,msg,list)
                    }else{
                        loading.value=true
                    }

                }else{
                    loading.value=true
                }



            }
        })

        return getHealthResponse
    }
}