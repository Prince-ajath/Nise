package com.ncdc.nise.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ncdc.nise.R
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.WaterSourceData
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.FragmentWaterSourceBinding
import com.ncdc.nise.interfaces.FragPostionInterface
import com.ncdc.nise.ui.viewmodel.WaterDetailsViewModel
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WaterSourceFrag:Fragment() {

    lateinit var binding: FragmentWaterSourceBinding
    var listener: FragPostionInterface? = null
    lateinit var waterDetailsViewModel:WaterDetailsViewModel
    var vaccineStorageFrag:VaccineStorageFrag=VaccineStorageFrag()
    var isEdit:Boolean =false
    var isBackPressed=false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaterSourceBinding.inflate(inflater, container, false)
        val view = binding.root
        waterDetailsViewModel=ViewModelProvider(requireActivity()).get(WaterDetailsViewModel::class.java)
        isEdit=SharePreference.getBooleanPref(requireActivity(),Constants.isEdit)
        isBackPressed=SharePreference.getBooleanPref(requireActivity(),Constants.isBackPressed)
        init()
        listener= activity as FragPostionInterface
        if(isEdit){
            getWaterDetailsApi(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId))
        }else if(isBackPressed){
            getWaterDetailsApi(SharePreference.getIntPref(requireActivity(),Constants.SurveyId))
        } else{
            binding.atSourceWater.setText("")
            binding.capOfStTank.editText?.setText("")
            binding.atPumping.setText("")
            binding.atWattage.editText?.setText("")
            binding.atOperational.editText?.setText("")
            binding.qtyWaterReq.editText?.setText("")
            binding.qtyWaterAvialable.editText?.setText("")
            binding.depthWatertTable.editText?.setText("")
            binding.distWaterSourceTank.editText?.setText("")
            binding.atAvialable.setText("")
        }




        binding.tvNextWaterSource.setOnClickListener {

            validateAndLogin()

        }


        return view


    }

    private fun init(){
        val wtrSorsArray = arrayListOf("Bore well","Open Well","Municipal Water")
        binding.atSourceWater.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, wtrSorsArray))

        val pumpingArray = arrayListOf("Moter Pump set","Hand Pump set")
        binding.atPumping.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, pumpingArray))


        val waterAvlArray = arrayListOf("Yes","No")
        binding.atAvialable.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, waterAvlArray))
    }


    private fun validateAndLogin() {
        val isValid = validateForm()
        if (isValid) {
            binding.progressBar.visibility = View.VISIBLE
            binding.ProgressBar1.visibility=View.VISIBLE
            binding.tvNextWaterSource.visibility=View.GONE

            if(isEdit){
                waterDetailsViewModel.updateWaterDetails(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId),binding.atSourceWater.text.toString(),binding.capOfStTank.editText?.text.toString(),binding.atPumping.text.toString(),binding.atWattage.editText?.text.toString(),binding.atOperational.editText?.text.toString(),
                    binding.qtyWaterReq.editText?.text.toString(),binding.qtyWaterAvialable.editText?.text.toString(),binding.depthWatertTable.editText?.text.toString(),binding.distWaterSourceTank.editText?.text.toString(),
                    binding.atAvialable.text.toString())
            }else if(isBackPressed){
                waterDetailsViewModel.updateWaterDetails(SharePreference.getIntPref(requireActivity(),Constants.SurveyId),binding.atSourceWater.text.toString(),binding.capOfStTank.editText?.text.toString(),binding.atPumping.text.toString(),binding.atWattage.editText?.text.toString(),binding.atOperational.editText?.text.toString(),
                    binding.qtyWaterReq.editText?.text.toString(),binding.qtyWaterAvialable.editText?.text.toString(),binding.depthWatertTable.editText?.text.toString(),binding.distWaterSourceTank.editText?.text.toString(),
                    binding.atAvialable.text.toString())
            }
            else{
                waterDetailsViewModel.addWaterDetails(SharePreference.getIntPref(requireActivity(),Constants.SurveyId),binding.atSourceWater.text.toString(),binding.capOfStTank.editText?.text.toString(),binding.atPumping.text.toString(),binding.atWattage.editText?.text.toString(),binding.atOperational.editText?.text.toString(),
                    binding.qtyWaterReq.editText?.text.toString(),binding.qtyWaterAvialable.editText?.text.toString(),binding.depthWatertTable.editText?.text.toString(),binding.distWaterSourceTank.editText?.text.toString(),
                    binding.atAvialable.text.toString(),)
            }

            waterDetailsViewModel.servicesLiveData!!.observe(requireActivity(), Observer { SurveyorResponse->
                var checkStaus:Boolean= SurveyorResponse.status
                binding.progressBar.visibility =View.GONE
                if(checkStaus){
                    binding.progressBar.visibility =View.GONE
                    if(isEdit){
                        var surveyId:Int=SharePreference.getIntPref(requireActivity(), Constants.editSurveyId)
                        AddAuditApi(surveyId,"Update")
                    } else if(isBackPressed){
                        AddAuditApi(SharePreference.getIntPref(requireActivity(), Constants.SurveyId),"Update")
                    }
                    else{
                        AddAuditApi(SharePreference.getIntPref(requireActivity(), Constants.SurveyId),"Insert")
                    }
                    if(!vaccineStorageFrag.isAdded){
                        listener?.onItemClick(4)
                        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frameLayout,vaccineStorageFrag )
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }

                }else{
                    binding.ProgressBar1.visibility=View.GONE
                    binding.tvNextWaterSource.visibility=View.VISIBLE
                    binding.progressBar.visibility =View.GONE
                    Toast.makeText(requireActivity(),""+SurveyorResponse.message, Toast.LENGTH_SHORT).show()
                }
            })



        }
    }

    private fun getWaterDetailsApi(surveyId:Int){
        binding.progressBar.visibility = View.VISIBLE
        waterDetailsViewModel.getWaterDetails(surveyId)
        waterDetailsViewModel.loading!!.observe(requireActivity()) {
            if (it) {
                binding.progressBar.visibility = View.GONE
            }
        }

            waterDetailsViewModel.waterResoponse!!.observe(
                requireActivity(),
                Observer { GetWaterSourceResponse ->
                    var checkStaus: Boolean = GetWaterSourceResponse.status

                    if (checkStaus) {
                        binding.progressBar.visibility = View.GONE
                        var waterData: WaterSourceData = GetWaterSourceResponse.data
                        binding.atSourceWater.setText(waterData.waterDrinking, false)
                        binding.capOfStTank.editText?.setText(waterData.waterStorage.toString())
                        binding.atPumping.setText(waterData.pumpingMechanism, false)
                        binding.atWattage.editText?.setText(waterData.motorPumpSet)
                        binding.atOperational.editText?.setText((waterData.operationalHours))
                        binding.qtyWaterReq.editText?.setText(waterData.qwRequired.toString())
                        binding.qtyWaterAvialable.editText?.setText(waterData.qualityWater.toString())
                        binding.depthWatertTable.editText?.setText(waterData.waterTable.toString())
                        binding.distWaterSourceTank.editText?.setText(waterData.waterTank.toString())
                        binding.atAvialable.setText(waterData.avilableWater,false)


                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.atSourceWater.setText("")
                        binding.capOfStTank.editText?.setText("")
                        binding.atPumping.setText("")
                        binding.atWattage.editText?.setText("")
                        binding.atOperational.editText?.setText("")
                        binding.qtyWaterReq.editText?.setText("")
                        binding.qtyWaterAvialable.editText?.setText("")
                        binding.depthWatertTable.editText?.setText("")
                        binding.distWaterSourceTank.editText?.setText("")
                        binding.atAvialable.setText("")
                    }
                })


    }

    private fun validateForm(): Boolean {
        var isValid = false


        if (binding.atSourceWater.text.isNullOrEmpty()) {
            isValid = false
            binding.atSourceWater.error = "Source of water for drinking and other purposes is required"
            Toast.makeText(
                requireActivity(),
                "Source of water for drinking and other purposes is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atPumping.text.isNullOrEmpty()) {
            binding.capOfStTank.error =null
            isValid = false
            binding.atPumping.error = "Pumping mechanism is required"
            Toast.makeText(
                requireActivity(),
                "Pumping mechanism is required",
                Toast.LENGTH_SHORT
            ).show()

        }else if (binding.atWattage.editText?.text.isNullOrEmpty()) {
            binding.atPumping.error =null
            isValid = false
            binding.atWattage.error = "Capacity,and pattern of operation is required"
            Toast.makeText(
                requireActivity(),
                "Capacity,and pattern of operation is required",
                Toast.LENGTH_SHORT
            ).show()

        }else if (binding.qtyWaterReq.editText?.text.isNullOrEmpty()) {
            binding.atWattage.error =null
            isValid = false
            binding.qtyWaterReq.error = "Quantity of water is required"
            Toast.makeText(
                requireActivity(),
                "Quantity of water is required",
                Toast.LENGTH_SHORT
            ).show()
        } else{

            binding.qtyWaterAvialable.error =null
            isValid = true
        }

        return isValid
    }

    fun AddAuditApi(surveyId:Int,operationType:String){
        var userId:Int=SharePreference.getIntPref(requireActivity(),Constants.UserId)
        var userName:String?=SharePreference.getStringPref(requireActivity(),Constants.UserName)

        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,surveyId,operationType,"Water Source")

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