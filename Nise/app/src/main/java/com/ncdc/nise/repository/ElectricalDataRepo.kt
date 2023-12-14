package com.ncdc.nise.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.electrical.getElectricalResponse
import com.ncdc.nise.data.model.health.HealthDetailsModel
import com.ncdc.nise.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ElectricalDataRepo {

    val surveyorResponse = MutableLiveData<SurveyorResponse>()
    var electricalResponse=MutableLiveData<getElectricalResponse>()
    var loading=MutableLiveData<Boolean>()
    var status:Boolean=false
    var msg:String=""
    var id:String=""

    fun getElectricalDataApi(surveyId:Int,epAccess:String,epConnection :String,epBill:String,epPeakLoadBill:String,epConnctionType:String,epAverage:String,epAvlMainLine:String,epVoltage:String,
                             epAvgPower:String,epPSSupply:String,epSSSupply:String,epMtrConnAvlbl:String,epAcVltag:String,epExpnLoads:String,epOutdrsLightng:String,
                             epSolrSystm:String,epConsumption:String,epPrvsionsExistng:String,epEnrgyEffcncy:String,epPwrFactr:String,epDistbtionBord:String,epNoMcbFive:String,
                             epNoMCBUptoTen:String,epNoMCBUpToTwnty:String,epWiringConditns:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.addElectricalDetailsApi(surveyId,epAccess,epConnection,epBill,epPeakLoadBill,epConnctionType,epAverage,epAvlMainLine,epVoltage,epAvgPower,epPSSupply,epSSSupply,epMtrConnAvlbl,
            epAcVltag,epExpnLoads,epOutdrsLightng,epSolrSystm,epConsumption,epPrvsionsExistng,epEnrgyEffcncy,epPwrFactr,epDistbtionBord,epNoMcbFive,epNoMCBUptoTen,epNoMCBUpToTwnty,epWiringConditns)

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
                        val msg = data.message
                        id=""
                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }
                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Respnse Server Erroe","")
                }


            }
        })

        return surveyorResponse
    }

    fun updateElectricalDataApi(surveyId:Int,epAccess:String,epConnection :String,epBill:String,epPeakLoadBill:String,epConnctionType:String,epAverage:String,epAvlMainLine:String,epVoltage:String,
                             epAvgPower:String,epPSSupply:String,epSSSupply:String,epMtrConnAvlbl:String,epAcVltag:String,epExpnLoads:String,epOutdrsLightng:String,
                             epSolrSystm:String,epConsumption:String,epPrvsionsExistng:String,epEnrgyEffcncy:String,epPwrFactr:String,epDistbtionBord:String,epNoMcbFive:String,
                             epNoMCBUptoTen:String,epNoMCBUpToTwnty:String,epWiringConditns:String): MutableLiveData<SurveyorResponse> {

        val call = RetrofitClient.apiInterface.updateElectricalDetailsApi(surveyId,epAccess,epConnection,epBill,epPeakLoadBill,epConnctionType,epAverage,epAvlMainLine,epVoltage,epAvgPower,epPSSupply,epSSSupply,epMtrConnAvlbl,
            epAcVltag,epExpnLoads,epOutdrsLightng,epSolrSystm,epConsumption,epPrvsionsExistng,epEnrgyEffcncy,epPwrFactr,epDistbtionBord,epNoMcbFive,epNoMCBUptoTen,epNoMCBUpToTwnty,epWiringConditns)

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
                        val msg = data.message
                        id=""
                        surveyorResponse.value = SurveyorResponse(status,msg,id)
                    }
                }else{
                    surveyorResponse.value = SurveyorResponse(false,"Respnse Server Erroe","")
                }


            }
        })

        return surveyorResponse
    }

    fun getElectricalApi(surveyId:Int): MutableLiveData<getElectricalResponse> {
        loading.value=false
        val call = RetrofitClient.apiInterface.getElectricalData(surveyId)

        call.enqueue(object: Callback<getElectricalResponse> {
            override fun onFailure(call: Call<getElectricalResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<getElectricalResponse>,
                response: Response<getElectricalResponse>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    if (data != null) {
                        status=data.status
                        msg = data.message
                        if(status){
                            loading.value=true
                            val list=data.data
                            electricalResponse.value = getElectricalResponse(status,msg,list)
                        }else{
                            loading.value=true
                        }
                    }


                }else{
                    loading.value=true
                }

            }
        })

        return electricalResponse
    }
}