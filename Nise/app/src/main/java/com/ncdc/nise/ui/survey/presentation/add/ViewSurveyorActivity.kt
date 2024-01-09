package com.ncdc.nise.ui.survey.presentation.add

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import com.ncdc.nise.MainActivity
import com.ncdc.nise.R
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.ActivityAddSurveyerBinding
import com.ncdc.nise.interfaces.OnClickListener
import com.ncdc.nise.ui.core.CoreActivity
import com.ncdc.nise.ui.login.ChangePasswordActivity
import com.ncdc.nise.ui.login.LoginActivity
import com.ncdc.nise.ui.register.model.AuthData
import com.ncdc.nise.ui.register.model.AuthResponse
import com.ncdc.nise.ui.survey.adapter.SurveyViewItemAdapter
import com.ncdc.nise.ui.survey.model.SurveyDetailsItem
import com.ncdc.nise.ui.survey.model.getSurveyDetailsResponse
import com.ncdc.nise.ui.survey.model.updateResponse
import com.ncdc.nise.ui.survey.viewModel.SurveyDetailsViewModel
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ViewSurveyorActivity : AppCompatActivity(),OnClickListener {


    lateinit var binding:ActivityAddSurveyerBinding
    lateinit var  surveyDetailsViewModel: SurveyDetailsViewModel
    private  var listAdapter:SurveyViewItemAdapter= SurveyViewItemAdapter(this,arrayListOf(),this)
    private lateinit var lottieAnimation: LottieAnimationView
    private lateinit var progressBar: ProgressBar
    var backPressedTime: Long = 0
    var loadDataList:ArrayList<SurveyDetailsItem>?=ArrayList<SurveyDetailsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSurveyerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        surveyDetailsViewModel=ViewModelProvider(this).get(SurveyDetailsViewModel::class.java)
        initViews()
        observeViewModel()


    }
    private fun initViews() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        progressBar = binding.progressBar
        lottieAnimation = findViewById(R.id.noDataFound)
        binding.rvSurvey.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
        binding.addSurvey.setOnClickListener {
            SharePreference.setBooleanPref(this@ViewSurveyorActivity, Constants.isEdit,false)
            SharePreference.setBooleanPref(this@ViewSurveyorActivity,Constants.isBackPressed,false)
            val intent= Intent(this@ViewSurveyorActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }



    private fun observeViewModel(){
        binding.progressBar.visibility = View.VISIBLE
        var surveyorId:Int=SharePreference.getIntPref(this@ViewSurveyorActivity,Constants.UserId)
        surveyDetailsViewModel.getSurveyorApi(surveyorId)
        surveyDetailsViewModel.loading!!.observe(this) {
            if (it) {
                binding.progressBar.visibility = View.GONE
            }
        }
        loadDataList!!.clear()
        surveyDetailsViewModel.surveyResponseData!!.observe(this, Observer { getSurveyDetailsResponse->
            var checkStaus:Boolean=getSurveyDetailsResponse.status
            if(checkStaus){
                binding.progressBar.visibility = View.GONE
                loadDataList= getSurveyDetailsResponse.items
                if(loadDataList!!.size >0){
                    listAdapter.setRecords(loadDataList!!)
                    binding.rvSurvey.visibility = View.VISIBLE
                    lottieAnimation.visibility = View.GONE
                    listAdapter.notifyDataSetChanged()

                }else{
                    lottieAnimation.visibility = View.VISIBLE
                    binding.rvSurvey.visibility = View.GONE

                }

            }else{
                binding.progressBar.visibility = View.GONE

            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_logout -> {
                showDialog()
            }
            R.id.changePassword ->{

                val intent=Intent(this@ViewSurveyorActivity, ChangePasswordActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDialog() {
        val dialog = Dialog(this, R.style.AppCompatAlertDialogStyleBig)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dlg_logout)
        var tvOkSetting = dialog.findViewById<View>(R.id.tvProceed) as TextView
        tvOkSetting.setOnClickListener {

            var isLogout=SharePreference.mLogout(this)
            if(isLogout){
                AddAuditApi("Logout")
                SharePreference.setBooleanPref(this@ViewSurveyorActivity,Constants.isLogin,false)
                SharePreference.setIntPref(this@ViewSurveyorActivity,Constants.UserId,0)
                val intent=Intent(this@ViewSurveyorActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }


        }
        var tvNo = dialog.findViewById<View>(R.id.tvCancel) as TextView
        tvNo.setOnClickListener {
          dialog.dismiss()
        }
        dialog.show()
    }

    override fun onUpdate(surveyId: Int) {
        alertBox(surveyId)
    }

    fun alertBox(surveyId: Int) {
        val dialog = Dialog(this, R.style.AppCompatAlertDialogStyleBig)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.item_submit_alert)
        var tvOkSetting = dialog.findViewById<View>(R.id.tvProceed) as TextView
        tvOkSetting.setOnClickListener {
            updateStatusApi(surveyId)
            listAdapter.notifyDataSetChanged()
            dialog.dismiss()

        }
        var tvNo = dialog.findViewById<View>(R.id.tvCancel) as TextView
        tvNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    fun updateStatusApi(surveyId:Int) {
        binding.progressBar.visibility = View.VISIBLE
        val call = RetrofitClient.apiInterface.isUpdateStatus(surveyId,1)

        call.enqueue(object: Callback<updateResponse> {
            override fun onFailure(call: Call<updateResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }
            override fun onResponse(
                call: Call<updateResponse>,
                response: Response<updateResponse>
            ) {
                if(response.isSuccessful){
                    binding.progressBar.visibility = View.GONE
                    val data = response.body()
                    var status =data!!.status
                    var msg = data.message
                    if(status){
                        observeViewModel()
                        listAdapter.notifyDataSetChanged()
                        AddAuditApi("Final Submission")

                    }else{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@ViewSurveyorActivity, msg,Toast.LENGTH_SHORT).show()
                    }

                }else{
                    binding.progressBar.visibility = View.GONE
                }

            }
        })


    }
    fun AddAuditApi(operationType:String){
        var userId:Int= SharePreference.getIntPref(this@ViewSurveyorActivity, Constants.UserId)
        var userName:String?= SharePreference.getStringPref(this@ViewSurveyorActivity, Constants.UserName)
        var surveyId:Int= SharePreference.getIntPref(this@ViewSurveyorActivity, Constants.SurveyId)
        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,surveyId,operationType,"View Survey Details")

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
                }


            }
        })
    }
    override fun onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()
    }


}


