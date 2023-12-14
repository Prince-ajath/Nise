package com.ncdc.nise.ui.connected.fragment

import android.content.Intent
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
import com.ncdc.nise.data.model.energyAssments.EnergyAssmentData
import com.ncdc.nise.data.model.inputsAssments.InputAssmentsData
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.FragmentInputAssementBinding
import com.ncdc.nise.interfaces.FragPostionInterface
import com.ncdc.nise.ui.connected.viewModel.InputAssmentViewModel
import com.ncdc.nise.ui.survey.presentation.add.ViewSurveyorActivity
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputsAssementFrag: Fragment() {

    lateinit var binding:FragmentInputAssementBinding
    var listener: FragPostionInterface? = null
    lateinit var inputsAssViewModel:InputAssmentViewModel
    var isEdit:Boolean=false
    var isBackPressed=false
    var surveIdEdit:Int=0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInputAssementBinding.inflate(inflater, container, false)
        val view = binding.root
        inputsAssViewModel= ViewModelProvider(requireActivity()).get(InputAssmentViewModel::class.java)
        isEdit=SharePreference.getBooleanPref(requireActivity(),Constants.isEdit)
        surveIdEdit=SharePreference.getIntPref(requireActivity(),Constants.editSurveyId)
        isBackPressed=SharePreference.getBooleanPref(requireActivity(),Constants.isBackPressed)
        init()
        listener = activity as FragPostionInterface
        if(isEdit){
            getInputAssmentApi(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId))
        }else if(isBackPressed){
            getInputAssmentApi(SharePreference.getIntPref(requireActivity(),Constants.SurveyId))
        }else{
            binding.oldWaterPump.editText?.setText("")
            binding.etCarbonFeatures.setText("")
        }

        binding.tvNextEnergy2.setOnClickListener {
            addDataApi()

        }
        return view

    }
    private fun init(){

        val yesNoArray =  arrayListOf("Yes","No")
        binding.atElectricalLoads.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atHcfMonitor.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atHospital.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atCentralPlant.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atLowEnergy.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atInstallLighting.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atEfficientEquipment.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atEducateStaff.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        val cbFeatures= arrayListOf("Enhanced building thermal envelope","Reflective roofing","Open able windows","Shading by overhangs or planting","Use of local materials","Low-flow water fixtures",
            "Solar water heating","Energy labelling Equipments","Implement sleep mode on computer equipment","Automatic Turning off lighting and equipment when not in use","Energy management systems")

       binding.etCarbonFeatures.setAdapter(ArrayAdapter(requireActivity(),R.layout.list_item,cbFeatures))
    }

    private fun addDataApi(){
        val isValid = validateForm()
        if (isValid) {

            binding.progressBar.visibility = View.VISIBLE
            binding.ProgressBar1.visibility=View.VISIBLE
            binding.tvNextEnergy2.visibility=View.GONE

            if (isEdit) {
                inputsAssViewModel.updateInputAssmentApi(
                    SharePreference.getIntPref(requireActivity(), Constants.editSurveyId),
                    binding.rBFreezeRating.rating.toString(),
                    binding.rBACRating.rating.toString(),
                    binding.oldWaterPump.editText?.text.toString(),
                    binding.atElectricalLoads.text.toString(),
                    binding.atHcfMonitor.text.toString(),
                    binding.atHospital.text.toString(),
                    binding.atCentralPlant.text.toString(),
                    binding.atLowEnergy.text.toString(),
                    binding.atInstallLighting.text.toString(),
                    binding.atEfficientEquipment.text.toString(),
                    binding.atEducateStaff.text.toString(),
                    binding.etCarbonFeatures.text.toString(),
                    binding.etComments.editText?.text.toString()
                )
            } else if (isBackPressed) {
                inputsAssViewModel.updateInputAssmentApi(
                    SharePreference.getIntPref(requireActivity(), Constants.SurveyId),
                    binding.rBFreezeRating.rating.toString(),
                    binding.rBACRating.rating.toString(),
                    binding.oldWaterPump.editText?.text.toString(),
                    binding.atElectricalLoads.text.toString(),
                    binding.atHcfMonitor.text.toString(),
                    binding.atHospital.text.toString(),
                    binding.atCentralPlant.text.toString(),
                    binding.atLowEnergy.text.toString(),
                    binding.atInstallLighting.text.toString(),
                    binding.atEfficientEquipment.text.toString(),
                    binding.atEducateStaff.text.toString(),
                    binding.etCarbonFeatures.text.toString(),
                    binding.etComments.editText?.text.toString()

                )
            } else {
                inputsAssViewModel.addInputAssmentApi(
                    SharePreference.getIntPref(requireActivity(), Constants.SurveyId),
                    binding.rBFreezeRating.rating.toString(),
                    binding.rBACRating.rating.toString(),
                    binding.oldWaterPump.editText?.text.toString(),
                    binding.atElectricalLoads.text.toString(),
                    binding.atHcfMonitor.text.toString(),
                    binding.atHospital.text.toString(),
                    binding.atCentralPlant.text.toString(),
                    binding.atLowEnergy.text.toString(),
                    binding.atInstallLighting.text.toString(),
                    binding.atEfficientEquipment.text.toString(),
                    binding.atEducateStaff.text.toString(),
                    binding.etCarbonFeatures.text.toString(),
                    binding.etComments.editText?.text.toString()
                )
            }
            inputsAssViewModel.servicesLiveData!!.observe(
                requireActivity(),
                Observer { EnergyAssResponse ->
                    var checkStaus: Boolean = EnergyAssResponse.status

                    if (checkStaus) {
                        binding.progressBar.visibility = View.GONE
                        if(isEdit){
                            var surveyId:Int=SharePreference.getIntPref(requireActivity(), Constants.editSurveyId)
                            AddAuditApi(surveyId,"Update")
                        } else if(isBackPressed){
                            AddAuditApi(SharePreference.getIntPref(requireActivity(), Constants.SurveyId),"Update")
                        }
                        else{
                            AddAuditApi(SharePreference.getIntPref(requireActivity(), Constants.SurveyId),"Insert")
                        }

                    } else {
                        binding.ProgressBar1.visibility=View.GONE
                        binding.tvNextEnergy2.visibility=View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireActivity(),
                            "" + EnergyAssResponse.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    fun getInputAssmentApi(surveyId:Int){
        binding.progressBar.visibility = View.VISIBLE

        inputsAssViewModel.getInputAssApi(surveyId)
        inputsAssViewModel.loading!!.observe(requireActivity()) {
            if (it) {

                binding.progressBar.visibility = View.GONE
            }
        }

            inputsAssViewModel.inputAssResponse!!.observe(
                requireActivity(),
                Observer { GetInputAssments ->
                    var checkStaus: Boolean = GetInputAssments.status

                    if (checkStaus) {
                        binding.progressBar.visibility = View.GONE
                        var waterData: InputAssmentsData = GetInputAssments.data

                        binding.oldWaterPump.editText?.setText(waterData.waterPump)
                        binding.atElectricalLoads.setText(waterData.electricalLoads, false)
                        binding.atHcfMonitor.setText(waterData.hcf, false)
                        binding.atHospital.setText(waterData.conservation, false)
                        binding.atCentralPlant.setText(waterData.mequipment, false)
                        binding.atLowEnergy.setText(waterData.lowEnergy, false)
                        binding.atInstallLighting.setText(waterData.lightingControl,false)
                        binding.atEfficientEquipment.setText(waterData.energyEfficient, false)
                        binding.atEducateStaff.setText(waterData.strategies, false)
                        binding.etCarbonFeatures.setText(waterData.carbonFeatures)


                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.oldWaterPump.editText?.setText("")
                        binding.atElectricalLoads.setText("")
                        binding.atHcfMonitor.setText("")
                        binding.atHospital.setText("")
                        binding.atCentralPlant.setText("")
                        binding.atLowEnergy.setText("")
                        binding.atInstallLighting.setText("")
                        binding.atEfficientEquipment.setText("")
                        binding.atEducateStaff.setText("")
                        binding.etCarbonFeatures.setText("")
                    }
                })

    }
    private fun validateForm(): Boolean {
        var isValid = false

        if(binding.atElectricalLoads.text.toString().isEmpty()){
            binding.oldWaterPump.error=null
            binding.atElectricalLoads.error="Electrical Load filed is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Electrical Load filed is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atHcfMonitor.text.toString().isEmpty()){
            binding.oldWaterPump.error=null
            binding.atHcfMonitor.error="HCF monitor filed is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "HCF monitor filed is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atHospital.text.toString().isEmpty()){
            binding.atHcfMonitor.error=null
            binding.atHospital.error="Hospital have an energy conservation filed is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Hospital have an energy conservation filed is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atCentralPlant.text.toString().isEmpty()){
            binding.atHospital.error=null
            binding.atCentralPlant.error="Central plant filed is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Central plant filed is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atLowEnergy.text.toString().isEmpty()){
            binding.atCentralPlant.error=null
            binding.atLowEnergy.error="Low-energy lighting filed is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Low-energy lighting filed is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atInstallLighting.text.toString().isEmpty()){
            binding.atLowEnergy.error=null
            binding.atInstallLighting.error="Install lighting control filed is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Install lighting control filed is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atEfficientEquipment.text.toString().isEmpty()){
            binding.atInstallLighting.error=null
            binding.atEfficientEquipment.error="Install energy efficient filed is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Install energy efficient filed is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atEducateStaff.text.toString().isEmpty()){
            binding.atEfficientEquipment.error=null
            binding.atEducateStaff.error="Educate staff filed is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Educate staff filed is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.etCarbonFeatures.text.toString().isEmpty()){
            binding.atEducateStaff.error=null
            binding.etCarbonFeatures.error="Low carbon features filed is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Low carbon features filed is required",
                Toast.LENGTH_SHORT
            ).show()
        }else{
            binding.etCarbonFeatures.error=null
            isValid=true
        }

        return  isValid
    }

    fun AddAuditApi(surveyId:Int,operationType:String){
        var userId:Int=SharePreference.getIntPref(requireActivity(),Constants.UserId)
        var userName:String?=SharePreference.getStringPref(requireActivity(),Constants.UserName)

        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,surveyId,operationType,"Inputs for Energy Assessment")

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
                        startActivity(Intent(requireActivity(), ViewSurveyorActivity::class.java))
                        requireActivity().overridePendingTransition(0, 0)
                        requireActivity().finish()
                    }
                }


            }
        })
    }


}