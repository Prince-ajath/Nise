package com.ncdc.nise.ui.connected.fragment

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
import androidx.lifecycle.get

import com.ncdc.nise.R
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.energyAssments.EnergyAssmentData
import com.ncdc.nise.data.model.healthrelatedinfo.HealthRelatedData
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.FragmentEnergyAssementsBinding
import com.ncdc.nise.interfaces.FragPostionInterface
import com.ncdc.nise.ui.connected.viewModel.EnergyAssmentViewModel
import com.ncdc.nise.ui.fragment.SpaceAvailabiltyFrag
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnergyAssementsFrag: Fragment() {


    lateinit var binding: FragmentEnergyAssementsBinding
    var listener: FragPostionInterface? = null
    lateinit var energyAssmentViewModel:EnergyAssmentViewModel
    var inputsAssementFrag:InputsAssementFrag=InputsAssementFrag()
    var isEdit:Boolean=false
    var isBackPressed=false
    var surveIdEdit:Int=0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEnergyAssementsBinding.inflate(inflater, container, false)
        val view = binding.root
        energyAssmentViewModel=ViewModelProvider(requireActivity()).get(EnergyAssmentViewModel::class.java)
        listener = activity as FragPostionInterface
        isEdit=SharePreference.getBooleanPref(requireActivity(),Constants.isEdit)
        surveIdEdit=SharePreference.getIntPref(requireActivity(),Constants.editSurveyId)
        isBackPressed=SharePreference.getBooleanPref(requireActivity(),Constants.isBackPressed)
        init()

        if(isEdit){
            getEnergyAssment(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId))
        }else if(isBackPressed){
            getEnergyAssment(SharePreference.getIntPref(requireActivity(),Constants.SurveyId))
        }else{
            binding.atFuelsUsed.setText("")
            binding.totalBuildArea.editText?.setText("")
            binding.totalNoBuilding.editText?.setText("")
            binding.totalNoFloors.editText?.setText("")
            binding.totalNoWindows.editText?.setText("")
            binding.totalNoOfAc.editText?.setText("")
            binding.totalInstalledAc.editText?.setText("")
            binding.estmetedHotWatar.editText?.setText("")
            binding.atThermostatic.setText("")
            binding.atFanSpeed.setText("")
            binding.atBulbsOlder5Years.setText("")
            binding.atEquipments.setText("")
            binding.atRoomsEnough.setText("")
            binding.atOccuSensors.setText("")
            binding.atElectEquipments.setText("")
            binding.atFreezers.setText("")
            binding.atInsulatedWell.setText("")
            binding.atLoadParrelol.setText("")
            binding.atEnergyUsage.setText("")
            binding.atEfficiency.setText("")
            binding.atCircuits.setText("")
            binding.atHumidity.setText("")
        }
        binding.tvNextEnergy.setOnClickListener {

            getEnergyAssApi()

        }
        return view

    }

    private fun init(){

        val fuelsUsedArray =  arrayListOf("Diesel","Natural Gas","Electricity")
        binding.atFuelsUsed.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, fuelsUsedArray))

        val yesNoArray =  arrayListOf("Yes","No")
        binding.atThermostatic.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atFanSpeed.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atBulbsOlder5Years.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atEquipments.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atRoomsEnough.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atOccuSensors.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        val equipmentArray =  arrayListOf("For almost all","For some","Very few")
        binding.atElectEquipments.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, equipmentArray))

        binding.atFreezers.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atInsulatedWell.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atLoadParrelol.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atEnergyUsage.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atEfficiency.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atCircuits.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        binding.atHumidity.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))


    }


    private fun getEnergyAssApi(){
        val isValid = validateForm()
        if (isValid) {
            binding.progressBar.visibility = View.VISIBLE
            binding.ProgressBar1.visibility=View.VISIBLE
            binding.tvNextEnergy.visibility=View.GONE

            if (isEdit) {
                energyAssmentViewModel.updateEnergyAssmentApi(
                    SharePreference.getIntPref(requireActivity(),Constants.editSurveyId),
                    binding.atFuelsUsed.text.toString(),
                    binding.totalBuildArea.editText?.text.toString(),
                    binding.totalNoBuilding.editText?.text.toString(),
                    binding.totalNoFloors.editText?.text.toString(),
                    binding.totalNoWindows.editText?.text.toString(),
                    binding.totalInstalledAc.editText?.text.toString(),
                    binding.estmetedHotWatar.editText?.text.toString(),
                    binding.atThermostatic.text.toString(),
                    binding.atFanSpeed.text.toString(),
                    binding.atBulbsOlder5Years.text.toString(),
                    binding.atEquipments.text.toString(),
                    binding.atRoomsEnough.text.toString(),
                    binding.atOccuSensors.text.toString(),
                    binding.atElectEquipments.text.toString(),
                    binding.atFreezers.text.toString(),
                    binding.atInsulatedWell.text.toString(),
                    binding.atLoadParrelol.text.toString(),
                    binding.atEnergyUsage.text.toString(),
                    binding.atEfficiency.text.toString(),
                    binding.atCircuits.text.toString(),
                    binding.atHumidity.text.toString()
                )
            } else if (isBackPressed) {
                energyAssmentViewModel.updateEnergyAssmentApi(
                    SharePreference.getIntPref(requireActivity(),Constants.SurveyId),
                    binding.atFuelsUsed.text.toString(),
                    binding.totalBuildArea.editText?.text.toString(),
                    binding.totalNoBuilding.editText?.text.toString(),
                    binding.totalNoFloors.editText?.text.toString(),
                    binding.totalNoWindows.editText?.text.toString(),
                    binding.totalInstalledAc.editText?.text.toString(),
                    binding.estmetedHotWatar.editText?.text.toString(),
                    binding.atThermostatic.text.toString(),
                    binding.atFanSpeed.text.toString(),
                    binding.atBulbsOlder5Years.text.toString(),
                    binding.atEquipments.text.toString(),
                    binding.atRoomsEnough.text.toString(),
                    binding.atOccuSensors.text.toString(),
                    binding.atElectEquipments.text.toString(),
                    binding.atFreezers.text.toString(),
                    binding.atInsulatedWell.text.toString(),
                    binding.atLoadParrelol.text.toString(),
                    binding.atEnergyUsage.text.toString(),
                    binding.atEfficiency.text.toString(),
                    binding.atCircuits.text.toString(),
                    binding.atHumidity.text.toString()
                )
            } else {
                energyAssmentViewModel.addEnergyAssmentApi(
                    SharePreference.getIntPref(requireActivity(), Constants.SurveyId),
                    binding.atFuelsUsed.text.toString(),
                    binding.totalBuildArea.editText?.text.toString(),
                    binding.totalNoBuilding.editText?.text.toString(),
                    binding.totalNoFloors.editText?.text.toString(),
                    binding.totalNoWindows.editText?.text.toString(),
                    binding.totalInstalledAc.editText?.text.toString(),
                    binding.estmetedHotWatar.editText?.text.toString(),
                    binding.atThermostatic.text.toString(),
                    binding.atFanSpeed.text.toString(),
                    binding.atBulbsOlder5Years.text.toString(),
                    binding.atEquipments.text.toString(),
                    binding.atRoomsEnough.text.toString(),
                    binding.atOccuSensors.text.toString(),
                    binding.atElectEquipments.text.toString(),
                    binding.atFreezers.text.toString(),
                    binding.atInsulatedWell.text.toString(),
                    binding.atLoadParrelol.text.toString(),
                    binding.atEnergyUsage.text.toString(),
                    binding.atEfficiency.text.toString(),
                    binding.atCircuits.text.toString(),
                    binding.atHumidity.text.toString()
                )
            }
            energyAssmentViewModel.servicesLiveData!!.observe(
                requireActivity(),
                Observer { EnergyAssResponse ->
                    var checkStaus: Boolean = EnergyAssResponse.status
                    binding.progressBar.visibility = View.GONE
                    if (checkStaus) {
                        if(isEdit){
                            var surveyId:Int=SharePreference.getIntPref(requireActivity(), Constants.editSurveyId)
                            AddAuditApi(surveyId,"Update")
                        } else if(isBackPressed){
                            AddAuditApi(SharePreference.getIntPref(requireActivity(), Constants.SurveyId),"Update")
                        }
                            else{
                            AddAuditApi(SharePreference.getIntPref(requireActivity(), Constants.SurveyId),"Insert")
                        }
                        if (!inputsAssementFrag.isAdded) {
                            listener?.onItemClick(7)
                            val fragmentTransaction =
                                requireActivity().supportFragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.frameLayout, inputsAssementFrag)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }


                    } else {
                        binding.ProgressBar1.visibility=View.GONE
                        binding.tvNextEnergy.visibility=View.VISIBLE
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

    fun getEnergyAssment(surveyId:Int){
        binding.progressBar.visibility = View.VISIBLE
        energyAssmentViewModel.getEnergyAssApi(surveyId)
        energyAssmentViewModel.loading!!.observe(requireActivity()) {
            if (it) {

                binding.progressBar.visibility = View.GONE
            }
        }
            energyAssmentViewModel.energyAssResponse!!.observe(
                requireActivity(),
                Observer { GetEnergyAssments ->
                    var checkStaus: Boolean = GetEnergyAssments.status

                    if (checkStaus) {
                        binding.progressBar.visibility = View.GONE
                        var waterData: EnergyAssmentData = GetEnergyAssments.data
                        binding.atFuelsUsed.setText(waterData.fuelsUsed, false)
                        binding.totalBuildArea.editText?.setText(waterData.totalBuiltUpArea)
                        binding.totalNoBuilding.editText?.setText(waterData.totalNoBuildings)
                        binding.totalNoFloors.editText?.setText(waterData.totalNoFloors)
                        binding.totalNoWindows.editText?.setText(waterData.totalNoWindows)
                        binding.totalInstalledAc.editText?.setText(waterData.totalInstallCapacity)
                        binding.estmetedHotWatar.editText?.setText(waterData.hotWaterConsumption)
                        binding.atThermostatic.setText(waterData.thermostatic, false)
                        binding.atFanSpeed.setText(waterData.fanSpeed, false)
                        binding.atBulbsOlder5Years.setText(waterData.incandescent, false)
                        binding.atEquipments.setText(waterData.equipments, false)
                        binding.atRoomsEnough.setText(waterData.rooms, false)
                        binding.atOccuSensors.setText(waterData.sensors, false)
                        binding.atElectEquipments.setText(waterData.labelling, false)
                        binding.atFreezers.setText(waterData.refrigerators, false)
                        binding.atInsulatedWell.setText(waterData.insulatedWell, false)
                        binding.atLoadParrelol.setText(waterData.lightingLoad, false)
                        binding.atEnergyUsage.setText(waterData.energyUsage, false)
                        binding.atEfficiency.setText(waterData.energyEfficiency, false)
                        binding.atCircuits.setText(waterData.lightingCircuits, false)
                        binding.atHumidity.setText(waterData.humidity, false)


                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.atFuelsUsed.setText("")
                        binding.totalBuildArea.editText?.setText("")
                        binding.totalNoBuilding.editText?.setText("")
                        binding.totalNoFloors.editText?.setText("")
                        binding.totalNoWindows.editText?.setText("")
                        binding.totalInstalledAc.editText?.setText("")
                        binding.estmetedHotWatar.editText?.setText("")
                        binding.atThermostatic.setText("")
                        binding.atFanSpeed.setText("")
                        binding.atBulbsOlder5Years.setText("")
                        binding.atEquipments.setText("")
                        binding.atRoomsEnough.setText("")
                        binding.atOccuSensors.setText("")
                        binding.atElectEquipments.setText("")
                        binding.atFreezers.setText("")
                        binding.atInsulatedWell.setText("")
                        binding.atLoadParrelol.setText("")
                        binding.atEnergyUsage.setText("")
                        binding.atEfficiency.setText("")
                        binding.atCircuits.setText("")
                        binding.atHumidity.setText("")
                    }
                })

    }

    private fun validateForm(): Boolean {
        var isValid = false

        if(binding.atFuelsUsed.text.toString().isEmpty()){
            binding.atFuelsUsed.error="Fuels used for generating steam field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Fuels used for generating steam field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.totalBuildArea.editText!!.text.toString().isEmpty()){
            binding.atFuelsUsed.error=null
            binding.totalBuildArea.error="Total Built up area field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Total Built up area field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.totalNoBuilding.editText!!.text.toString().isEmpty()){
            binding.totalBuildArea.error=null
            binding.totalNoBuilding.error="Total number of buildings field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Total number of buildings field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.totalNoFloors.editText!!.text.toString().isEmpty()){
            binding.totalNoBuilding.error=null
            binding.totalNoFloors.error="Total number of floors field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Total number of floors field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.totalNoWindows.editText!!.text.toString().isEmpty()){
            binding.totalNoFloors.error=null
            binding.totalNoWindows.error="Total number of windows field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Total number of windows field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.totalInstalledAc.editText!!.text.toString().isEmpty()){
            binding.totalNoWindows.error=null
            binding.totalInstalledAc.error="Total Installed capacity of AC field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Total Installed capacity of AC field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.estmetedHotWatar.editText!!.text.toString().isEmpty()){
            binding.totalInstalledAc.error=null
            binding.estmetedHotWatar.error="Estimated hot water field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Estimated hot water field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atThermostatic.text.toString().isEmpty()){
            binding.estmetedHotWatar.error=null
            binding.atThermostatic.error="Thermostatic radiator field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Thermostatic radiator field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atFanSpeed.text.toString().isEmpty()){
            binding.atThermostatic.error=null
            binding.atFanSpeed.error="Controlling the fan speed field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Controlling the fan speed field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atBulbsOlder5Years.text.toString().isEmpty()){
            binding.atFanSpeed.error=null
            binding.atBulbsOlder5Years.error="Are there incandescent bulbs field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Are there incandescent bulbs field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atEquipments.text.toString().isEmpty()){
            binding.atBulbsOlder5Years.error=null
            binding.atEquipments.error="Electrical Equipments field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Electrical Equipments field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atRoomsEnough.text.toString().isEmpty()){
            binding.atEquipments.error=null
            binding.atRoomsEnough.error="Does the rooms have enough field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Does the rooms have enough field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atOccuSensors.text.toString().isEmpty()){
            binding.atRoomsEnough.error=null
            binding.atOccuSensors.error="Occupancy sensors field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Occupancy sensors field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atElectEquipments.text.toString().isEmpty()){
            binding.atOccuSensors.error=null
            binding.atElectEquipments.error="Labelling on the electrical Equipments field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Labelling on the electrical Equipments field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atFreezers.text.toString().isEmpty()){
            binding.atElectEquipments.error=null
            binding.atFreezers.error="Freezers carried out field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Freezers carried out field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atInsulatedWell.text.toString().isEmpty()){
            binding.atFreezers.error=null
            binding.atInsulatedWell.error="Insulated well field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Insulated well field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atLoadParrelol.text.toString().isEmpty()){
            binding.atInsulatedWell.error=null
            binding.atLoadParrelol.error="Lighting load field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Lighting load field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atEnergyUsage.text.toString().isEmpty()){
            binding.atLoadParrelol.error=null
            binding.atEnergyUsage.error="Energy usage field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Energy usage field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atEfficiency.text.toString().isEmpty()){
            binding.atEnergyUsage.error=null
            binding.atEfficiency.error="Energy efficiency field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Energy efficiency field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atCircuits.text.toString().isEmpty()){
            binding.atEfficiency.error=null
            binding.atCircuits.error="Lighting circuits field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Lighting circuits field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if(binding.atHumidity.text.toString().isEmpty()){
            binding.atCircuits.error=null
            binding.atHumidity.error="Humidity field is required"
            isValid=false
            Toast.makeText(
                requireActivity(),
                "Humidity field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else{
            binding.atHumidity.error=null
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
                }


            }
        })
    }


}