package com.ncdc.nise.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ncdc.nise.R
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.spaceavilability.SpaceAvilableData
import com.ncdc.nise.data.model.vaccineStorage.VaccineData
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.FragmentVaccineStorageBinding

import com.ncdc.nise.interfaces.FragPostionInterface
import com.ncdc.nise.ui.viewmodel.VaccineStorageViewModel
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VaccineStorageFrag:Fragment(){


    lateinit var binding: FragmentVaccineStorageBinding
    var listener: FragPostionInterface? = null
    lateinit var vaccineStorageViewModel:VaccineStorageViewModel
    var healthRelatedInfoFrag:HealthRelatedInfoFrag=HealthRelatedInfoFrag()
    var isEdit:Boolean =false
    var isBackPressed=false
    var isChecked=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentVaccineStorageBinding.inflate(inflater, container, false)
        val view = binding.root
        vaccineStorageViewModel=ViewModelProvider(requireActivity()).get(VaccineStorageViewModel::class.java)
        isEdit=SharePreference.getBooleanPref(requireActivity(),Constants.isEdit)
        isBackPressed=SharePreference.getBooleanPref(requireActivity(),Constants.isBackPressed)
        init()
        listener= activity as FragPostionInterface

       if(isEdit){
           getVaccineStorageApi(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId))
       }else if(isBackPressed){
           getVaccineStorageApi(SharePreference.getIntPref(requireActivity(),Constants.SurveyId))
       }else{
           binding.atWeatherVaccine.setText("")
           binding.atWeatherWorking.setText("")
           binding.atWSPower.setText("")
           binding.tempMaintain.editText?.setText("")
           binding.usePattern.editText?.setText("")

       }

        binding.tvNextVaccineStorage.setOnClickListener {
            validateAndLogin()

        }


        return view


    }

    private fun init(){
        val yesNoArray = arrayListOf("Yes","No")
        binding.atWeatherVaccine.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        val workingArray = arrayListOf("Working","Not Working")
        binding.atWeatherWorking.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, workingArray))

        binding.atWSPower.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        if(binding.atWeatherVaccine.text.equals("Yes")){

            binding.llView.visibility=View.VISIBLE
            isChecked=true
        }else if(binding.atWeatherVaccine.text.equals("No")){
            isChecked=false
            binding.llView.visibility=View.GONE
        }else{
            isChecked=false
            binding.llView.visibility=View.GONE
        }

        binding.atWeatherVaccine.addTextChangedListener(textWatcher)


    }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(s.toString().equals("Yes")){
                binding.llView.visibility=View.VISIBLE
                isChecked=true
            }else if(s.toString().equals("No")){
                isChecked=false
                binding.llView.visibility=View.GONE
            }else{
                isChecked=false
                binding.llView.visibility=View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(binding.atWeatherVaccine.text.equals("Yes")){

            binding.llView.visibility=View.VISIBLE
            isChecked=true
        }else if(binding.atWeatherVaccine.text.equals("No")){
            isChecked=false
            binding.llView.visibility=View.GONE
        }else{
            isChecked=false
            binding.llView.visibility=View.GONE
        }
    }

    private fun validateAndLogin() {
        val isValid = validateForm()
        if (isValid) {
            binding.progressBar.visibility = View.VISIBLE
            binding.ProgressBar1.visibility=View.VISIBLE
            binding.tvNextVaccineStorage.visibility=View.GONE
            if(isEdit){
                vaccineStorageViewModel.updateVaccineStorage(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId),binding.atWeatherVaccine.text.toString(),binding.atWeatherWorking.text.toString(),binding.atWSPower.text.toString(),
                    binding.tempMaintain.editText?.text.toString(),binding.usePattern.editText?.text.toString())
            }else if(isBackPressed){
                vaccineStorageViewModel.updateVaccineStorage(SharePreference.getIntPref(requireActivity(),Constants.SurveyId),binding.atWeatherVaccine.text.toString(),binding.atWeatherWorking.text.toString(),binding.atWSPower.text.toString(),
                    binding.tempMaintain.editText?.text.toString(),binding.usePattern.editText?.text.toString())
            }
            else{
                vaccineStorageViewModel.addVaccineStorage(SharePreference.getIntPref(requireActivity(),Constants.SurveyId),binding.atWeatherVaccine.text.toString(),binding.atWeatherWorking.text.toString(),binding.atWSPower.text.toString(),
                    binding.tempMaintain.editText?.text.toString(),binding.usePattern.editText?.text.toString())
            }

            vaccineStorageViewModel.servicesLiveData!!.observe(requireActivity(), Observer { SurveyorResponse->
                var checkStaus:Boolean=SurveyorResponse.status
                binding.progressBar.visibility =View.GONE

                if(checkStaus){
                    if(isEdit){
                        var surveyId:Int=SharePreference.getIntPref(requireActivity(), Constants.editSurveyId)
                        AddAuditApi(surveyId,"Update")
                    } else if(isBackPressed){
                        AddAuditApi(SharePreference.getIntPref(requireActivity(), Constants.SurveyId),"Update")
                    }
                    else{
                        AddAuditApi(SharePreference.getIntPref(requireActivity(), Constants.SurveyId),"Insert")
                    }
                   if(!healthRelatedInfoFrag.isAdded){
                       listener?.onItemClick(5)
                       val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                       fragmentTransaction.replace(R.id.frameLayout, healthRelatedInfoFrag)
                       fragmentTransaction.addToBackStack(null)
                       fragmentTransaction.commit()
                   }
                }else{
                    binding.ProgressBar1.visibility=View.GONE
                    binding.tvNextVaccineStorage.visibility=View.VISIBLE
                    binding.progressBar.visibility =View.GONE

                }
            })



        }
    }

    private fun validateForm(): Boolean {
        var isValid = false
        if(isChecked){
            if (binding.atWeatherVaccine.text.isNullOrEmpty()) {
                isValid = false
                binding.atWeatherVaccine.error = "Whether Vaccine storage Refrigerator is required"
                Toast.makeText(
                    requireActivity(),
                    "Whether Vaccine storage Refrigerator is required",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (binding.atWeatherWorking.text.isNullOrEmpty()) {
                binding.atWeatherVaccine.error =null
                isValid = false
                binding.atWeatherWorking.error = "Whether Vaccine storage Refrigerator working is required"
                Toast.makeText(
                    requireActivity(),
                    "Whether Vaccine storage Refrigerator working is required",
                    Toast.LENGTH_SHORT
                ).show()

            }else if (binding.atWSPower.text.isNullOrEmpty()) {
                binding.atWeatherWorking.error =null
                isValid = false
                binding.atWSPower.error = "Whether the Refrigerator is solar powered is required"
                Toast.makeText(
                    requireActivity(),
                    "Whether the Refrigerator is solar powered is required",
                    Toast.LENGTH_SHORT
                ).show()

            }else if (binding.tempMaintain.editText?.text.isNullOrEmpty()) {
                binding.atWSPower.error =null
                isValid = false
                binding.tempMaintain.error = "Temperature maintained is required"
                Toast.makeText(
                    requireActivity(),
                    "Temperature maintained is required",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.usePattern.editText?.text.isNullOrEmpty()) {
                binding.tempMaintain.error =null
                isValid = false
                binding.usePattern.error = "Usage pattern field is required"
                Toast.makeText(
                    requireActivity(),
                    "Usage pattern field is required",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                binding.usePattern.error =null
                isValid = true
            }
        }else{
            if (binding.atWeatherVaccine.text.isNullOrEmpty()) {
                isValid = false
                binding.atWeatherVaccine.error = "Whether Vaccine storage Refrigerator is required"
                Toast.makeText(
                    requireActivity(),
                    "Whether Vaccine storage Refrigerator is required",
                    Toast.LENGTH_SHORT
                ).show()

            } else{
                binding.usePattern.error =null
                isValid = true
            }
        }


        return isValid
    }
    private fun getVaccineStorageApi(surveyIds:Int){
        binding.progressBar.visibility = View.VISIBLE
        vaccineStorageViewModel.getVaccineStorageApi(surveyIds)
        vaccineStorageViewModel.loading!!.observe(requireActivity()) {
            if (it) {
                binding.progressBar.visibility = View.GONE
            }
        }

            vaccineStorageViewModel.vaccineStorageResponse!!.observe(
                requireActivity(),
                Observer { GetVaccineStorageResponse ->
                    var checkStaus: Boolean = GetVaccineStorageResponse.status

                    if (checkStaus) {
                        binding.progressBar.visibility = View.GONE
                        var data: VaccineData = GetVaccineStorageResponse.data
                        binding.atWeatherVaccine.setText(data.refrigeAvailable, false)
                        binding.atWeatherWorking.setText(data.refrigeWorking, false)
                        binding.atWSPower.setText(data.solarPowered, false)
                        binding.tempMaintain.editText?.setText(data.tempMaintained)
                        binding.usePattern.editText?.setText(data.usagePattern)


                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.atWeatherVaccine.setText("")
                        binding.atWeatherWorking.setText("")
                        binding.atWSPower.setText("")
                        binding.tempMaintain.editText?.setText("")
                        binding.usePattern.editText?.setText("")


                    }
                })
    }

    fun AddAuditApi(surveyId:Int,operationType:String){
        var userId:Int=SharePreference.getIntPref(requireActivity(),Constants.UserId)
        var userName:String?=SharePreference.getStringPref(requireActivity(),Constants.UserName)

        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,surveyId,operationType,"Vaccine Storage")

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