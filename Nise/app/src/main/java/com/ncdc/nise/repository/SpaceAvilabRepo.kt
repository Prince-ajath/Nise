package com.ncdc.nise.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.electrical.getElectricalResponse
import com.ncdc.nise.data.model.spaceavilability.GetSpaceResponse
import com.ncdc.nise.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SpaceAvilabRepo {

    val surveyorResponse = MutableLiveData<SurveyorResponse>()
    val spaceAvilableResponse=MutableLiveData<GetSpaceResponse>()
    var loading=MutableLiveData<Boolean>()
    var status:Boolean=false
    var msg:String=""
    var id:String=""

    fun getSpaceAvlApi(surveyId:Int,spFreeAreaRoof:String,spOpenGround :String,spHost:String,spScurStorage:String,spAvlblErthng:String,spInstallation:String,spCpu:String,spSlrPVSystm:String,
                             spTheftSite:String,spInstltionLoc:String,spTypeRoof:String,spOrientation:String,spTiltAngle:String,spTotlArea:String,spTotalShadow:String,spObstructions:String,
                             spGnrlShape:String,spAccessRoof:String,spWeightRestricton:String,spObstruction:String,spConstruction:String,spEcondition:String,spRoofConditons:String,
                             spSupportConditons:String,spRoofMtrial:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.addSpaceAvilabilty(surveyId,spFreeAreaRoof,spOpenGround,spHost,spScurStorage,spAvlblErthng,spInstallation,spCpu,spSlrPVSystm,spTheftSite,spInstltionLoc,spTypeRoof,spOrientation,spTiltAngle,
            spTotlArea,spTotalShadow,spObstructions,spGnrlShape,spAccessRoof,spWeightRestricton,spObstruction,spConstruction,spEcondition,spRoofConditons,spSupportConditons,spRoofMtrial)

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
                   surveyorResponse.value = SurveyorResponse(false,"Response Serever Error","")
               }



            }
        })

        return surveyorResponse
    }

    fun updateSpaceAvlApi(surveyId:Int,spFreeAreaRoof:String,spOpenGround :String,spHost:String,spScurStorage:String,spAvlblErthng:String,spInstallation:String,spCpu:String,spSlrPVSystm:String,
                       spTheftSite:String,spInstltionLoc:String,spTypeRoof:String,spOrientation:String,spTiltAngle:String,spTotlArea:String,spTotalShadow:String,spObstructions:String,
                       spGnrlShape:String,spAccessRoof:String,spWeightRestricton:String,spObstruction:String,spConstruction:String,spEcondition:String,spRoofConditons:String,
                       spSupportConditons:String,spRoofMtrial:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.updateSpaceAvilabilty(surveyId,spFreeAreaRoof,spOpenGround,spHost,spScurStorage,spAvlblErthng,spInstallation,spCpu,spSlrPVSystm,spTheftSite,spInstltionLoc,spTypeRoof,spOrientation,spTiltAngle,
            spTotlArea,spTotalShadow,spObstructions,spGnrlShape,spAccessRoof,spWeightRestricton,spObstruction,spConstruction,spEcondition,spRoofConditons,spSupportConditons,spRoofMtrial)

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
                        id=""
                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }
                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Response Serever Error","")
                }



            }
        })

        return surveyorResponse
    }

    fun getSpaceAvilableApi(surveyId:Int): MutableLiveData<GetSpaceResponse> {
        loading.value=false
        val call = RetrofitClient.apiInterface.getSpaceAvilabiltiy(surveyId)

        call.enqueue(object: Callback<GetSpaceResponse> {
            override fun onFailure(call: Call<GetSpaceResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<GetSpaceResponse>,
                response: Response<GetSpaceResponse>
            ) {
               if(response.isSuccessful){
                   val data = response.body()
                   status=data!!.status
                   msg = data.message
                   if(status){
                       loading.value=true
                       val list=data.data
                       spaceAvilableResponse.value = GetSpaceResponse(status,msg,list)
                   }else{
                       loading.value=true
                   }
               }else{
                   loading.value=true
               }


            }
        })

        return spaceAvilableResponse
    }
}