package com.ncdc.nise.ui.survey.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.ui.survey.model.SurveyDetailsItem
import com.ncdc.nise.ui.survey.model.getSurveyDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SurveyDetailsRepo {
    val surveyResponsData = MutableLiveData<getSurveyDetailsResponse>()
    val loading=MutableLiveData<Boolean>()
    var status=false
    var msg=""
    var item: ArrayList<SurveyDetailsItem> = ArrayList()

    fun getSurveyDataApi(surveyorId:Int): MutableLiveData<getSurveyDetailsResponse> {
        loading.value=false
        val call = RetrofitClient.apiInterface.getSurveyData(surveyorId)

        call.enqueue(object: Callback<getSurveyDetailsResponse> {
            override fun onFailure(call: Call<getSurveyDetailsResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
                loading.value=true

            }
            override fun onResponse(
                call: Call<getSurveyDetailsResponse>,
                response: Response<getSurveyDetailsResponse>) {
                if(response.isSuccessful){
                    loading.value=true
                    val data = response.body()
                    status=data!!.status
                    val msg = data.message
                    if(status){
                        loading.value=true
                        item=data.items
                        surveyResponsData.value = getSurveyDetailsResponse(status,msg,item)
                    }else{
                        loading.value=true
                        surveyResponsData.value = getSurveyDetailsResponse(status,msg,item)
                    }
                }else{
                    loading.value=true
                    surveyResponsData.value = getSurveyDetailsResponse(false,"Response Server Error ",item)
                }



            }
        })

        return surveyResponsData
    }
}