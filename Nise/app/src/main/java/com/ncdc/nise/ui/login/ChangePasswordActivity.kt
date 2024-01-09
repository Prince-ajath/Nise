package com.ncdc.nise.ui.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ncdc.nise.R
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.ActivityChangepasswordBinding
import com.ncdc.nise.ui.login.model.updatePassResponse
import com.ncdc.nise.ui.survey.presentation.add.ViewSurveyorActivity
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChangePasswordActivity :AppCompatActivity() {
    lateinit var binding:ActivityChangepasswordBinding

    var status:Boolean=false
    var msg:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangepasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnProceed.setOnClickListener {
            val isValid = validateForm()
            if(isValid){

                updatePasswordApi(binding.etcnfPassword.text.toString(),binding.etPassword.text.toString()) ;
            }

        }
        binding.back.setOnClickListener {
            val intent= Intent(this@ChangePasswordActivity, ViewSurveyorActivity::class.java)
            startActivity(intent)
            finish()
        }
        val passwordType = arrayOf("text")

        binding.etPasswordHideShow.setOnClickListener(View.OnClickListener {
            if (passwordType.get(0).equals("text")) {
                binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                binding.etPasswordHideShow.setImageResource(R.drawable.hide)
                passwordType[0] = "password"
            } else {
                binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
                binding.etPasswordHideShow.setImageResource(R.drawable.show)
                passwordType[0] = "text"
            }
        })

        binding.etoldPasswordHideShow.setOnClickListener(View.OnClickListener {
            if (passwordType.get(0).equals("text")) {
                binding.etoldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                binding.etoldPasswordHideShow.setImageResource(R.drawable.hide)
                passwordType[0] = "password"
            } else {
                binding.etoldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
                binding.etoldPasswordHideShow.setImageResource(R.drawable.show)
                passwordType[0] = "text"
            }
        })

        binding.etcnfPasswordHideShow.setOnClickListener(View.OnClickListener {
            if (passwordType.get(0).equals("text")) {
                binding.etcnfPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                binding.etcnfPasswordHideShow.setImageResource(R.drawable.hide)
                passwordType[0] = "password"
            } else {
                binding.etcnfPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
                binding.etcnfPasswordHideShow.setImageResource(R.drawable.show)
                passwordType[0] = "text"
            }
        })
    }

    fun updatePasswordApi(oldPassword:String,newPassword:String) {
        binding.progressBar.visibility==View.VISIBLE
        var surveyorId:Int=SharePreference.getIntPref(this@ChangePasswordActivity,Constants.UserId)

        val call = RetrofitClient.apiInterface.updatePassword(surveyorId,oldPassword,newPassword)

        call.enqueue(object: Callback<updatePassResponse> {
            override fun onFailure(call: Call<updatePassResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<updatePassResponse>,
                response: Response<updatePassResponse>
            ) {

                if(response.isSuccessful){
                    binding.progressBar.visibility = View.GONE
                    val data = response.body()
                    status =data!!.status
                    msg = data.message
                    if(status){
                        showDialog(msg)


                    }else{

                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@ChangePasswordActivity, msg, Toast.LENGTH_SHORT).show()


                    }

                }else{
                    Toast.makeText(this@ChangePasswordActivity, msg, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE

                }

            }
        })


    }

    fun showDialog(message:String) {
        val dialog = Dialog(this, R.style.AppCompatAlertDialogStyleBig)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dlg_confirmation)
        var tvTitle = dialog.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.setText(message)

        var tvOkSetting = dialog.findViewById<View>(R.id.tvOkSetting) as TextView
        tvOkSetting.setOnClickListener {
            dialog.dismiss()
            val intent= Intent(this@ChangePasswordActivity, ViewSurveyorActivity::class.java)
            startActivity(intent)
            finish()
        }
        dialog.show()
    }
    private fun validateForm(): Boolean {
        var isValid = false

        if(binding.etPassword.text.isNullOrEmpty()){
            isValid = false
            binding.etPassword.error = "Please enter old password"
        }else if(!binding.etoldPassword.text.toString().equals(binding.etcnfPassword.text.toString())){
            binding.etPassword.error =null
            isValid=false
            binding.etcnfPassword.error="Old Password and Confirm Password does not matched"
        }else{
            binding.etcnfPassword.error=null
            isValid = true
        }
        return isValid
    }
}