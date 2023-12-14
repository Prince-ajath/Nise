package com.ncdc.nise.ui.register.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.ui.register.model.AuthData
import com.ncdc.nise.ui.register.model.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RegisterRepo {

    val registerResponse = MutableLiveData<AuthResponse>()
    val failResponse=MutableLiveData<String>()
    val statusResponse=MutableLiveData<String>()
    var loading=MutableLiveData<Boolean>()
    var status:Boolean=false
    var msg:String = ""
    var authData:AuthData= AuthData("","",0)


    fun getRegisterApi(sName:String,sEmail:String,sDesignation :String,sOrganistion:String,sContactNo:String,sPassword:String,sActive:Int): MutableLiveData<AuthResponse> {

        val call = RetrofitClient.apiInterface.register(sName,sEmail,sDesignation,sOrganistion,sContactNo,sPassword,sActive)

        call.enqueue(object: Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                if(response.isSuccessful){

                    val data = response.body()
                    status=data!!.status
                    if(status){
                        loading.value=true
                        msg = data.message
                        val authData=data.data
                        registerResponse.value = AuthResponse(status,msg,authData)
                    }else{
                        loading.value=false
                        statusResponse.value= msg

                    }

                }else{
                    loading.value=false
                }


            }
        })

        return registerResponse
    }


    fun getLoginApi(sEmail:String,sPassword:String): MutableLiveData<AuthResponse> {

        val call = RetrofitClient.apiInterface.login(sEmail,sPassword)

        call.enqueue(object: Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {

                if(response.isSuccessful){

                    val data = response.body()
                    status=data!!.status
                    msg = data.message
                    if(status){
                        authData=data.data
                        registerResponse.value = AuthResponse(status,msg, authData)
                    }else{

                        registerResponse.value = AuthResponse(status,msg, authData)


                    }

                }else{
                    loading.value=false
                }

            }
        })

        return registerResponse
    }
}