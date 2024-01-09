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
import com.ncdc.nise.databinding.ActivityForgotBinding
import com.ncdc.nise.ui.login.model.updatePassResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotActivity :AppCompatActivity() {

    lateinit var binding:ActivityForgotBinding

    var status:Boolean=false
    var msg:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnForgot.setOnClickListener {
            val isValid = validateForm()
            if(isValid){

                forgotPasswordApi(binding.etEmail.text.toString(),binding.etcnfPassword.text.toString())
            }

        }
        binding.back.setOnClickListener {
            val intent= Intent(this@ForgotActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val passwordType = arrayOf("text")

        binding.etoldPasswordHideShow.setOnClickListener(View.OnClickListener {
            if (passwordType.get(0).equals("text")) {
                binding.etnewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                binding.etoldPasswordHideShow.setImageResource(R.drawable.hide)
                passwordType[0] = "password"
            } else {
                binding.etnewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
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

    fun forgotPasswordApi(emailId:String,newPassword:String) {
        binding.progressBar.visibility== View.VISIBLE

        val call = RetrofitClient.apiInterface.forgotPassword(emailId,newPassword)

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
                        Toast.makeText(this@ForgotActivity, msg, Toast.LENGTH_SHORT).show()


                    }

                }else{
                    Toast.makeText(this@ForgotActivity, msg, Toast.LENGTH_SHORT).show()
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
            val intent= Intent(this@ForgotActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        dialog.show()
    }
    private fun validateForm(): Boolean {

        var isValid = false

        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        var sEmailId=binding.etEmail.text.toString()

        if(!EMAIL_REGEX.toRegex().matches(sEmailId) && sEmailId.length==0){
            isValid = false
            binding.etEmail.error = "Please enter register email id"
        }else if(!binding.etnewPassword.text.toString().equals(binding.etcnfPassword.text.toString())){
            binding.etEmail.error =null
            isValid=false
            binding.etcnfPassword.error="Old Password and Confirm Password does not matched"
        }else{
            binding.etcnfPassword.error=null
            isValid = true
        }
        return isValid
    }

}