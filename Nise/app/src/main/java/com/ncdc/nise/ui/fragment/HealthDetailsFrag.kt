package com.ncdc.nise.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ncdc.nise.R
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.health.HealthData
import com.ncdc.nise.data.model.health.HealthDetailsModel
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.FragmentHealthDetailsBinding
import com.ncdc.nise.interfaces.FragPostionInterface
import com.ncdc.nise.repository.HealthDetailsRepo
import com.ncdc.nise.ui.viewmodel.HealthDetailsViewModel
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.regex.Pattern


class HealthDetailsFrag :Fragment() {
    lateinit var binding:FragmentHealthDetailsBinding
    var listener: FragPostionInterface? = null
    lateinit var healthDetailsViewModel: HealthDetailsViewModel
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var electricalFragment=ElectricalFragment()
    var isEdit:Boolean=false
    var isBackPressed=false
    var surveIdEdit:Int=0
    var isTimeDialog:Boolean=false
    private var isClick = false
    var posCity: Int=0




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHealthDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.progressBar.visibility=View.VISIBLE

        isEdit=SharePreference.getBooleanPref(requireActivity(),Constants.isEdit)
        surveIdEdit=SharePreference.getIntPref(requireActivity(),Constants.editSurveyId)
        isBackPressed=SharePreference.getBooleanPref(requireActivity(),Constants.isBackPressed)

