package com.ncdc.nise.ui.load.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.parser.IntegerParser
import com.google.android.material.snackbar.Snackbar
import com.ncdc.nise.R
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.ActivityAddLoaddetailsBinding
import com.ncdc.nise.ui.core.CoreActivity
import com.ncdc.nise.ui.load.viewModel.CLoadViewModel
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class AddLoadDetailsActivity:CoreActivity() {

    lateinit var binding: ActivityAddLoaddetailsBinding
    lateinit var connectedLoadViewModel: CLoadViewModel
    var rating:String="0.0"
    var surveyId:String?=null
    var surveyIds:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLoaddetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerInternetConnectionReceiver()
        val bundle = intent.extras
        if (bundle != null) {
            surveyId = bundle.getString("surveyId")
            surveyIds=Integer.parseInt(surveyId)


        }
        connectedLoadViewModel= ViewModelProvider(this).get(CLoadViewModel::class.java)
        init()

        binding.tvNextLoadConnected.setOnClickListener {
            if(!hasInternet){
                showSnackBar(getString(R.string.no_internet))
            }else{
                addDataApi()
            }

        }
    }
    private fun init(){

        val criticalLoadArray =  arrayListOf("Yes","No")
        binding.atCriticalLoad.setAdapter(ArrayAdapter(this, R.layout.list_item, criticalLoadArray))

        val loadTypeArray =  arrayListOf("AC","DC")
        binding.atLoadType.setAdapter(ArrayAdapter(this, R.layout.list_item, loadTypeArray))

        binding.rgEnergyRating.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            when (radio) {
                binding.rbEnergyRatingYes -> {
                    binding.energingRating.visibility=View.VISIBLE
                    rating=binding.energingRating.rating.toString()
                }
                binding.rbEnergyRatingNo -> {
                    binding.energingRating.visibility=View.GONE
                    rating="0.0"
                }
            }
        }
    }

    private fun addDataApi(){
        if(surveyIds==0){
            Toast.makeText(this,"User Id Not valid", Toast.LENGTH_SHORT).show()
        }else {
            val isValid = validateForm()
            if (isValid) {
                binding.progressBar.visibility = View.VISIBLE
                binding.ProgressBar1.visibility=View.VISIBLE
                binding.tvNextLoadConnected.visibility=View.GONE
                if(binding.rbEnergyRatingYes.isChecked){
                    rating=binding.energingRating.rating.toString()
                }else if(binding.rbEnergyRatingNo.isChecked){
                    rating="0.0"
                }else{
                    rating=binding.energingRating.rating.toString()
                }
                connectedLoadViewModel.addConnectedLoadApi(
                    surveyIds,
                    binding.connectedLoadCat.editText?.text.toString(),
                    binding.connectedLoadName.editText?.text.toString(),
                    binding.connectedWatt.editText?.text.toString(),
                    binding.connectedQty.editText?.text.toString(),
                    binding.duringDay.editText?.text.toString(),
                    binding.duringNight.editText?.text.toString(),
                    binding.atCriticalLoad.text.toString(),
                    binding.atLoadType.text.toString(),
                    rating,
                    binding.yearInstall.editText?.text.toString()
                )
                connectedLoadViewModel.servicesLiveData!!.observe(
                    this,
                    Observer { SurveyorResponse ->
                        var checkStaus: Boolean = SurveyorResponse.status

                        if (checkStaus) {
                            binding.ProgressBar1.visibility=View.GONE
                            binding.progressBar.visibility = View.GONE
                            binding.tvNextLoadConnected.visibility=View.VISIBLE
                            surveyId = SurveyorResponse.surveyorId

                            AddAuditApi("Insert")



                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.ProgressBar1.visibility=View.GONE
                            binding.tvNextLoadConnected.visibility=View.VISIBLE
                            Toast.makeText(
                                this,
                                "" + SurveyorResponse.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }
    private fun showSnackBar(title: String) {
        val snackBar = Snackbar.make(binding.rootLayout, title,
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackBar.setActionTextColor(Color.BLACK)
        snackBar.show()
    }

    private fun validateForm(): Boolean {
        var isValid = false
        val regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]")

        var fName=binding.connectedLoadCat.editText?.text.toString()


        if (regex.matcher(fName).find() || fName.isEmpty()) {
            isValid = false
            binding.connectedLoadCat.error = "Connected Load Category is required"
            Toast.makeText(
                this@AddLoadDetailsActivity,
                "Connected Load Category is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.connectedLoadName.editText?.text.isNullOrEmpty()) {
                binding.connectedLoadCat.error =null
            isValid = false
            binding.connectedLoadName.error = "Connected Load Name field is required"
            Toast.makeText(
                this@AddLoadDetailsActivity,
                "Connected Load Name field is required",
                Toast.LENGTH_SHORT
            ).show()


        } else if (binding.connectedWatt.editText?.text.isNullOrEmpty()) {
                binding.connectedLoadName.error =null
                isValid = false
                binding.connectedWatt.error = "Connected Load Wattage (Watts) field is required"
            Toast.makeText(
                this@AddLoadDetailsActivity,
                "Connected Load Wattage (Watts) field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.connectedQty.editText?.text.isNullOrEmpty()) {
            binding.connectedWatt.error =null
            isValid = false
            binding.connectedQty.error = "Connected Load Quantity field is required"
            binding.connectedQty.requestFocus()
            Toast.makeText(
                this@AddLoadDetailsActivity,
                "Connected Load Quantity field is required",
                Toast.LENGTH_SHORT
            ).show()


        }else if (binding.duringDay.editText?.text.isNullOrEmpty()) {
            binding.connectedQty.error =null
            isValid = false
            binding.duringDay.error = "Operational Hours during DAY field is required"
            binding.duringDay.requestFocus()
            Toast.makeText(
                this@AddLoadDetailsActivity,
                "Operational Hours during DAY field is required",
                Toast.LENGTH_SHORT
            ).show()


        }else if (binding.duringNight.editText?.text.isNullOrEmpty()) {
            binding.duringDay.error =null
            isValid = false
            binding.duringNight.error = "Operational Hours during Night field is required"
            binding.duringNight.requestFocus()
            Toast.makeText(
                this@AddLoadDetailsActivity,
                "Operational Hours during Night field is required",
                Toast.LENGTH_SHORT
            ).show()


        }
        else{
            binding.duringNight.error =null
            isValid = true
        }


        return isValid
    }

    fun AddAuditApi(operationType:String){
        var userId:Int=SharePreference.getIntPref(this@AddLoadDetailsActivity,Constants.UserId)
        var userName:String?=SharePreference.getStringPref(this@AddLoadDetailsActivity,Constants.UserName)
        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,surveyIds,operationType,"Connected Load")

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
                    if(status){
                        startActivity(Intent(applicationContext, LoadDetailsActivity::class.java).putExtra("surveyId", surveyIds.toString()))
                    }
                }


            }
        })
    }

}