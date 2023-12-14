package com.ncdc.nise.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.spaceavilability.GetSpaceResponse
import com.ncdc.nise.data.model.vaccineStorage.GetVaccineStorageResponse
import com.ncdc.nise.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object VaccineStorageRepo {
    val surveyorResponse = MutableLiveData<SurveyorResponse>()
    val vaccineResponse=MutableLiveData<GetVaccineStorageResponse>()
    var loading=MutableLiveData<Boolean>()
    var status:Boolean=false
    var msg:String=""
    var id:String=""

    fun addVaccineStorageApi(surveyId:Int,vRefrigrotrAvl:String,vRefrigrotrWorking :String,vSolorPwrd:String,vTempMentained:String,vUsagePattern:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.addVaccineDetails(surveyId,vRefrigrotrAvl,vRefrigrotrWorking,vSolorPwrd,vTempMentained,vUsagePattern)

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

                        surveyorResponse.value = SurveyorResponse(false,msg,id)
                    }

                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Response Serevr Error",id)
                }


            }
        })

        return surveyorResponse
    }
    fun updateVaccineStorageApi(surveyId:Int,vRefrigrotrAvl:String,vRefrigrotrWorking :String,vSolorPwrd:String,vTempMentained:String,vUsagePattern:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.updateVaccineDetails(surveyId,vRefrigrotrAvl,vRefrigrotrWorking,vSolorPwrd,vTempMentained,vUsagePattern)

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

                        surveyorResponse.value = SurveyorResponse(false,msg,id)
                    }

                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Response Serevr Error",id)
                }


            }
        })

        return surveyorResponse
    }
    fun getVaccineStorageApi(surveyId:Int): MutableLiveData<GetVaccineStorageResponse> {
        loading.value=false
        val call = RetrofitClient.apiInterface.getVacination(surveyId)

        call.enqueue(object: Callback<GetVaccineStorageResponse> {
            override fun onFailure(call: Call<GetVaccineStorageResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<GetVaccineStorageResponse>,
                response: Response<GetVaccineStorageResponse>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    status=data!!.status
                    msg = data.message
                    if(status){
                        loading.value=true
                        val list=data.data
                        vaccineResponse.value = GetVaccineStorageResponse(status,msg,list)
                    }else{
                        loading.value=true
                    }
                }else{
                    loading.value=true
                }


            }
        })

        return vaccineResponse
    }
}