package com.ncdc.nise.ui.register

import android.app.Dialog
import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ncdc.nise.R
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.ActivitySignupBinding
import com.ncdc.nise.ui.core.CoreActivity
import com.ncdc.nise.ui.login.LoginActivity
import com.ncdc.nise.ui.register.model.AuthData
import com.ncdc.nise.ui.register.model.AuthResponse
import com.ncdc.nise.ui.register.repository.RegisterRepo
import com.ncdc.nise.ui.register.viewModel.RegisterViewModel
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegisterActivity : CoreActivity() {
    lateinit var binding:ActivitySignupBinding
    lateinit var registerViewModel:RegisterViewModel
    var loading=MutableLiveData<Boolean>()
    var status:Boolean=false
    var msg:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerInternetConnectionReceiver()
        registerViewModel = ViewModelProvider(this@RegisterActivity).get(RegisterViewModel::class.java)
        binding.tvSignUp.setOnClickListener {
            validateAndLogin()
        }

        binding.tvLogin.setOnClickListener {
            val intent= Intent(this@RegisterActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun validateAndLogin() {
        val isValid = validateForm()
        if (isValid) {
            binding.progressBar.visibility = View.VISIBLE
            getRegisterApi(binding.sName.text.toString(),binding.edEmail.text.toString(),binding.sDesignation.text.toString(),binding.sOrganisation.text.toString(),binding.sContactNo.text.toString(),"Nise@123#",0)

        }
    }

    private fun validateForm(): Boolean {
        var isValid = false
        val regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]")
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";

        var fName=binding.sName.text.toString()
        var sEmailId=binding.edEmail.text.toString()
        if(!hasInternet){
            showSnackBar(getString(R.string.no_internet))
            isValid = false
        }

        if (!regex.matcher(fName).find()&& fName.length == 0 ) {
            isValid = false
            binding.sName.error = getString(R.string.name_field_required)

        }else if(!EMAIL_REGEX.toRegex().matches(sEmailId) && sEmailId.length==0){
            isValid = false
            binding.edEmail.error = getString(R.string.email_field_required)
        }else if (binding.sDesignation.text.isNullOrEmpty()&& binding.sDesignation.text.toString().length==0) {
            isValid = false
            binding.sDesignation.error = getString(R.string.surveyor_field_required)

        }else if (binding.sOrganisation.text.isNullOrEmpty() && binding.sOrganisation.text.toString().length==0) {
            isValid = false
            binding.sOrganisation.error = getString(R.string.surveyor_organisation_required)

        }else if (binding.sContactNo.text.isNullOrEmpty()||binding.sContactNo.text.length < 10) {
            isValid = false
            binding.sContactNo.error = getString(R.string.surveyor_ContactNo_required)

        }else{
            isValid = true
        }



        return isValid
    }

    fun showDialog() {
        val dialog = Dialog(this, R.style.AppCompatAlertDialogStyleBig)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dlg_confirmation)
        var tvTitle = dialog.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.setText(getString(R.string.confirmation_email))

        var tvOkSetting = dialog.findViewById<View>(R.id.tvOkSetting) as TextView
        tvOkSetting.setOnClickListener {
            val intent=Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        dialog.show()
    }
    private fun showSnackBar(title: String) {
        val snackBar = Snackbar.make(binding.rootView, title,
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackBar.setActionTextColor(Color.BLACK)
        snackBar.show()
    }

    fun getRegisterApi(sName:String,sEmail:String,sDesignation :String,sOrganistion:String,sContactNo:String,sPassword:String,sActive:Int) {
        binding.progressBar.visibility = View.VISIBLE
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
                    binding.progressBar.visibility = View.GONE
                    val data = response.body()
                    status =data!!.status
                    msg = data.message
                    if(status){
                        val data=data.data
                        var surveyorId:String= data.id
                        var userId:Int=Integer.parseInt(surveyorId)
                        SharePreference.setIntPref(this@RegisterActivity, Constants.UserId,userId)
                        AddAuditApi()
                        showDialog()
                        Toast.makeText(this@RegisterActivity, msg,Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(this@RegisterActivity, msg,Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                        loading.value=false


                    }

                }else{
                    Toast.makeText(this@RegisterActivity, msg,Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    loading.value=false
                }


            }
        })


    }
    fun AddAuditApi(){
        var userId:Int=SharePreference.getIntPref(this@RegisterActivity,Constants.UserId)
        var userName:String?=SharePreference.getStringPref(this@RegisterActivity,Constants.UserName)
        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,0,"Create","Register")

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
                    status =data!!.status
                    msg = data.message
                }


            }
        })
    }

}