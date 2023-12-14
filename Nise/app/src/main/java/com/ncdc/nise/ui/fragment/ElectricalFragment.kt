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
import com.ncdc.nise.data.model.electrical.ElectricalData
import com.ncdc.nise.data.model.health.HealthData
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.FragmentElectricalBinding
import com.ncdc.nise.interfaces.Backpressedlistener
import com.ncdc.nise.interfaces.FragPostionInterface
import com.ncdc.nise.ui.viewmodel.ElectricalViewModel
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class ElectricalFragment:Fragment() {


    lateinit var binding: FragmentElectricalBinding
    var listener: FragPostionInterface? = null
    lateinit var electricalViewModel: ElectricalViewModel
    var spaceAvailabiltyFrag:SpaceAvailabiltyFrag=SpaceAvailabiltyFrag()
    var isEdit:Boolean=false
    var isBackPressed=false
    var surveIdEdit:Int=0;
    var avgPowerValue:Double=1.0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectricalBinding.inflate(inflater, container, false)
        val view = binding.root
        electricalViewModel= ViewModelProvider(requireActivity()).get(ElectricalViewModel::class.java)

        isEdit=SharePreference.getBooleanPref(requireActivity(),Constants.isEdit)
        isBackPressed=SharePreference.getBooleanPref(requireActivity(),Constants.isBackPressed)

        surveIdEdit=SharePreference.getIntPref(requireActivity(),Constants.editSurveyId)
        init()
        listener = activity as FragPostionInterface

        if(isEdit){
            getElectricalApi(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId))
        }else if(isBackPressed){
            getElectricalApi(SharePreference.getIntPref(requireActivity(),Constants.SurveyId))
        }else{
            binding.atElectricityBill.setText("")
            binding.atElectcityConnection.setText("")
            binding.sectionedLoad.editText?.setText("")
            binding.peakLoad.editText?.setText("")
            binding.atTypeConnection.setText("")
            binding.averageElectricity.editText?.setText("")
            binding.atMainLine.setText("")
            binding.atVoltage.setText("")
            binding.averagePowerOutage.editText?.setText("")
            binding.atPSourceSupply.setText("")
            binding.atSSourceSupply.setText("")
            binding.atMeterConnection.setText("")
            binding.atACVoltage.setText("")
            binding.atScopeLoads.setText("")
            binding.etNoOutDoorLighting.editText?.setText("")
            binding.atSolarSystem.setText("")
            binding.etElectConsumption.editText?.setText("")
            binding.atProvisions.setText("")
            binding.atScopeAdoption.setText("")
            binding.etAvgPowerFact.setText("")
            binding.atDistribBoards.editText?.setText("")
            binding.atmcb5to10.editText?.setText("")
            binding.atmcb5to10.editText?.setText("")
            binding.atMCBGreter20.editText?.setText("")
            binding.atConditionOfwiring.setText("")
        }

        binding.tvNextElectrical.setOnClickListener {

            if (isEdit) {
              updateElectricalDataApi(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId))
            } else if(isBackPressed){
                updateElectricalDataApi(SharePreference.getIntPref(requireActivity(),Constants.SurveyId))
            }
            else{
                addElectricalDataApi()
        }

        }


        return view

    }

    private fun init(){

        val electricityBill =  arrayListOf("Yes","No")
        binding.atElectricityBill.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, electricityBill))

        val electricityConn =  arrayListOf("Yes","No")
        binding.atElectcityConnection.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, electricityConn))

        val typeConnection =  arrayListOf("Single Phase","Three Phase")
        binding.atTypeConnection.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, typeConnection))

        val mainLine =  arrayListOf("Yes","No")
        binding.atMainLine.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, mainLine))

        val volatageKv =  arrayListOf("11KV","33KV")
        binding.atVoltage.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, volatageKv))

        val sourceSupply =  arrayListOf("Grid","Inverter","DG")
        binding.atPSourceSupply.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, sourceSupply))

        binding.atSSourceSupply.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, sourceSupply))

        val anaxerueB =  arrayListOf("Yes","No")
        binding.atMeterConnection.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, anaxerueB))

        binding.atACVoltage.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, typeConnection))

        binding.atScopeLoads.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, anaxerueB))

        binding.atSolarSystem.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, anaxerueB))

        binding.atProvisions.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, anaxerueB))

        binding.atScopeAdoption.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, anaxerueB))


        val NoDistribBoards =  arrayListOf("Greater than 0.8","Equal to 0.8","Less than 0.8")
        binding.etAvgPowerFact.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, NoDistribBoards))

        val conditionWiring = arrayListOf("Good","Average","Poor","Significant Damage")
        binding.atConditionOfwiring.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, conditionWiring))



    }

    private fun addElectricalDataApi(){
        val isValid = validateForm()
        if (isValid) {
            binding.progressBar.visibility = View.VISIBLE
            binding.ProgressBar1.visibility=View.VISIBLE
            binding.tvNextElectrical.visibility=View.GONE

            electricalViewModel.addElectricalData(
                SharePreference.getIntPref(requireActivity(), Constants.SurveyId),
                binding.atElectricityBill.text.toString(),
                binding.atElectcityConnection.text.toString(),
                binding.sectionedLoad.editText?.text.toString(),
                binding.peakLoad.editText?.text.toString(),
                binding.atTypeConnection.text.toString(),
                binding.averageElectricity.editText?.text.toString(),
                binding.atMainLine.text.toString(),
                binding.atVoltage.text.toString(),
                binding.averagePowerOutage.editText?.text.toString(),
                binding.atPSourceSupply.text.toString(),
                binding.atSSourceSupply.text.toString(),
                binding.atMeterConnection.text.toString(),
                binding.atACVoltage.text.toString(),
                binding.atScopeLoads.text.toString(),
                binding.etNoOutDoorLighting.editText?.text.toString(),
                binding.atSolarSystem.text.toString(),
                binding.etElectConsumption.editText?.text.toString(),
                binding.atProvisions.text.toString(),
                binding.atScopeAdoption.text.toString(),
                binding.etAvgPowerFact.text.toString(),
                binding.atDistribBoards.editText?.text.toString(),
                binding.atmcb5to10.editText?.text.toString(),
                binding.atMCB10to15.editText?.text.toString(),
                binding.atMCBGreter20.editText?.text.toString(),
                binding.atConditionOfwiring.text.toString()
            )
            electricalViewModel.servicesLiveData!!.observe(
                requireActivity(),
                Observer { SurveyorResponse ->
                    var checkStaus: Boolean = SurveyorResponse.status
                    binding.progressBar.visibility = View.GONE
                    AddAuditApi(SharePreference.getIntPref(requireActivity(), Constants.SurveyId),"Insert")
                    if (checkStaus) {
                        binding.progressBar.visibility = View.GONE
                        if (!spaceAvailabiltyFrag.isAdded) {
                            listener?.onItemClick(2)
                            val fragmentTransaction =
                                requireActivity().supportFragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.frameLayout, spaceAvailabiltyFrag)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                    } else {
                        binding.ProgressBar1.visibility=View.GONE
                        binding.tvNextElectrical.visibility=View.VISIBLE
                        Toast.makeText(
                            requireActivity(),
                            "" + SurveyorResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBar.visibility = View.GONE
                    }
                })
        }
    }

    private fun updateElectricalDataApi(editSurveyId:Int){
        val isValid = validateForm()
        if (isValid) {
            binding.progressBar.visibility = View.VISIBLE
            binding.ProgressBar1.visibility=View.VISIBLE
            binding.tvNextElectrical.visibility=View.GONE
            electricalViewModel.updateElectricalData(
                editSurveyId,
                binding.atElectricityBill.text.toString(),
                binding.atElectcityConnection.text.toString(),
                binding.sectionedLoad.editText?.text.toString(),
                binding.peakLoad.editText?.text.toString(),
                binding.atTypeConnection.text.toString(),
                binding.averageElectricity.editText?.text.toString(),
                binding.atMainLine.text.toString(),
                binding.atVoltage.text.toString(),
                binding.averagePowerOutage.editText?.text.toString(),
                binding.atPSourceSupply.text.toString(),
                binding.atSSourceSupply.text.toString(),
                binding.atMeterConnection.text.toString(),
                binding.atACVoltage.text.toString(),
                binding.atScopeLoads.text.toString(),
                binding.etNoOutDoorLighting.editText?.text.toString(),
                binding.atSolarSystem.text.toString(),
                binding.etElectConsumption.editText?.text.toString(),
                binding.atProvisions.text.toString(),
                binding.atScopeAdoption.text.toString(),
                binding.etAvgPowerFact.text.toString(),
                binding.atDistribBoards.editText?.text.toString(),
                binding.atmcb5to10.editText?.text.toString(),
                binding.atMCB10to15.editText?.text.toString(),
                binding.atMCBGreter20.editText?.text.toString(),
                binding.atConditionOfwiring.text.toString(),
            )
            electricalViewModel.servicesLiveData!!.observe(
                requireActivity(),
                Observer { SurveyorResponse ->
                    var checkStaus: Boolean = SurveyorResponse.status
                    binding.progressBar.visibility = View.GONE
                    if (checkStaus) {
                        AddAuditApi(editSurveyId,"Update")
                        binding.progressBar.visibility = View.GONE
                        val bundle1: Bundle = Bundle()
                        bundle1.putBoolean("isEdit", true)
                        listener?.onItemClick(2)
                        if (!spaceAvailabiltyFrag.isAdded) {
                            val fragmentTransaction =
                                requireActivity().supportFragmentManager.beginTransaction()
                            fragmentTransaction.add(R.id.frameLayout, spaceAvailabiltyFrag)
                            spaceAvailabiltyFrag.arguments = bundle1
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                    } else {
                        binding.ProgressBar1.visibility=View.GONE
                        binding.tvNextElectrical.visibility=View.VISIBLE
                        Toast.makeText(
                            requireActivity(),
                            "" + SurveyorResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBar.visibility = View.GONE
                    }
                })
        }
    }
    private fun getElectricalApi(surveyIds:Int) {
        binding.progressBar.visibility = View.VISIBLE

        electricalViewModel.getElectricalDetails(surveyIds)
        electricalViewModel.loading!!.observe(requireActivity()) {
            if (it) {
                binding.progressBar.visibility = View.GONE
            }
        }
            electricalViewModel.electricalResponse!!.observe(
                requireActivity(),
                Observer { getElectricalResponse ->
                    var checkStaus: Boolean = getElectricalResponse.status
                    if (checkStaus) {

                        var electricalData: ElectricalData = getElectricalResponse.data
                        binding.atElectricityBill.setText(electricalData.accessElectricity, false)
                        binding.atElectcityConnection.setText(electricalData.electricityConnection, false)
                        binding.sectionedLoad.editText?.setText(electricalData.electricityBill)
                        binding.peakLoad.editText?.setText(electricalData.peakLoadEbill)
                        binding.atTypeConnection.setText(electricalData.connectionType, false)
                        binding.averageElectricity.editText?.setText(electricalData.averageElectricity)
                        binding.atMainLine.setText(electricalData.availableMainLine, false)
                        binding.atVoltage.setText(electricalData.voltage, false)
                        binding.averagePowerOutage.editText?.setText(electricalData.averagePower)
                        binding.atPSourceSupply.setText(electricalData.psourceSupply, false)
                        binding.atSSourceSupply.setText(electricalData.ssourceSupply, false)
                        binding.atMeterConnection.setText(electricalData.meterconnAvailable, false)
                        binding.atACVoltage.setText(electricalData.acVoltage, false)
                        binding.atScopeLoads.setText(electricalData.expansionLoads, false)
                        binding.etNoOutDoorLighting.editText?.setText(electricalData.outdoorLightings)
                        binding.atSolarSystem.setText(electricalData.solarSystem, false)
                        binding.etElectConsumption.editText?.setText(electricalData.econsumption)
                        binding.atProvisions.setText(electricalData.provisionsExisting, false)
                        binding.atScopeAdoption.setText(electricalData.energyEfficiency, false)
                        binding.etAvgPowerFact.setText(electricalData.powerFactor,false)
                        binding.atDistribBoards.editText?.setText(electricalData.distributionBoards)
                        binding.atmcb5to10.editText?.setText(electricalData.noOfMcbsFiveToTen)
                        binding.atMCB10to15.editText?.setText(electricalData.noOfMcbsTenUper)
                        binding.atMCBGreter20.editText?.setText(electricalData.mcbsUptotwenty)
                        binding.atConditionOfwiring.setText(electricalData.conditionWiring, false)

                    } else {

                        binding.atElectricityBill.setText("")
                        binding.atElectcityConnection.setText("")
                        binding.sectionedLoad.editText?.setText("")
                        binding.peakLoad.editText?.setText("")
                        binding.atTypeConnection.setText("")
                        binding.averageElectricity.editText?.setText("")
                        binding.atMainLine.setText("")
                        binding.atVoltage.setText("")
                        binding.averagePowerOutage.editText?.setText("")
                        binding.atPSourceSupply.setText("")
                        binding.atSSourceSupply.setText("")
                        binding.atMeterConnection.setText("")
                        binding.atACVoltage.setText("")
                        binding.atScopeLoads.setText("")
                        binding.etNoOutDoorLighting.editText?.setText("")
                        binding.atSolarSystem.setText("")
                        binding.etElectConsumption.editText?.setText("")
                        binding.atProvisions.setText("")
                        binding.atScopeAdoption.setText("")
                        binding.etAvgPowerFact.setText("")
                        binding.atDistribBoards.editText?.setText("")
                        binding.atmcb5to10.editText?.setText("")
                        binding.atmcb5to10.editText?.setText("")
                        binding.atMCBGreter20.editText?.setText("")
                        binding.atConditionOfwiring.setText("")

                    }
                })


    }

    private fun validateForm(): Boolean {
        var isValid = false

        if (binding.atElectricityBill.text.toString().isEmpty()) {
            binding.atElectricityBill.error = "Access to electricity field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Access to electricity field is required",
                Toast.LENGTH_SHORT
            ).show()

        }else if (binding.atElectcityConnection.text.isNullOrEmpty()) {
            binding.atElectricityBill.error =null
            binding.atElectcityConnection.error = "Electricity Connection field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Electricity Connection field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.sectionedLoad.editText!!.text.isNullOrEmpty()) {
            binding.atElectcityConnection.error =null
            binding.sectionedLoad.error = "Sanctioned Load field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Sanctioned Load field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.peakLoad.editText!!.text.isNullOrEmpty()) {
            binding.sectionedLoad.error =null
            binding.sectionedLoad.error="Peak Load field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Peak Load field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if (binding.atTypeConnection.text.isNullOrEmpty()) {
            binding.sectionedLoad.error=null
            binding.atTypeConnection.error="Type of Connection field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Type of Connection field is required",
                Toast.LENGTH_SHORT
            ).show()


        }else if (binding.averageElectricity.editText!!.text.isNullOrEmpty()) {
            binding.atTypeConnection.error=null
            binding.averageElectricity.error = "Average electricity consumption field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Average electricity consumption field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.averagePowerOutage.editText!!.text.isNullOrEmpty()) {
            binding.averageElectricity.error =null
            binding.averagePowerOutage.error = "Average Power outage field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Average Power outage field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atPSourceSupply.text.isNullOrEmpty()) {
            binding.averagePowerOutage.error =null
            binding.atPSourceSupply.error = "Primary Source of Supply field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Primary Source of Supply field is required",
                Toast.LENGTH_SHORT
            ).show()

        }else if (binding.atACVoltage.text.isNullOrEmpty()) {
            binding.atPSourceSupply.error =null
            binding.atACVoltage.error = "AC Voltage field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "AC Voltage field is required",
                Toast.LENGTH_SHORT
            ).show()
        }else if (binding.atSolarSystem.text.isNullOrEmpty()) {
            binding.atACVoltage.error =null
            isValid = false
            binding.atSolarSystem.error = "Please Select Yes or No Solar system"
            Toast.makeText(
                requireActivity(),
                "Please Select Yes or No Solar system",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atConditionOfwiring.text.isNullOrEmpty()) {
            binding.atSolarSystem.error =null
            isValid = false
            binding.atConditionOfwiring.error = "Condition of Wiring field is required"
            Toast.makeText(
                requireActivity(),
                "Condition of Wiring field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else{
            binding.atConditionOfwiring.error =null
            isValid = true
        }


        return isValid
    }

    fun AddAuditApi(surveyId:Int,operationType:String){
        var userId:Int=SharePreference.getIntPref(requireActivity(),Constants.UserId)
        var userName:String?=SharePreference.getStringPref(requireActivity(),Constants.UserName)

        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,surveyId,operationType,"Electrical Parameters")

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