        init()
        setOnClickListener()
        listener= activity as FragPostionInterface
        listener?.onItemClick(0)
        healthDetailsViewModel=ViewModelProvider(requireActivity()).get(HealthDetailsViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if(isEdit){
            getHealthDetailsApi2(SharePreference.getIntPref(requireActivity(),Constants.editSurveyId))
            binding.progressBar.visibility = View.VISIBLE
        }else if(isBackPressed){
            getHealthDetailsApi2(SharePreference.getIntPref(requireActivity(),Constants.SurveyId))
            binding.progressBar.visibility = View.VISIBLE
        } else{
            binding.progressBar.visibility=View.GONE
            isClick = false
            binding.hHDistrictName.visibility=View.VISIBLE
            binding.tvAddressType.visibility=View.GONE
            binding.hFName.editText?.setText("")
            binding.hFCategory.setText("")
            binding.hHWelnesCenter.setText("")
            binding.hHMobileNetwork.setText("")
            binding.hHDistrictCate.setText("")
            binding.hFAddLocDetails.editText?.setText("")
            binding.hFPincode.editText?.setText("")
            binding.hFContactPersonName.editText?.setText("")
            binding.hFPersonEmail.editText?.setText("")
            binding.hFContactNo.editText?.setText("")
            binding.workingHrs.editText?.setText("")

            binding.atExtendedWorking.setText("")
            binding.atLogisticfacility.setText("")
            binding.hFDismainRoad.editText?.setText("")
            binding.atapproachFacility.setText("")
            binding.nameElectrictyBoard.editText?.setText("")
            binding.hFConsumerId.editText?.setText("")
            binding.hFConsumerName.editText?.setText("")
            binding.atnoOfStaff.editText?.setText("")
            binding.hFNoOfBeds.editText?.setText("")
            binding.atNoOfQuarters.editText?.setText("")
            binding.hFdisQuarters.editText?.setText("")
            binding.atAmbulance.setText("")
            binding.atDrinkingWater.setText("")
            binding.atAgeOfTheBuilding.editText?.setText("")
            binding.athFMajorRenovation.setText("")
            binding.hFNoOFFloors.editText?.setText("")
            binding.hFNoBuildings.editText?.setText("")
            binding.hFTempLocation.editText?.setText("")
            binding.hFdisComPlace.editText?.setText("")
        }
        binding.tvNextHealth.setOnClickListener {

            if(isEdit){
                updateHealthDetails(surveIdEdit)
            }else if(isBackPressed){
                updateHealthDetails(SharePreference.getIntPref(requireActivity(),Constants.SurveyId))
            }
            else{
                validateAndLogin()
            }


        }


        return view

    }
    private fun init(){

        binding.hFStateName.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, Constants.statesArray))
        binding.hFStateName.setSelection(
            Constants.getIndexOf(
                SharePreference.getStringPref(
                    requireActivity(),
                    Constants.STATE
                )
            )
        )

        val posDistrict: Int = Constants.getIndexOf(
            SharePreference.getStringPref(
                requireActivity(),
                Constants.STATE
            )
        )


        if (posDistrict != 0) {

            binding.hHDistrictName.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, Constants.districtsArray.get(posDistrict)))

             posCity = Constants.getIndexOf(
                SharePreference.getStringPref(
                    requireActivity(),
                    Constants.CITY
                ), posDistrict
            )

            binding.hHDistrictName.setSelection(posCity)
        }

        val hfCateArray = arrayListOf("Medical College","General Hospital","Sub District Hospital","CHC","PHC","Sub Health Center")
        binding.hFCategory.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, hfCateArray))

        val yesNoArray = arrayListOf("Yes","No")
        binding.hHWelnesCenter.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yesNoArray))

        val MobileNetworkValues = arrayListOf("Good","Poor")
        binding.hHMobileNetwork.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, MobileNetworkValues))

        val countryValues = arrayListOf("India")
        binding.atCountry.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, countryValues))


        val DistrictCateValues = arrayListOf("Aspirational District","Non Aspirational District")
        binding.hHDistrictCate.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, DistrictCateValues))

        val eWorkingHours = arrayListOf("Yes","No")
        binding.atExtendedWorking.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, eWorkingHours))

        val logisticFacility = arrayListOf("Yes","No")
        binding.atLogisticfacility.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, logisticFacility))

        val approachFacility = arrayListOf("Pucca Road","Motorable Road","Kacha Road")
        binding.atapproachFacility.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, approachFacility))




        val ambulanceFacility =  arrayListOf("Yes","No")
        binding.atAmbulance.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, ambulanceFacility))

        val drinkingWaterFacility = arrayListOf("Yes","No")
        binding.atDrinkingWater.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, drinkingWaterFacility))



        val yearMajorRenov= arrayListOf("No renovation","2022","2021","2020","2019","2018","2017","2016","2015","2014","2013","2012","2011","2010","2009","2008","2007","2006","2005","2004","2003","2002","2001","2000",
        "1999","1998","1997","1996","1995","1994","1993","1992","1991","1990","1989","1988","1987","1986","1985","1984","1983","1982","1981","1980","1979","1978","1976","1975","1974","1973","1972","1971",
        "1970","1969","1968","1967","1965","1964","1963","1962","1961","1960")

        binding.athFMajorRenovation.setAdapter(ArrayAdapter(requireActivity(), R.layout.list_item, yearMajorRenov))





    }



    private fun setOnClickListener() {

        binding.hFStateName.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, postion: Int, l: Long) {
                binding.hHDistrictName.setAdapter(
                    ArrayAdapter<String>(
                        requireActivity(),
                        android.R.layout.simple_list_item_1,
                        Constants.districtsArray.get(postion)
                    )
                )
            }




            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        })

        binding.tvAddressType.setOnClickListener {
            if(isClick){
                isClick=false
                binding.hHDistrictName.visibility=View.VISIBLE
                binding.tvAddressType.visibility=View.GONE
                val posDistrict: Int = Constants.getIndexOf(
                    SharePreference.getStringPref(
                        requireActivity(),
                        Constants.STATE
                    )
                )


                if (posDistrict != 0) {

                    binding.hHDistrictName.setAdapter(
                        ArrayAdapter(
                            requireActivity(),
                            R.layout.list_item,
                            Constants.districtsArray.get(posDistrict)
                        )
                    )

                     posCity = Constants.getIndexOf(
                        SharePreference.getStringPref(
                            requireActivity(),
                            Constants.CITY
                        ), posDistrict
                    )
                    binding.hHDistrictName.setSelection(posCity)
                }
            }



        }

    }






    private fun updateHealthDetails(surveyId:Int) {
        val isValid = validateForm()
        if (isValid) {
            binding.progressBar.visibility = View.VISIBLE
            binding.ProgressBar1.visibility=View.VISIBLE
            binding.tvNextHealth.visibility=View.GONE
            var selectedStateId: String = binding.hFStateName.selectedItemId.toString()
            var stateId = Integer.parseInt(selectedStateId)
            var distName=""
            if(isClick){
                distName=binding.tvAddressType.text.toString()
            }else{
                distName=binding.hHDistrictName.selectedItem.toString()
            }

            healthDetailsViewModel.updateHealthDetails(
                surveyId,
                SharePreference.getIntPref(requireActivity(), Constants.UserId),
                binding.hFName.editText?.text.toString(),
                binding.hFCategory.text.toString(),
                binding.hHWelnesCenter.text.toString(),
                distName,
                binding.hFStateName.selectedItem.toString(),
                binding.hHMobileNetwork.text.toString(),
                binding.hHDistrictCate.text.toString(),
                binding.hFAddLocDetails.editText?.text.toString(),
                binding.hFLatitudeSite.text.toString(),
                binding.hFLongitudeSite.text.toString(),
                "India",
                binding.hFPincode.editText?.text.toString(),
                binding.hFContactPersonName.editText?.text.toString(),
                binding.hFPersonEmail.editText?.text.toString(),
                binding.hFContactNo.editText?.text.toString(),
                binding.workingHrs.editText?.text.toString(),
               "",
               "",
                binding.atExtendedWorking.text.toString(),
                binding.atLogisticfacility.text.toString(),
                binding.hFDismainRoad.editText?.text.toString(),
                binding.atapproachFacility.text.toString(),
                binding.nameElectrictyBoard.editText?.text.toString(),
                binding.hFConsumerId.editText?.text.toString(),
                binding.hFConsumerName.editText?.text.toString(),
                binding.atnoOfStaff.editText?.text.toString(),
                binding.hFNoOfBeds.editText?.text.toString(),
                binding.atNoOfQuarters.editText?.text.toString(),
                binding.hFdisQuarters.editText?.text.toString(),
                binding.atAmbulance.text.toString(),
                binding.atDrinkingWater.text.toString(),
                binding.atAgeOfTheBuilding.editText?.text.toString(),
                binding.athFMajorRenovation.text.toString(),
                binding.hFNoOFFloors.editText?.text.toString(),
                binding.hFNoBuildings.editText?.text.toString(),
                binding.hFTempLocation.editText?.text.toString(),
                binding.hFdisComPlace.editText?.text.toString(),
                stateId, 0
            )
            healthDetailsViewModel.servicesLiveData!!.observe(
                requireActivity(),
                Observer { SurveyorResponse ->
                    var checkStaus: Boolean = SurveyorResponse.status
                    if (checkStaus) {
                        binding.ProgressBar1.visibility=View.GONE
                        binding.tvNextHealth.visibility=View.GONE
                        binding.progressBar.visibility = View.GONE
                        var surveyIds: String = SurveyorResponse.surveyorId
                        var userId: Int = Integer.parseInt(surveyIds)

                        AddAuditApi(surveyId,"Update")
                        SharePreference.setIntPref(requireActivity(), Constants.SurveyId, userId)
                        val bundle1: Bundle = Bundle()
                        bundle1.putBoolean("isEdit", true)
                        bundle1.putString("userId", surveyIds)

                        listener?.onItemClick(1)
                        if (!electricalFragment.isAdded) {
                            val fragmentTransaction =
                                requireActivity().supportFragmentManager.beginTransaction()
                            fragmentTransaction.add(R.id.frameLayout, electricalFragment)
                            electricalFragment.arguments = bundle1
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                    } else {
                        binding.ProgressBar1.visibility=View.GONE
                        binding.tvNextHealth.visibility=View.VISIBLE
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

    private fun validateAndLogin() {
        val isValid = validateForm()
        if (isValid) {
            var distName=""
            binding.progressBar.visibility = View.VISIBLE
            binding.ProgressBar1.visibility=View.VISIBLE
            binding.tvNextHealth.visibility=View.GONE
            var surveyorId:Int=SharePreference.getIntPref(requireActivity(),Constants.UserId)
            var selectedStateId:String= binding.hFStateName.selectedItemId.toString()
            var stateId=Integer.parseInt(selectedStateId)
            if(isClick){
                distName=binding.tvAddressType.text.toString()
            }else{
                distName=binding.hHDistrictName.selectedItem.toString()
            }
            healthDetailsViewModel.addHealthDetails(
                surveyorId,
                binding.hFName.editText?.text.toString(),
                binding.hFCategory.text.toString(),
                binding.hHWelnesCenter.text.toString(),
                distName,
                binding.hFStateName.selectedItem.toString(),
                binding.hHMobileNetwork.text.toString(),
                binding.hHDistrictCate.text.toString(),
                binding.hFAddLocDetails.editText?.text.toString(),
                binding.hFLatitudeSite.text.toString(),
                binding.hFLongitudeSite.text.toString(),
                "India",
                binding.hFPincode.editText?.text.toString(),
                binding.hFContactPersonName.editText?.text.toString(),
                binding.hFPersonEmail.editText?.text.toString(),
                binding.hFContactNo.editText?.text.toString(),
                binding.workingHrs.editText?.text.toString(),
                "",
               "",
                binding.atExtendedWorking.text.toString(),
                binding.atLogisticfacility.text.toString(),
                binding.hFDismainRoad.editText?.text.toString(),
                binding.atapproachFacility.text.toString(),
                binding.nameElectrictyBoard.editText?.text.toString(),
                binding.hFConsumerId.editText?.text.toString(),
                binding.hFConsumerName.editText?.text.toString(),
                binding.atnoOfStaff.editText?.text.toString(),
                binding.hFNoOfBeds.editText?.text.toString(),
                binding.atNoOfQuarters.editText?.text.toString(),
                binding.hFdisQuarters.editText?.text.toString(),
                binding.atAmbulance.text.toString(),
                binding.atDrinkingWater.text.toString(),
                binding.atAgeOfTheBuilding.editText?.text.toString(),
                binding.athFMajorRenovation.text.toString(),
                binding.hFNoOFFloors.editText?.text.toString(),
                binding.hFNoBuildings.editText?.text.toString(),
                binding.hFTempLocation.editText?.text.toString(),
                binding.hFdisComPlace.editText?.text.toString(),
                stateId,0)
            healthDetailsViewModel.servicesLiveData!!.observe(requireActivity(), Observer { SurveyorResponse->
                var checkStaus:Boolean=SurveyorResponse.status
                if(checkStaus){

                    var surveyId:String=SurveyorResponse.surveyorId
                    var userId:Int=Integer.parseInt(surveyId)
                    SharePreference.setIntPref(requireActivity(),Constants.SurveyId,userId)
                    AddAuditApi(userId,"Insert")
                    listener?.onItemClick(1)
                    val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                    fragmentTransaction.add(R.id.frameLayout, ElectricalFragment())
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }else{
                    binding.ProgressBar1.visibility=View.GONE
                    binding.tvNextHealth.visibility=View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireActivity(),""+SurveyorResponse.message,Toast.LENGTH_SHORT).show()
                }
            })


        }
    }




    private fun validateForm(): Boolean {
        var isValid = false
        val regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]")
        var hrs:String=binding.workingHrs.editText?.text.toString()

        var fName=binding.etName.text.toString()

        if (regex.matcher(fName).find() || fName.isEmpty()) {

            binding.hFName.error = "Name of Health facility is required"
            Toast.makeText(
                requireActivity(),
                "Name of Health facility is required",
                Toast.LENGTH_SHORT
            ).show()
            isValid = false

        }else if (binding.hFCategory.text.isNullOrEmpty()) {
                binding.hFName.error =null
                binding.hFCategory.error = "Health Facility Category is required"
                isValid = false
            Toast.makeText(
                requireActivity(),
                "Health Facility Category is required",
                Toast.LENGTH_SHORT
            ).show()
        } else if (binding.hHWelnesCenter.text.isNullOrEmpty()) {
            binding.hFCategory.error =null
            binding.hHWelnesCenter.error = "Please Select Wellness Center Yes or No"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "Please Select Wellness Center Yes or No",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.hFStateName.selectedItem.toString().equals("Select State")) {
                    binding.hHWelnesCenter.error =null
                    isValid = false
                    Toast.makeText(
                        requireActivity(),
                        "Plaese select State",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.hFStateName.requestFocus()


                }else if (binding.hHDistrictName.selectedItem.toString().equals("Select District") && !isClick) {
                        isValid = false
                        Toast.makeText(
                            requireActivity(),
                            "Plaese select District",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.hHDistrictName.requestFocus()

        }else if (binding.hHMobileNetwork.text.isNullOrEmpty()) {
            isValid = false
            binding.hHMobileNetwork.error = "Mobile network strength at the facility is required"
            Toast.makeText(
                requireActivity(),
                "Mobile network strength at the facility is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.hHDistrictCate.text.isNullOrEmpty()) {
            binding.hHMobileNetwork.error =null
            binding.hHDistrictCate.error="District Category field is required"
            isValid = false
            Toast.makeText(
                requireActivity(),
                "District Category field is required",
                Toast.LENGTH_SHORT
            ).show()

        } else if (binding.hFPincode.editText!!.text.isNullOrEmpty()) {
            binding.hHDistrictCate.error =null
            isValid = false
            binding.hFPincode.error = "Pincode field is required"
            Toast.makeText(
                requireActivity(),
                "Pincode field is required",
                Toast.LENGTH_SHORT
            ).show()

        }else if (binding.hFContactNo.editText!!.text.isNullOrEmpty()) {
            binding.hFPincode.error =null
            isValid = false
            binding.hFContactNo.error = "Contact No. field is required"
            Toast.makeText(
                requireActivity(),
                "Contact No. field is required",
                Toast.LENGTH_SHORT
            ).show()

        }else if (binding.workingHrs.editText?.text.isNullOrEmpty() || Integer.parseInt(hrs) > 25) {
            binding.hFContactNo.error =null
            isValid = false
            binding.workingHrs.error = "Working Hours Health facility field is required or less than 25 hours"
            Toast.makeText(
                requireActivity(),
                "Working Hours Health facility field is required or less than 25 hours",
                Toast.LENGTH_SHORT
            ).show()

        }

        else if (binding.hFNoOFFloors.editText!!.text.isNullOrEmpty()) {
            binding.workingHrs.error =null
            isValid = false
            binding.hFNoOFFloors.error = "Number of floors field is required"
            Toast.makeText(
                requireActivity(),
                "Number of floors field is required",
                Toast.LENGTH_SHORT
            ).show()

        }else if (binding.hFNoBuildings.editText!!.text.isNullOrEmpty()) {
            binding.hFNoOFFloors.error =null
            isValid = false
            binding.hFNoBuildings.error = "Number of Buildings field is required"
            Toast.makeText(
                requireActivity(),
                "Number of Buildings field is required",
                Toast.LENGTH_SHORT
            ).show()

        }else{
                binding.hFNoBuildings.error =null
                isValid =true
        }



        return isValid
    }

     override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
        else {
            getLastLocation()
        }
    }
    private fun getLastLocation() {
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result
                binding.hFLatitudeSite.text = (lastLocation)!!.latitude.toString()
                binding.hFLongitudeSite.text = (lastLocation)!!.longitude.toString()
            }
            else {
                Log.w(TAG, "getLastLocation:exception", task.exception)
                showMessage("No location detected. Make sure location is enabled on the device.")
            }
        }
    }
    private fun showMessage(string: String) {

        if (binding.rootLayout != null) {
            Toast.makeText(requireActivity(), string, Toast.LENGTH_LONG).show()
        }
    }
    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(requireActivity(), mainTextStringId, Toast.LENGTH_LONG).show()
    }
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }
    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        }
        else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {

                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
    companion object {
        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    fun getHealthDetailsApi2(surveyId:Int) {

        val call = RetrofitClient.apiInterface.getHealthDetails(surveyId)
        binding.progressBar.visibility = View.VISIBLE
        binding.cardLayout.visibility=View.GONE

        call.enqueue(object: Callback<HealthDetailsModel> {

            override fun onFailure(call: Call<HealthDetailsModel>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<HealthDetailsModel>,
                response: Response<HealthDetailsModel>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    var checkStaus:Boolean =data!!.status
                    if(checkStaus){

                        binding.cardLayout.visibility=View.VISIBLE
                        isClick = true
                        binding.hHDistrictName.visibility=View.GONE
                        binding.tvAddressType.visibility=View.VISIBLE

                        var healthData: HealthData=data.data
                        binding.hFName.editText?.setText(healthData.hfName)
                        binding.hFCategory.setText(healthData.hfCategory,false)
                        binding.hHWelnesCenter.setText(healthData.hfWellnessCentre,false)
                        binding.hFStateName.setSelection(Constants.getIndexOf(healthData.hfStateName))
                        binding.tvAddressType.setText(healthData.hfDistrictName)
                        binding.hHMobileNetwork.setText(healthData.hfMobileNetwork,false)
                        binding.hHDistrictCate.setText(healthData.hfDistrictCate,false)
                        binding.hFAddLocDetails.editText?.setText(healthData.hfAddressLocation)
                        binding.hFPincode.editText?.setText(healthData.hfPincode)
                        binding.hFContactPersonName.editText?.setText(healthData.hfCPName)
                        binding.hFPersonEmail.editText?.setText(healthData.hfCPEmail)
                        binding.hFContactNo.editText?.setText(healthData.hfCPMobile)
                        binding.workingHrs.editText?.setText(healthData.hfWorkinghours)

                        binding.atExtendedWorking.setText(healthData.hfEWorkinghours,false)
                        binding.atLogisticfacility.setText(healthData.hfLogisticfacility,false)
                        binding.hFDismainRoad.editText?.setText(healthData.hfMainRoadDist)
                        binding.atapproachFacility.setText(healthData.hfApproachSite,false)
                        binding.nameElectrictyBoard.editText?.setText(healthData.hfEBoardName)
                        binding.hFConsumerId.editText?.setText(healthData.hfConsumerid)
                        binding.hFConsumerName.editText?.setText(healthData.hfCFullName)
                        binding.atnoOfStaff.editText?.setText(healthData.hfTotalStaff)
                        binding.hFNoOfBeds.editText?.setText(healthData.hfNoBeds)
                        binding.atNoOfQuarters.editText?.setText(healthData.hfNoQuraters)
                        binding.hFdisQuarters.editText?.setText(healthData.hfDistanceQuarter)
                        binding.atAmbulance.setText(healthData.hfAmbulance,false)
                        binding.atDrinkingWater.setText(healthData.hfDrinkingWater,false)
                        binding.atAgeOfTheBuilding.editText?.setText(healthData.hfAgeBuilding)
                        binding.athFMajorRenovation.setText(healthData.hfRenovation,false)
                        binding.hFNoOFFloors.editText?.setText(healthData.hfNoFloors)
                        binding.hFNoBuildings.editText?.setText(healthData.hfNoBuilding)
                        binding.hFTempLocation.editText?.setText(healthData.hfTemperature)
                        binding.hFdisComPlace.editText?.setText(healthData.hfDistanceCPlace)
                        binding.progressBar.visibility = View.GONE
                    }else{
                        isClick = false
                        binding.cardLayout.visibility=View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.hHDistrictName.visibility=View.VISIBLE
                        binding.tvAddressType.visibility=View.GONE
                        binding.hFName.editText?.setText("")
                        binding.hFCategory.setText("")
                        binding.hHWelnesCenter.setText("")
                        binding.hHMobileNetwork.setText("")
                        binding.hHDistrictCate.setText("")
                        binding.hFAddLocDetails.editText?.setText("")
                        binding.hFPincode.editText?.setText("")
                        binding.hFContactPersonName.editText?.setText("")
                        binding.hFPersonEmail.editText?.setText("")
                        binding.hFContactNo.editText?.setText("")
                        binding.workingHrs.editText?.setText("")
                        binding.atExtendedWorking.setText("")
                        binding.atLogisticfacility.setText("")
                        binding.hFDismainRoad.editText?.setText("")
                        binding.atapproachFacility.setText("")
                        binding.nameElectrictyBoard.editText?.setText("")
                        binding.hFConsumerId.editText?.setText("")
                        binding.hFConsumerName.editText?.setText("")
                        binding.atnoOfStaff.editText?.setText("")
                        binding.hFNoOfBeds.editText?.setText("")
                        binding.atNoOfQuarters.editText?.setText("")
                        binding.hFdisQuarters.editText?.setText("")
                        binding.atAmbulance.setText("")
                        binding.atDrinkingWater.setText("")
                        binding.atAgeOfTheBuilding.editText?.setText("")
                        binding.athFMajorRenovation.setText("")
                        binding.hFNoOFFloors.editText?.setText("")
                        binding.hFNoBuildings.editText?.setText("")
                        binding.hFTempLocation.editText?.setText("")
                        binding.hFdisComPlace.editText?.setText("")
                    }


                }else{
                    HealthDetailsRepo.loading.value=true
                }



            }
        })


    }



    fun AddAuditApi(surveyId:Int,operationType:String){
        var userId:Int=SharePreference.getIntPref(requireActivity(),Constants.UserId)
        var userName:String?=SharePreference.getStringPref(requireActivity(),Constants.UserName)
        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,surveyId,operationType,"HealthDetails")

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
                    if(status){

                    }
                }


            }
        })
    }


}