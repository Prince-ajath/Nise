package com.ncdc.nise.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


object SurveyorDetailsRepository {

    val surveyorResponse = MutableLiveData<SurveyorResponse>()
    var status:Boolean=false
    var msg:String=""
    var id:String=""

    fun getServicesApiCall(sName:String,sDesignation :String,sOrganistion:String,sDate:String,sTime:String,sContactNo:String,sOtherInfo:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.setSurveyor(sName,sDesignation,sOrganistion,sDate,sTime,sContactNo,sOtherInfo)

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

}