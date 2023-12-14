package com.ncdc.nise.ui.connected.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.energyAssments.GetEnergyAssments
import com.ncdc.nise.data.model.healthrelatedinfo.GetHealthInfoResponse
import com.ncdc.nise.data.model.inputsAssments.GetInputAssments
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.repository.HealthRelatedRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EnergyAssmentRepo {
    val surveyorResponse = MutableLiveData<SurveyorResponse>()
    val energeyAssResponse=MutableLiveData<GetEnergyAssments>()
    val inputsAssResponse=MutableLiveData<GetInputAssments>()
    var loading=MutableLiveData<Boolean>()
    var status:Boolean=false
    var msg:String=""
    var id:String=""

    fun addEnergyAssApi(surveyId:Int,enFuelsUsed:String,enTtlBuiltUpArea :String,enTtlNoBuilding:String,enTtlNoFloors:String,enTtlNoWimdows:String,enTtlInstlCapcity:String,enHotWtrConsumption:String,enThermostatic:String,enFanSpeed:String,enIncandescent:String,
                            enEquipments:String,enRooms:String,enSensors:String,enLabelling:String,enRefrigerators:String,enInsulatedWell:String,enLightingLoad:String,enEnergyUsage:String,enEnergyEfficiency:String,enLightingCircuits:String,enHumidity:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.addEnergyAssment(surveyId,enFuelsUsed,enTtlBuiltUpArea,enTtlNoBuilding,enTtlNoFloors,enTtlNoWimdows,enTtlInstlCapcity,enHotWtrConsumption,enThermostatic,enFanSpeed,enIncandescent,
            enEquipments,enRooms,enSensors,enLabelling,enRefrigerators,enInsulatedWell,enLightingLoad,enEnergyUsage,enEnergyEfficiency,enLightingCircuits,enHumidity)

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
                     status=data!!.status
                    if(status){
                         msg = data.message
                         id = data.surveyorId

                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }else{
                         msg = data.message
                         id=""
                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }
                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Response Server Error","")
                }



            }
        })

        return surveyorResponse
    }
    fun updateEnergyAssApi(surveyId:Int,enFuelsUsed:String,enTtlBuiltUpArea :String,enTtlNoBuilding:String,enTtlNoFloors:String,enTtlNoWimdows:String,enTtlInstlCapcity:String,enHotWtrConsumption:String,enThermostatic:String,enFanSpeed:String,enIncandescent:String,
                        enEquipments:String,enRooms:String,enSensors:String,enLabelling:String,enRefrigerators:String,enInsulatedWell:String,enLightingLoad:String,enEnergyUsage:String,enEnergyEfficiency:String,enLightingCircuits:String,enHumidity:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.updateEnergyAssment(surveyId,enFuelsUsed,enTtlBuiltUpArea,enTtlNoBuilding,enTtlNoFloors,enTtlNoWimdows,enTtlInstlCapcity,enHotWtrConsumption,enThermostatic,enFanSpeed,enIncandescent,
            enEquipments,enRooms,enSensors,enLabelling,enRefrigerators,enInsulatedWell,enLightingLoad,enEnergyUsage,enEnergyEfficiency,enLightingCircuits,enHumidity)

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
                    status=data!!.status
                    if(status){
                        msg = data.message
                        id = data.surveyorId

                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }else{
                        msg = data.message
                        id=""
                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }
                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Response Server Error","")
                }



            }
        })

        return surveyorResponse
    }



    fun addInputAssApi(surveyId:Int,InRefrigeratorsRating:String,InAirconditionersRating :String,InWaterPump:String,InElectricalLoads:String,InHcf:String,InConservation:String,InMequipment:String,InLowEnergy:String,InLightingControl:String,InEnergyEfficient:String,InStrategies:String,InCarbonFeatures:String,InAditionalRemarks:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.addInputAssment(surveyId,InRefrigeratorsRating,InAirconditionersRating,InWaterPump,InElectricalLoads,InHcf,InConservation,InMequipment,InLowEnergy,InLightingControl,InEnergyEfficient,InStrategies,InCarbonFeatures,InAditionalRemarks)

        call.enqueue(object: Callback<SurveyorResponse> {
            override fun onFailure(call: Call<SurveyorResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<SurveyorResponse>,
                response: Response<SurveyorResponse>
            ) {

                Log.v("DEBUG : ", response.body().toString())

                if(response.isSuccessful){
                    val data = response.body()
                    status=data!!.status
                    if(status){
                        msg = data.message
                        id = data.surveyorId

                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }else{
                        msg = data.message
                        id=""
                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }
                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Response Server Error","")
                }


            }
        })

        return surveyorResponse
    }

    fun updateInputAssApi(surveyId:Int,InRefrigeratorsRating:String,InAirconditionersRating :String,InWaterPump:String,InElectricalLoads:String,InHcf:String,InConservation:String,InMequipment:String,InLowEnergy:String,InLightingControl:String,InEnergyEfficient:String,InStrategies:String,InCarbonFeatures:String,InAditionalRemarks:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.updateInputAssment(surveyId,InRefrigeratorsRating,InAirconditionersRating,InWaterPump,InElectricalLoads,InHcf,InConservation,InMequipment,InLowEnergy,InLightingControl,InEnergyEfficient,InStrategies,InCarbonFeatures,InAditionalRemarks)

        call.enqueue(object: Callback<SurveyorResponse> {
            override fun onFailure(call: Call<SurveyorResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<SurveyorResponse>,
                response: Response<SurveyorResponse>
            ) {

                Log.v("DEBUG : ", response.body().toString())

                if(response.isSuccessful){
                    val data = response.body()
                    status=data!!.status
                    if(status){
                        msg = data.message
                        id = data.surveyorId

                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }else{
                        msg = data.message
                        id=""
                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }
                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Response Server Error","")
                }


            }
        })

        return surveyorResponse
    }
    fun getEnergyAssApi(surveyId:Int): MutableLiveData<GetEnergyAssments> {
        loading.value=false
        val call = RetrofitClient.apiInterface.getEnergyAssmentApi(surveyId)

        call.enqueue(object: Callback<GetEnergyAssments> {
            override fun onFailure(call: Call<GetEnergyAssments>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<GetEnergyAssments>,
                response: Response<GetEnergyAssments>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    status=data!!.status
                    msg = data.message
                    if(status){
                        loading.value=true
                        val list=data.data
                        energeyAssResponse.value = GetEnergyAssments(status,msg,list)
                    }else{
                        loading.value=true
                    }
                }else{
                    loading.value=true
                }

            }
        })

        return energeyAssResponse
    }
    fun getInputAssApi(surveyId:Int): MutableLiveData<GetInputAssments> {
        loading.value=false
        val call = RetrofitClient.apiInterface.getInputAssment(surveyId)

        call.enqueue(object: Callback<GetInputAssments> {
            override fun onFailure(call: Call<GetInputAssments>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<GetInputAssments>,
                response: Response<GetInputAssments>
            ) {
               if(response.isSuccessful){
                   val data = response.body()
                   status=data!!.status
                   msg = data.message
                   if(status){
                       loading.value=true
                       val list=data.data
                       inputsAssResponse.value = GetInputAssments(status,msg,list)
                   }else{
                       loading.value=true
                   }

               }else{
                   loading.value=true
               }
            }
        })

        return inputsAssResponse
    }
}