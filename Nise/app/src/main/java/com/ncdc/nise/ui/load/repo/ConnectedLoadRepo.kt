package com.ncdc.nise.ui.load.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.ui.load.model.load.LoadItem
import com.ncdc.nise.ui.load.model.load.LoadItemResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ConnectedLoadRepo {
    val surveyorResponse = MutableLiveData<SurveyorResponse>()
    val loadResponsData = MutableLiveData<LoadItemResponse>()
    val loading=MutableLiveData<Boolean>()
    var item: ArrayList<LoadItem> = ArrayList()
    var status:Boolean=false
    var msg:String=""
    var id:String=""

    fun getConnectedLoadApi(surveyId:Int,clCategory:String,clName :String,clWattage:String,clQuantity:String,clOperationalDay:String,clOperationalNight:String,clCriticalLoad:String,clLoadType:String,clEnergyRating:String,clYearInstallation:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.addConnectedLoad(surveyId,clCategory,clName,clWattage,clQuantity,clOperationalDay,clOperationalNight,clCriticalLoad,clLoadType,clEnergyRating,clYearInstallation)

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
                    surveyorResponse.value = SurveyorResponse(false,"Waiting....","")
                }



            }
        })

        return surveyorResponse
    }




    fun getLoadDataApi(cloadId:Int): MutableLiveData<LoadItemResponse> {
        loading.value=false
        val call = RetrofitClient.apiInterface.getLoadData(cloadId)

        call.enqueue(object: Callback<LoadItemResponse> {
            override fun onFailure(call: Call<LoadItemResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())

            }

            override fun onResponse(
                call: Call<LoadItemResponse>,
                response: Response<LoadItemResponse>
            ) {
                if(response.isSuccessful){
                    loading.value=true
                    val data = response.body()
                    if(data!=null){
                        status=data.status
                        msg = data.message
                        if(status){
                            loading.value=true
                            item=data.items
                            loadResponsData.value = LoadItemResponse(status,msg,item)
                        }else{
                            loading.value=true
                            loadResponsData.value = LoadItemResponse(status,msg,item)
                        }
                    }

                }else{
                    loading.value=true
                    loadResponsData.value = LoadItemResponse(false,"Waiting...",item)
                }

            }
        })

        return loadResponsData
    }


}