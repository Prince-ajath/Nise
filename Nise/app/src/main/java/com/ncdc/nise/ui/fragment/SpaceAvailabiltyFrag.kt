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
import com.ncdc.nise.data.model.spaceavilability.SpaceAvilableData
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.FragmentSpaceDetailsBinding
import com.ncdc.nise.interfaces.FragPostionInterface
import com.ncdc.nise.ui.viewmodel.SpaceViewModel
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SpaceAvailabiltyFrag: Fragment() {

    lateinit var binding:FragmentSpaceDetailsBinding
    var listener: FragPostionInterface? = null
    lateinit var spaceViewModel: SpaceViewModel
    var waterSourceFrag:WaterSourceFrag= WaterSourceFrag()
    var isEdit:Boolean=false
    var isBackPressed=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpaceDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        isEdit=SharePreference.getBooleanPref(requireActivity(),Constants.isEdit)
        isBackPressed=SharePreference.getBooleanPref(requireActivity(),Constants.isBackPressed)
        spaceViewModel=ViewModelProvider(requireActivity()).get(SpaceViewModel::class.java)

        init()

        listener = activity as FragPostionInterface
        if(isEdit){
            getSpaceAvilabiltyApi(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId))
        }else if(isBackPressed){
            getSpaceAvilabiltyApi(SharePreference.getIntPref(requireActivity(),Constants.SurveyId))
        }else{
            binding.etApproximateBuilding.editText?.setText("")
            binding.etApproximateParking.editText?.setText("")
            binding.atSpHosting.setText("")
            binding.atTiltAngle.setText("")
            binding.atEarth.setText("")
            binding.atInstallation.setText("")
            binding.atCPU.setText("")
            binding.atFancing.setText("")
            binding.atTheft.setText("")
            binding.etBuildingName.editText?.setText("")
            binding.atTypeRoof.setText("")
            binding.atRoofDirection.setText("")
            binding.atTiltAngle.setText("")
            binding.etTotalRoofAreaAv.editText?.setText("")
            binding.etTotalShadow.editText?.setText("")
            binding.atAnyObstrucrions.setText("")
            binding.atShapeRoof.setText("")
            binding.atAccessRoof.setText("")
            binding.atWeightRestrict.setText("")
            binding.atCMeterialBuilding.setText("")
            binding.atEwallCondition.setText("")
            binding.atRoofCondition.setText("")
            binding.atRoofSupportCondition.setText("")
            binding.atRoofMeterial.setText("")
        }

        binding.tvNextSpaceDetails.setOnClickListener {

            if (isEdit) {
               updateSpaceAvilabilityApi(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId))
            }else if(isBackPressed){
                updateSpaceAvilabilityApi(SharePreference.getIntPref(requireActivity(),Constants.SurveyId))
            }
            else{

                addSpaceAvilabilityApi()
            }

        }

        return view

    }

    private fun init(){

        val roofTypes = arrayListOf("Corrugated sheet","Flat RCC","Roof tiles")
        binding.atTypeRoof.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofTypes))

        val roofDirection = arrayListOf("Horizontal","Multiple Direction","North","North East","East","East South","South","South West","West","West North")
        binding.atRoofDirection.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofDirection))

        val tiltAngleArray= arrayListOf("0","5","10","15","20","25","30","35","40","45","50","55","60","65","70","75","80")

        binding.atTiltAngle.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, tiltAngleArray))

        val roofType = arrayListOf("Yes","No")
        binding.atAnyObstrucrions.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        val roofShape = arrayListOf("Square","Rectangular","Slanted","Uneven")
        binding.atShapeRoof.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofShape))

        val roofAccess = arrayListOf("Stairs","Escalators","Temporary","etc")
        binding.atAccessRoof.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofAccess))


        binding.atWeightRestrict.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atCPU.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atFancing.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atTheft.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atInstallation.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atWeatherSpace.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atEarth.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        binding.atSpHosting.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofType))

        val constructionArray= arrayListOf("Burnt brick","Concrete Brick","Wood","Soil")
        binding.atCMeterialBuilding.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, constructionArray))

        val eWallCondArray = arrayListOf("Good","Average","Poor","Significant Damage")
        binding.atEwallCondition.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, eWallCondArray))


        binding.atRoofCondition.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, eWallCondArray))


        binding.atRoofSupportCondition.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, eWallCondArray))

        val roofMeterialArray = arrayListOf("Brick","Metallic","Wood")
        binding.atRoofMeterial.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, roofMeterialArray))

    }

    private fun addSpaceAvilabilityApi(){
        val isValid = validateForm()
        if (isValid) {
            binding.progressBar.visibility = View.VISIBLE
            binding.ProgressBar1.visibility=View.VISIBLE
            binding.tvNextSpaceDetails.visibility=View.GONE
            var surveyId: Int = SharePreference.getIntPref(requireActivity(), Constants.SurveyId)

            spaceViewModel.addSpaceAvlData(
                surveyId,
                binding.etApproximateBuilding.editText?.text.toString(),
                binding.etApproximateParking.editText?.text.toString(),
                binding.atSpHosting.text.toString(),
                binding.atWeatherSpace.text.toString(),
                binding.atEarth.text.toString(),
                binding.atInstallation.text.toString(),
                binding.atCPU.text.toString(),
                binding.atFancing.text.toString(),
                binding.atTheft.text.toString(),
                binding.etBuildingName.editText?.text.toString(),
                binding.atTypeRoof.text.toString(),
                binding.atRoofDirection.text.toString(),
                binding.atTiltAngle.text.toString(),
                binding.etTotalRoofAreaAv.editText?.text.toString(),
                binding.etTotalShadow.editText?.text.toString(),
                binding.atAnyObstrucrions.text.toString(),
                binding.atShapeRoof.text.toString(),
                binding.atAccessRoof.text.toString(),
                binding.atWeightRestrict.text.toString(),
               "",
                binding.atCMeterialBuilding.text.toString(),
                binding.atEwallCondition.text.toString(),
                binding.atRoofCondition.text.toString(),
                binding.atRoofSupportCondition.text.toString(),
                binding.atRoofMeterial.text.toString(),
            )
            spaceViewModel.servicesLiveData!!.observe(
                requireActivity(),
                Observer { SurveyorResponse ->
                    var checkStaus: Boolean = SurveyorResponse.status
                    binding.progressBar.visibility = View.GONE
                    if (checkStaus) {

                        AddAuditApi(SharePreference.getIntPref(requireActivity(), Constants.SurveyId),"Insert")
                        if (!waterSourceFrag.isAdded) {
                            listener?.onItemClick(3)
                            val fragmentTransaction =
                                requireActivity().supportFragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.frameLayout, waterSourceFrag)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                    } else {
                        binding.ProgressBar1.visibility=View.GONE
                        binding.tvNextSpaceDetails.visibility=View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireActivity(),
                            "" + SurveyorResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }


    }

    private fun updateSpaceAvilabilityApi(surveyId:Int){
        val isValid = validateForm()
        if (isValid) {
            binding.progressBar.visibility = View.VISIBLE
            binding.ProgressBar1.visibility=View.VISIBLE
            binding.tvNextSpaceDetails.visibility=View.GONE
            spaceViewModel.updateSpaceAvlData(
                surveyId,
                binding.etApproximateBuilding.editText?.text.toString(),
                binding.etApproximateParking.editText?.text.toString(),
                binding.atSpHosting.text.toString(),
                binding.atWeatherSpace.text.toString(),
                binding.atEarth.text.toString(),
                binding.atInstallation.text.toString(),
                binding.atCPU.text.toString(),
                binding.atFancing.text.toString(),
                binding.atTheft.text.toString(),
                binding.etBuildingName.editText?.text.toString(),
                binding.atTypeRoof.text.toString(),
                binding.atRoofDirection.text.toString(),
                binding.atTiltAngle.text.toString(),
                binding.etTotalRoofAreaAv.editText?.text.toString(),
                binding.etTotalShadow.editText?.text.toString(),
                binding.atAnyObstrucrions.text.toString(),
                binding.atShapeRoof.text.toString(),
                binding.atAccessRoof.text.toString(),
                binding.atWeightRestrict.text.toString(),
                "",
                binding.atCMeterialBuilding.text.toString(),
                binding.atEwallCondition.text.toString(),
                binding.atRoofCondition.text.toString(),
                binding.atRoofSupportCondition.text.toString(),
                binding.atRoofMeterial.text.toString()
            )
            spaceViewModel.servicesLiveData!!.observe(
                requireActivity(),
                Observer { SurveyorResponse ->
                    var checkStaus: Boolean = SurveyorResponse.status
                    binding.progressBar.visibility = View.GONE
                    if (checkStaus) {
                        AddAuditApi(surveyId,"Update")
                        val bundle1: Bundle = Bundle()
                        bundle1.putBoolean("isEdit", true)

                        listener?.onItemClick(3)
                        if (!waterSourceFrag.isAdded) {
                            val fragmentTransaction =
                                requireActivity().supportFragmentManager.beginTransaction()
                            fragmentTransaction.add(R.id.frameLayout, waterSourceFrag)
                            waterSourceFrag.arguments = bundle1
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                    } else {
                        binding.ProgressBar1.visibility=View.GONE
                        binding.tvNextSpaceDetails.visibility=View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireActivity(),
                            "" + SurveyorResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }


    }
    private fun getSpaceAvilabiltyApi(surveyIds:Int){
        binding.progressBar.visibility = View.VISIBLE
        spaceViewModel.getSpaceAvilableApi(surveyIds)
        spaceViewModel.loading!!.observe(requireActivity()) {
            if (it) {

                binding.progressBar.visibility = View.GONE
            }
        }

            spaceViewModel.getSpaceResponse!!.observe(
                requireActivity(),
                Observer { GetSpaceResponse ->
                    var checkStaus: Boolean = GetSpaceResponse.status

                    if (checkStaus) {
                        var data: SpaceAvilableData = GetSpaceResponse.data
                        binding.progressBar.visibility = View.GONE
                        binding.etApproximateBuilding.editText?.setText(data.freeAreaRoof)
                        binding.etApproximateParking.editText?.setText(data.openGround)
                        binding.atSpHosting.setText(data.spaceToHost, false)
                        binding.atWeatherSpace.setText(data.secureStorage,false)
                        binding.atEarth.setText(data.availableEarthing, false)
                        binding.atInstallation.setText(data.laInstallation, false)
                        binding.atCPU.setText(data.saCpu, false)
                        binding.atFancing.setText(data.solarPVSystem, false)
                        binding.atTheft.setText(data.theftATSite, false)
                        binding.etBuildingName.editText?.setText(data.installationLocation)
                        binding.atTypeRoof.setText(data.typeOfRoof, false)
                        binding.atRoofDirection.setText(data.orientation, false)
                        binding.atTiltAngle.setText(data.tiltAngle,false)
                        binding.etTotalRoofAreaAv.editText?.setText(data.totalArea)
                        binding.etTotalShadow.editText?.setText(data.totalShadow)
                        binding.atAnyObstrucrions.setText(data.obstructions, false)
                        binding.atShapeRoof.setText(data.generalShape, false)
                        binding.atAccessRoof.setText(data.accessRoof, false)
                        binding.atWeightRestrict.setText(data.weightRestrictions, false)
                        binding.atCMeterialBuilding.setText(data.construction, false)
                        binding.atEwallCondition.setText(data.econdition, false)
                        binding.atRoofCondition.setText(data.roofCondition, false)
                        binding.atRoofSupportCondition.setText(data.supportCondition, false)
                        binding.atRoofMeterial.setText(data.roofMaterial, false)


                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.etApproximateBuilding.editText?.setText("")
                        binding.etApproximateParking.editText?.setText("")
                        binding.atSpHosting.setText("")
                        binding.atTiltAngle.setText("")
                        binding.atEarth.setText("")
                        binding.atInstallation.setText("")
                        binding.atCPU.setText("")
                        binding.atFancing.setText("")
                        binding.atTheft.setText("")
                        binding.etBuildingName.editText?.setText("")
                        binding.atTypeRoof.setText("")
                        binding.atRoofDirection.setText("")
                        binding.atTiltAngle.setText("")
                        binding.etTotalRoofAreaAv.editText?.setText("")
                        binding.etTotalShadow.editText?.setText("")
                        binding.atAnyObstrucrions.setText("")
                        binding.atShapeRoof.setText("")
                        binding.atAccessRoof.setText("")
                        binding.atWeightRestrict.setText("")
                        binding.atCMeterialBuilding.setText("")
                        binding.atEwallCondition.setText("")
                        binding.atRoofCondition.setText("")
                        binding.atRoofSupportCondition.setText("")
                        binding.atRoofMeterial.setText("")

                    }
                })


    }
    private fun validateForm(): Boolean {
        var isValid = false
    if (binding.etApproximateParking.editText!!.text.isNullOrEmpty()) {
            binding.etApproximateParking.error = "Area on the open ground field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Area on the open ground field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.etBuildingName.editText!!.text.isNullOrEmpty()) {
            binding.etApproximateParking.error =null
            binding.etBuildingName.error = "Installation Location field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Installation Location field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atTypeRoof.text.isNullOrEmpty()) {
            binding.etBuildingName.error =null
            binding.atTypeRoof.error="Type of Roof field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Type of Roof field is required",
                Toast.LENGTH_SHORT
            ).show()

        }else if (binding.atRoofDirection.text.isNullOrEmpty()) {
            binding.atTypeRoof.error=null
            binding.atRoofDirection.error="Orientation field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Orientation field is required",
                Toast.LENGTH_SHORT
            ).show()

        }else if (binding.etTotalRoofAreaAv.editText!!.text.isNullOrEmpty()) {
            binding.atRoofDirection.error=null
            binding.etTotalRoofAreaAv.error = "Total Area Available in Roof field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Total Area Available in Roof field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.etTotalShadow.editText!!.text.isNullOrEmpty()) {
            binding.etTotalRoofAreaAv.error =null
            binding.etTotalShadow.error = "Total Shadow free Area field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Total Shadow free Area field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.atAnyObstrucrions.text.isNullOrEmpty()) {
            binding.etTotalShadow.error =null
            isValid = false
            binding.atAnyObstrucrions.error = "Any obstructions field is required"
            Toast.makeText(
                requireActivity(),
                "Any obstructions field is required",
                Toast.LENGTH_SHORT
            ).show()
        } else{
            binding.atAnyObstrucrions.error =null
            isValid = true
        }


        return isValid
    }

    fun AddAuditApi(surveyId:Int,operationType:String){
        var userId:Int=SharePreference.getIntPref(requireActivity(),Constants.UserId)
        var userName:String?=SharePreference.getStringPref(requireActivity(),Constants.UserName)

        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,surveyId,operationType,"Space Avilability")

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
