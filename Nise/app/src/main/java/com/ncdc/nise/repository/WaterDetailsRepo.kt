package com.ncdc.nise.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ncdc.nise.data.model.GetWaterSourceResponse
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object WaterDetailsRepo {

    val surveyorResponse = MutableLiveData<SurveyorResponse>()
    val waterResponse=MutableLiveData<GetWaterSourceResponse>()
    var loading=MutableLiveData<Boolean>()
    var status:Boolean=false
    var msg:String=""
    var id:String=""

    fun addWaterDetailsApi(surveyId:Int,wDrinking:String,wStorage:String,wPumpingMechnism:String,wMoterPumpSet:String,wOperationalHrs:String,wQWReqiurd:String,wQuality:String,wTable:String,wTank:String,wAvlbility:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.addWaterDetails(surveyId,wDrinking,wStorage,wPumpingMechnism,wMoterPumpSet,wOperationalHrs,wQWReqiurd,wQuality,wTable,wTank,wAvlbility)

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
                        id = ""
                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }

                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Response Server Error","")
                }


            }
        })

        return surveyorResponse
    }

    fun updateWaterDetailsApi(surveyId:Int,wDrinking:String,wStorage:String,wPumpingMechnism:String,wMoterPumpSet:String,wOperationalHrs:String,wQWReqiurd:String,wQuality:String,wTable:String,wTank:String,wAvlbility:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.updateWaterSource(surveyId,wDrinking,wStorage,wPumpingMechnism,wMoterPumpSet,wOperationalHrs,wQWReqiurd,wQuality,wTable,wTank,wAvlbility)

        call.enqueue(object: Callback<SurveyorResponse> {
            override fun onFailure(call: Call<SurveyorResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<SurveyorResponse>,
                response: Response<SurveyorResponse>
            ) {

                Log.v("DEBUG : ", response.toString())

                val data = response.body()
                val status=data!!.status
                val msg = data.message
                val id = data.surveyorId

                surveyorResponse.value = SurveyorResponse(status,msg,id)
            }
        })

        return surveyorResponse
    }

    fun getWaterDetailsApi(surveyId:Int): MutableLiveData<GetWaterSourceResponse> {
       loading.value=false
        val call = RetrofitClient.apiInterface.getWaterSource(surveyId)

        call.enqueue(object: Callback<GetWaterSourceResponse> {
            override fun onFailure(call: Call<GetWaterSourceResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<GetWaterSourceResponse>,
                response: Response<GetWaterSourceResponse>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    Log.v("DEBUG : ", data.toString())
                    status=data!!.status
                    msg = data.message
                    if(status){
                        loading.value=true
                        val list=data.data
                        waterResponse.value = GetWaterSourceResponse(status,msg,list)
                    }else{
                        loading.value=true
                    }

                }else{
                    loading.value=true
                }

            }
        })

        return waterResponse
    }
}