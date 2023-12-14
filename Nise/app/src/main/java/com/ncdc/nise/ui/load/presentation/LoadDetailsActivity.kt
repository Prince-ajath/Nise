package com.ncdc.nise.ui.load.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import com.ncdc.nise.R
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.remote.RetrofitClient

import com.ncdc.nise.databinding.ActivityLoaddetailsBinding
import com.ncdc.nise.interfaces.HomeListener
import com.ncdc.nise.ui.core.CoreActivity
import com.ncdc.nise.ui.load.adapter.LoadItemAdapter
import com.ncdc.nise.ui.load.model.load.LoadItem
import com.ncdc.nise.ui.load.viewModel.CLoadViewModel
import com.ncdc.nise.ui.survey.presentation.add.ViewSurveyorActivity
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadDetailsActivity:CoreActivity(),HomeListener {
    lateinit var binding: ActivityLoaddetailsBinding
    private val listAdapter =LoadItemAdapter(arrayListOf(),this)
    private lateinit var lottieAnimation: LottieAnimationView

    lateinit var cLoadViewModel:CLoadViewModel
    var loadDataList:ArrayList<LoadItem>?=ArrayList<LoadItem>()
    var surveyId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoaddetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerInternetConnectionReceiver()
        val bundle = intent.extras

        if (bundle != null) {
            surveyId = bundle.getString("surveyId")

        }
        cLoadViewModel=ViewModelProvider(this).get(CLoadViewModel::class.java)
        initViews()
        observeViewModel()



        binding.addLeadsFAB.setOnClickListener {
            startActivity(Intent(applicationContext, AddLoadDetailsActivity::class.java).putExtra("surveyId",surveyId))
            overridePendingTransition(0,0)
        }
        binding.backArrow.setOnClickListener {
            startActivity(Intent(this@LoadDetailsActivity,ViewSurveyorActivity::class.java))
            overridePendingTransition(0,0)
        }

    }

    private fun initViews() {
        lottieAnimation = findViewById(R.id.noDataFound)
        binding.usersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }


    }


    override fun onResume() {
        super.onResume()
        val bundle = intent.extras

        if (bundle != null) {
            surveyId = bundle.getString("surveyId")

        }
        initViews()
        observeViewModel()
    }


    private fun observeViewModel(){

            binding.progressBar.visibility = View.VISIBLE
            binding.layout.visibility=View.GONE

            cLoadViewModel.addLoadApi(Integer.parseInt(surveyId))

            cLoadViewModel.loading!!.observe(this) {
            if (it) {
                binding.progressBar.visibility = View.GONE
            }
           }
            loadDataList!!.clear()
            cLoadViewModel.loadResponseData!!.observe(this, Observer { LoadItemResponse->
                var checkStaus:Boolean=LoadItemResponse.status

                if(checkStaus){
                    binding.progressBar.visibility = View.GONE
                    loadDataList=LoadItemResponse.items
                    if(loadDataList!!.size>0){
                        listAdapter.setRecords(loadDataList!!)
                        binding.usersList.visibility = View.VISIBLE
                        binding.layout.visibility=View.VISIBLE
                        lottieAnimation.visibility = View.GONE
                        listAdapter.notifyDataSetChanged()

                    }else{
                        lottieAnimation.visibility = View.VISIBLE
                        binding.usersList.visibility = View.GONE
                        binding.layout.visibility=View.GONE
                    }

                }else{
                    binding.progressBar.visibility = View.GONE
                    lottieAnimation.visibility = View.VISIBLE
                    binding.usersList.visibility = View.GONE
                    binding.layout.visibility=View.GONE

                }
            })



    }

    override fun deleteItem(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Load Data")
        builder.setMessage("Are you sure you want delete this?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            deleteItemApi(id)
            listAdapter.notifyDataSetChanged()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->

        }

        builder.show()
    }

    fun deleteItemApi(loadId:Int){
        binding.progressBar.visibility = View.VISIBLE
        val call = RetrofitClient.apiInterface.deleteLoad(loadId)
        Log.v("DEBUG : ", loadId.toString())

        call.enqueue(object: Callback<SurveyorResponse> {
            override fun onFailure(call: Call<SurveyorResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())

            }

            override fun onResponse(
                call: Call<SurveyorResponse>,
                response: Response<SurveyorResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                val data = response.body()
                val status=data!!.status
                if(status){
                    AddAuditApi("Delete")
                    observeViewModel()
                    listAdapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext,data.message, Toast.LENGTH_SHORT).show()
                }else{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext,data.message, Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    fun AddAuditApi(operationType:String){
        var userId:Int= SharePreference.getIntPref(this@LoadDetailsActivity, Constants.UserId)
        var userName:String?= SharePreference.getStringPref(this@LoadDetailsActivity, Constants.UserName)
        var surveyId:Int= SharePreference.getIntPref(this@LoadDetailsActivity, Constants.SurveyId)
        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,surveyId,operationType,"Connected Load")

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
                    var status =data!!.status
                    var msg = data.message

                }


            }
        })
    }


}
