package com.ncdc.nise.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ncdc.nise.R
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.WaterSourceData
import com.ncdc.nise.data.model.healthrelatedinfo.GetHealthInfoResponse
import com.ncdc.nise.data.model.healthrelatedinfo.HealthRelatedData
import com.ncdc.nise.data.model.vaccineStorage.GetVaccineStorageResponse
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.FragmentHealthRelatedInfoBinding

import com.ncdc.nise.interfaces.FragPostionInterface
import com.ncdc.nise.repository.VaccineStorageRepo
import com.ncdc.nise.ui.connected.fragment.EnergyAssementsFrag
import com.ncdc.nise.ui.viewmodel.HealthInfoViewModel
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HealthRelatedInfoFrag:Fragment() {

    lateinit var binding: FragmentHealthRelatedInfoBinding
    var listener: FragPostionInterface? = null
    lateinit var healthInfoViewModel:HealthInfoViewModel
    var energyAssementsFrag:EnergyAssementsFrag=EnergyAssementsFrag()
    var isEdit:Boolean=false
    var isBackPressed=false
    var surveIdEdit:Int=0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHealthRelatedInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        healthInfoViewModel=ViewModelProvider(requireActivity()).get(HealthInfoViewModel::class.java)
        isEdit=SharePreference.getBooleanPref(requireActivity(),Constants.isEdit)
        surveIdEdit=SharePreference.getIntPref(requireActivity(),Constants.editSurveyId)
        isBackPressed=SharePreference.getBooleanPref(requireActivity(),Constants.isBackPressed)

        init()
        listener= activity as FragPostionInterface
        if(isEdit){
            getHealthRelatedInfo(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId))
        }else if(isBackPressed){
            getHealthRelatedInfo(SharePreference.getIntPref(requireActivity(),Constants.SurveyId))
        }else{
            binding.totalPapulation.editText?.setText("")
            binding.annualTarget.editText?.setText("")
            binding.annualTargetWoman.editText?.setText("")
            binding.avearageNoOPD.editText?.setText("")
            binding.averageNoIPD.editText?.setText("")
            binding.averageNoSurgeries.editText?.setText("")
            binding.averageNoDeliveries.editText?.setText("")
            binding.atRoomService.setText("")
            binding.atOffer.setText("")
            binding.atAntiRabies.setText("")
            binding.atAreBirthLow.setText("")
            binding.atIsFixed.setText("")
            binding.atIsBlood.setText("")
            binding.atVedioConf.setText("")
            binding.atIsBloodStorage.setText("")
            binding.atColdChain.setText("")
            binding.etIPDOverNight.editText?.setText("")
        }


        binding.tvNextHealthRelated.setOnClickListener {

            addHealthRelInfoApi()

        }


        return view


    }
    private fun init(){
        val roofType = arrayListOf("Yes","No")
        binding.atRoomService.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atOffer.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atAntiRabies.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atAreBirthLow.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atIsFixed.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atIsBlood.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atVedioConf.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atIsBloodStorage.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atColdChain.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))
    }

    private fun addHealthRelInfoApi(){
            binding.progressBar.visibility = View.VISIBLE
            binding.ProgressBar1.visibility=View.VISIBLE
            binding.tvNextHealthRelated.visibility=View.GONE
            if (isEdit) {
                healthInfoViewModel.updateHealthRelatedApi(
                    SharePreference.getIntPref(requireActivity(), Constants.editSurveyId),
                    binding.totalPapulation.editText?.text.toString(),
                    binding.annualTarget.editText?.text.toString(),
                    binding.annualTargetWoman.editText?.text.toString(),
                    binding.avearageNoOPD.editText?.text.toString(),
                    binding.averageNoIPD.editText?.text.toString(),
                    binding.averageNoSurgeries.editText?.text.toString(),
                    binding.averageNoDeliveries.editText?.text.toString(),
                    binding.atRoomService.text.toString(),
                    binding.atOffer.text.toString(),
                    binding.atAntiRabies.text.toString(),
                    binding.atAreBirthLow.text.toString(),
                    binding.atIsFixed.text.toString(),
                    binding.atIsBlood.text.toString(),
                    binding.atVedioConf.text.toString(),
                    binding.atIsBloodStorage.text.toString(),
                    binding.atColdChain.text.toString(),
                    binding.etIPDOverNight.editText?.text.toString()
                )
            } else if (isBackPressed) {
                healthInfoViewModel.updateHealthRelatedApi(
                    SharePreference.getIntPref(requireActivity(), Constants.SurveyId),
                    binding.totalPapulation.editText?.text.toString(),
                    binding.annualTarget.editText?.text.toString(),
                    binding.annualTargetWoman.editText?.text.toString(),
                    binding.avearageNoOPD.editText?.text.toString(),
                    binding.averageNoIPD.editText?.text.toString(),
                    binding.averageNoSurgeries.editText?.text.toString(),
                    binding.averageNoDeliveries.editText?.text.toString(),
                    binding.atRoomService.text.toString(),
                    binding.atOffer.text.toString(),
                    binding.atAntiRabies.text.toString(),
                    binding.atAreBirthLow.text.toString(),
                    binding.atIsFixed.text.toString(),
                    binding.atIsBlood.text.toString(),
                    binding.atVedioConf.text.toString(),
                    binding.atIsBloodStorage.text.toString(),
                    binding.atColdChain.text.toString(),
                    binding.etIPDOverNight.editText?.text.toString()
                )
            } else {
                healthInfoViewModel.addHealthRelatedApi(
                    SharePreference.getIntPref(requireActivity(), Constants.SurveyId),
                    binding.totalPapulation.editText?.text.toString(),
                    binding.annualTarget.editText?.text.toString(),
                    binding.annualTargetWoman.editText?.text.toString(),
                    binding.avearageNoOPD.editText?.text.toString(),
                    binding.averageNoIPD.editText?.text.toString(),
                    binding.averageNoSurgeries.editText?.text.toString(),
                    binding.averageNoDeliveries.editText?.text.toString(),
                    binding.atRoomService.text.toString(),
                    binding.atOffer.text.toString(),
                    binding.atAntiRabies.text.toString(),
                    binding.atAreBirthLow.text.toString(),
                    binding.atIsFixed.text.toString(),
                    binding.atIsBlood.text.toString(),
                    binding.atVedioConf.text.toString(),
                    binding.atIsBloodStorage.text.toString(),
                    binding.atColdChain.text.toString(),
                    binding.etIPDOverNight.editText?.text.toString()
                )
            }

            healthInfoViewModel.servicesLiveData!!.observe(
                requireActivity(),
                Observer { HealthRelatedResponse ->
                    var checkStaus: Boolean = HealthRelatedResponse.status
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

                        if (!energyAssementsFrag.isAdded) {
                            listener?.onItemClick(6)
                            val fragmentTransaction =
                                requireActivity().supportFragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.frameLayout, energyAssementsFrag)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                    } else {
                        binding.ProgressBar1.visibility=View.GONE
                        binding.tvNextHealthRelated.visibility=View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireActivity(),
                            "" + HealthRelatedResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

    }

    fun getHealthRelatedInfo(surveyId:Int){
        binding.progressBar.visibility = View.VISIBLE
        if(isEdit){
            healthInfoViewModel.getHealthRelatedApi(surveyId)
        }else if(isBackPressed){
            healthInfoViewModel.getHealthRelatedApi(surveyId)
        }
        healthInfoViewModel.loading!!.observe(requireActivity()) {
            if (it) {

                binding.progressBar.visibility = View.GONE
            }
        }

            healthInfoViewModel.healthRelatedResponse!!.observe(requireActivity(), Observer { GetHealthInfoResponse->
                var checkStaus:Boolean=GetHealthInfoResponse.status

                if(checkStaus){
                    binding.progressBar.visibility = View.GONE
                    var waterData: HealthRelatedData =GetHealthInfoResponse.data
                    binding.totalPapulation.editText?.setText(waterData.totalPapulation)
                    binding.annualTarget.editText?.setText(waterData.annualTarget)
                    binding.annualTargetWoman.editText?.setText(waterData.pregnantWomen)
                    binding.avearageNoOPD.editText?.setText(waterData.averageNoOpd)
                    binding.averageNoIPD.editText?.setText(waterData.totalIpd)
                    binding.averageNoSurgeries.editText?.setText(waterData.averageNoSurgeries)
                    binding.averageNoDeliveries.editText?.setText(waterData.averageNoDeliveries)
                    binding.atRoomService.setText(waterData.emergencyServices,false)
                    binding.atOffer.setText(waterData.opdServices,false)
                    binding.atAntiRabies.setText(waterData.antiRabiesVaccine,false)
                    binding.atAreBirthLow.setText(waterData.babiesManaged,false)
                    binding.atIsFixed.setText(waterData.immunization,false)
                    binding.atIsBlood.setText(waterData.bloodExamination,false)
                    binding.atVedioConf.setText(waterData.teleMedicine,false)
                    binding.atIsBloodStorage.setText(waterData.bloodStorage,false)
                    binding.atColdChain.setText(waterData.coldChain,false)
                    binding.etIPDOverNight.editText?.setText(waterData.noOfIpds)


                }else{
                    binding.progressBar.visibility = View.GONE
                    binding.totalPapulation.editText?.setText("")
                    binding.annualTarget.editText?.setText("")
                    binding.annualTargetWoman.editText?.setText("")
                    binding.avearageNoOPD.editText?.setText("")
                    binding.averageNoIPD.editText?.setText("")
                    binding.averageNoSurgeries.editText?.setText("")
                    binding.averageNoDeliveries.editText?.setText("")
                    binding.atRoomService.setText("")
                    binding.atOffer.setText("")
                    binding.atAntiRabies.setText("")
                    binding.atAreBirthLow.setText("")
                    binding.atIsFixed.setText("")
                    binding.atIsBlood.setText("")
                    binding.atVedioConf.setText("")
                    binding.atIsBloodStorage.setText("")
                    binding.atColdChain.setText("")
                    binding.etIPDOverNight.editText?.setText("")
                }
            })



    }

   /* private fun validateForm(): Boolean {
        var isValid = false

        if (binding.totalPapulation.editText!!.text.toString().isEmpty()) {
            binding.totalPapulation.error = "Total Population field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Total Population field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else  if (binding.annualTarget.editText!!.text.toString().isEmpty()) {
            binding.totalPapulation.error = null
            binding.annualTarget.error = "Annual target field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Annual target field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.annualTargetWoman.editText!!.text.toString().isEmpty()) {
            binding.annualTarget.error = null
            binding.annualTargetWoman.error = "Annual target pregnant women field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Annual target pregnant women field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.avearageNoOPD.editText!!.text.toString().isEmpty()) {
            binding.annualTargetWoman.error = null
            binding.avearageNoOPD.error = "Average number of OPD field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Average number of OPD field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.averageNoIPD.editText!!.text.toString().isEmpty()) {
            binding.avearageNoOPD.error = null
            binding.averageNoIPD.error = "Average number of total IPD field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Average number of total IPD field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.averageNoSurgeries.editText!!.text.toString().isEmpty()) {
            binding.averageNoIPD.error = null
            binding.averageNoSurgeries.error = "Average number of surgeries field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Average number of surgeries field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else  if (binding.averageNoDeliveries.editText!!.text.toString().isEmpty()) {
            binding.averageNoSurgeries.error = null
            binding.averageNoDeliveries.error = "Average number of deliveries field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Average number of deliveries field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atRoomService.text.toString().isEmpty()) {
            binding.averageNoDeliveries.error = null
            binding.atRoomService.error = "Room services field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Room services field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atOffer.text.toString().isEmpty()) {
            binding.atRoomService.error = null
            binding.atOffer.error = "Evening OPD services field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Evening OPD services field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atAntiRabies.text.toString().isEmpty()) {
            binding.atOffer.error = null
            binding.atAntiRabies.error = "Is anti-rabies vaccine field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Is anti-rabies vaccine field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atAreBirthLow.text.toString().isEmpty()) {
            binding.atAntiRabies.error = null
            binding.atAreBirthLow.error = "Are low birth weight babies field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Are low birth weight babies field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atIsFixed.text.toString().isEmpty()) {
            binding.atAreBirthLow.error = null
            binding.atIsFixed.error = "Is there a fixed immunization field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Is there a fixed immunization field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atIsBlood.text.toString().isEmpty()) {
            binding.atIsFixed.error = null
            binding.atIsBlood.error = "Is blood examination field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Is blood examination field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atVedioConf.text.toString().isEmpty()) {
            binding.atIsBlood.error = null
            binding.atVedioConf.error = "Does the facility have tele-medicine field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Does the facility have tele-medicine field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atIsBloodStorage.text.toString().isEmpty()) {
            binding.atVedioConf.error = null
            binding.atIsBloodStorage.error = "Is blood storage facility field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Is blood storage facility field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atColdChain.text.toString().isEmpty()) {
            binding.atIsBloodStorage.error = null
            binding.atColdChain.error = "If this facility is a cold chain point field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "If this facility is a cold chain point field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.etIPDOverNight.editText!!.text.toString().isEmpty()) {
            binding.atColdChain.error = null
            binding.etIPDOverNight.error = "Number of IPDs overnight field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Number of IPDs overnight field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            binding.etIPDOverNight.error = null
            isValid = true
        }
        return isValid

    }*/

    fun AddAuditApi(surveyId:Int,operationType:String){
        var userId:Int=SharePreference.getIntPref(requireActivity(),Constants.UserId)
        var userName:String?=SharePreference.getStringPref(requireActivity(),Constants.UserName)

        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,surveyId,operationType,"Health Related Information")

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