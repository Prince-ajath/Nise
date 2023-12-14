package com.ncdc.nise.ui.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ncdc.nise.R
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.ActivityLoginBinding
import com.ncdc.nise.ui.core.CoreActivity
import com.ncdc.nise.ui.login.viewModel.LoginViewModel
import com.ncdc.nise.ui.register.RegisterActivity
import com.ncdc.nise.ui.register.model.AuthData
import com.ncdc.nise.ui.register.model.AuthResponse
import com.ncdc.nise.ui.register.repository.RegisterRepo
import com.ncdc.nise.ui.survey.presentation.add.ViewSurveyorActivity
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : CoreActivity() {

    lateinit var binding:ActivityLoginBinding
    lateinit var loginViewModel:LoginViewModel
    var activeUser:Int=0
    var loading=MutableLiveData<Boolean>()
    var status:Boolean=false
    var msg:String = ""
    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel=ViewModelProvider(this@LoginActivity).get(LoginViewModel::class.java)
        registerInternetConnectionReceiver()
        with(binding) {

            etEmail.doOnTextChanged { text, _, _, _ ->
                email.value = text.toString()
                etEmail.error=null
            }
            etPassWord.doOnTextChanged { text, _, _, _ ->
                password.value = text.toString()
                edPassword.error=null
            }

        }

        binding.tvLogin.setOnClickListener {
            validateAndLogin()

        }

        binding.tvSignup.setOnClickListener {
            val intent=Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun validateAndLogin() {
        val isValid = validateForm()
        if (isValid) {
            binding.progressBar.visibility = View.VISIBLE
            getLoginApi(binding.etEmail.text.toString(),binding.edPassword.editText?.text.toString())

        }
    }

    private fun validateForm(): Boolean {
        var isValid = false
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        var sEmailId=binding.etEmail.text.toString()
        if(!hasInternet){
            showSnackBar(getString(R.string.no_internet))
            isValid = false
        }

        if(!EMAIL_REGEX.toRegex().matches(sEmailId) && sEmailId.length==0){
            isValid = false
            binding.etEmail.error = getString(R.string.email_field_required)
        }else if(binding.edPassword.editText?.text.toString().length==0){
            binding.etEmail.error =null
            isValid=false
            binding.edPassword.error="Password field is required"
        }else{
            binding.edPassword.error=null
            isValid = true
        }
        return isValid
    }
    private fun showSnackBar(title: String) {
        val snackBar = Snackbar.make(binding.rootView, title,
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackBar.setActionTextColor(Color.BLACK)
        snackBar.show()
    }


    fun showDialog() {
        val dialog = Dialog(this, R.style.AppCompatAlertDialogStyleBig)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dlg_confirmation)
        var tvTitle = dialog.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.setText(getString(R.string.waiting_approval))

        var tvOkSetting = dialog.findViewById<View>(R.id.tvOkSetting) as TextView
        tvOkSetting.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }


    fun getLoginApi(sEmail:String,sPassword:String) {

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
                    binding.progressBar.visibility = View.GONE
                    val data = response.body()
                    status =data!!.status
                    msg = data.message
                    if(status){
                        var authData:AuthData=data.data
                        activeUser=authData.active
                        if(activeUser==0){
                            showDialog()
                        }else{
                            var surveyorId:String= authData.id
                            var surveyorName:String=authData.username
                            var userId:Int=Integer.parseInt(surveyorId)
                            SharePreference.setIntPref(this@LoginActivity,Constants.UserId,userId)
                            SharePreference.setStringPref(this@LoginActivity,Constants.UserName,surveyorName)
                            SharePreference.setBooleanPref(this@LoginActivity,Constants.isLogin,true)
                            AddAuditApi(userId,surveyorName)


                        }

                    }else{

                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, msg,Toast.LENGTH_SHORT).show()


                    }

                }else{
                    Toast.makeText(this@LoginActivity, msg,Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE

                }

            }
        })


    }
    fun AddAuditApi(surveyorId:Int,surveyorName:String){

        val call = RetrofitClient.apiInterface.isAuditPage(surveyorId,surveyorName,0,"Insert","Login")

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
                    if(status){
                        val intent=Intent(this@LoginActivity, ViewSurveyorActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }


            }
        })
    }
}