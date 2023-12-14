package com.ncdc.nise.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData

import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.healthrelatedinfo.GetHealthInfoResponse
import com.ncdc.nise.data.model.healthrelatedinfo.HealthRelatedData
import com.ncdc.nise.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object HealthRelatedRepo {

    val surveyorResponse = MutableLiveData<SurveyorResponse>()
    val healthRelatedResponse=MutableLiveData<GetHealthInfoResponse>()
    var loading=MutableLiveData<Boolean>()
    var status:Boolean=false
    var msg:String=""
    var id:String=""





    fun addHealthRelatedInfoApi(surveyorId:Int, hrTtlPapulation:String, hrAnnualTarget:String,hrPrgnentWomanTarget:String,hrAverageNoOpd :String,hrTtlIpd:String,hrAverageSurgeries:String,hrAvrNoDeliveries:String,hrEmServices:String,
                                hrOpdServices:String,hrAntiRabiesVaccine:String,hrBabiesManaged:String, hrImmunization:String,hrBloodExamination:String,hrTeleMedicine:String,hrBloodStorage:String,
                                hrColdChain:String,hrNoofIpds:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.addHealthRelatedInfo(surveyorId,hrTtlPapulation,hrAnnualTarget,hrPrgnentWomanTarget,hrAverageNoOpd,hrTtlIpd,hrAverageSurgeries, hrAvrNoDeliveries,hrEmServices,hrOpdServices,hrAntiRabiesVaccine,hrBabiesManaged,
            hrImmunization,hrBloodExamination,hrTeleMedicine,hrBloodStorage,hrColdChain,hrNoofIpds)

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
                  msg = data.message
                  id = data.surveyorId
                  surveyorResponse.value = SurveyorResponse(status,msg,id)
              }else{
                  surveyorResponse.value = SurveyorResponse(false,"Response Server Error","")
              }
            }
        })

        return surveyorResponse
    }
    fun updateHealthRelatedInfoApi(surveyorId:Int, hrTtlPapulation:String, hrAnnualTarget:String,hrPrgnentWomanTarget:String,hrAverageNoOpd :String,hrTtlIpd:String,hrAverageSurgeries:String,hrAvrNoDeliveries:String,hrEmServices:String,
                                hrOpdServices:String,hrAntiRabiesVaccine:String,hrBabiesManaged:String, hrImmunization:String,hrBloodExamination:String,hrTeleMedicine:String,hrBloodStorage:String,
                                hrColdChain:String,hrNoofIpds:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.updateHealthRelatedInfo(surveyorId,hrTtlPapulation,hrAnnualTarget,hrPrgnentWomanTarget,hrAverageNoOpd,hrTtlIpd,hrAverageSurgeries, hrAvrNoDeliveries,hrEmServices,hrOpdServices,hrAntiRabiesVaccine,hrBabiesManaged,
            hrImmunization,hrBloodExamination,hrTeleMedicine,hrBloodStorage,hrColdChain,hrNoofIpds)

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
                    msg = data.message
                    if(status){
                        id = data.surveyorId
                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }else{
                        surveyorResponse.value = SurveyorResponse(status,msg,"")
                    }

                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Response Server Error","")
                }
            }
        })

        return surveyorResponse
    }
    fun getHealthInfoApi(surveyId:Int): MutableLiveData<GetHealthInfoResponse> {
       loading.value=false
        val call = RetrofitClient.apiInterface.getHealthRelatedInfo(surveyId)

        call.enqueue(object: Callback<GetHealthInfoResponse> {
            override fun onFailure(call: Call<GetHealthInfoResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<GetHealthInfoResponse>,
                response: Response<GetHealthInfoResponse>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    status=data!!.status
                    msg = data.message
                    if(status){
                        loading.value=true
                       val healthRelatedData=data.data
                        healthRelatedResponse.value = GetHealthInfoResponse(status,msg, healthRelatedData)
                    }else{
                        loading.value=true
                    }
                }else{
                    loading.value=true
                }

            }
        })

        return healthRelatedResponse
    }



}