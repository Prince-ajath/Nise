package com.ncdc.nise.data.remote

import com.ncdc.nise.data.model.GetWaterSourceResponse
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.model.electrical.getElectricalResponse
import com.ncdc.nise.data.model.energyAssments.GetEnergyAssments
import com.ncdc.nise.data.model.health.HealthDetailsModel
import com.ncdc.nise.data.model.healthrelatedinfo.GetHealthInfoResponse
import com.ncdc.nise.data.model.inputsAssments.GetInputAssments
import com.ncdc.nise.data.model.spaceavilability.GetSpaceResponse
import com.ncdc.nise.data.model.vaccineStorage.GetVaccineStorageResponse
import com.ncdc.nise.ui.load.model.load.LoadItemResponse
import com.ncdc.nise.ui.register.model.AuthResponse
import com.ncdc.nise.ui.survey.model.getSurveyDetailsResponse
import com.ncdc.nise.ui.survey.model.updateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {


    @FormUrlEncoded
    @POST("add_survey.php")
     fun setSurveyor(
        @Field("sname") sName: String?,
        @Field("sdesignation") sDesignation: String?,
        @Field("sorganization") sOrigination: String?,
        @Field("sdate") sDate: String?,
        @Field("stime") sTime: String?,
        @Field("scontactno") sContactNo: String?,
        @Field("sotherinfo") sOtherInfo: String?,
    ): Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("signup.php")
    fun register(
        @Field("username") sName: String?,
        @Field("email") sEmail:String?,
        @Field("designation") sDesignation: String?,
        @Field("organization") sOrigination: String?,
        @Field("contactno") sContactNo: String?,
        @Field("password") sPassword: String?,
        @Field("status")sActive:Int?

    ): Call<AuthResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") sEmail:String?,
        @Field("password") sPassword: String?,

        ): Call<AuthResponse>



    @FormUrlEncoded
    @POST("addHealth.php")
    fun addHealthDetailsApi(
        @Field("surveyorId")surveyorId:Int?,
        @Field("hfName")hfName:String?,
        @Field("hfCategory")hfCategory:String?,
        @Field("hfWellnessCentre")hfWellnessCentre:String?,
        @Field("hfDistrictName")hfDistrictName:String?,
        @Field("hfStateName")hfStateName:String?,
        @Field("hfMobileNetwork")hfMobileNetwork:String?,
        @Field("hfDistrictCate")hfDistrictCate:String?,
        @Field("hfAddressLocation")hfAddressLocation:String?,
        @Field("hfLattitude")hfLattitude:String?,
        @Field("hfLongitude")hfLongitude:String?,
        @Field("hfCountry")hfCountry:String?,
        @Field("hfPincode")hfPincode:String?,
        @Field("hfCPName")hfCPName:String?,
        @Field("hfCPEmail")hfCPEmail:String?,
        @Field("hfCPMobile")hfCPMobile:String?,
        @Field("hfWorkinghours")hfWorkinghours:String?,
        @Field("hfStartTime")hfStartTime:String?,
        @Field("hfEndTime")hfEndTime:String?,
        @Field("hfEWorkinghours")hfEWorkinghours:String?,
        @Field("hfLogisticfacility")hfLogisticfacility:String?,
        @Field("hfMainRoadDist")hfMainRoadDist:String?,
        @Field("hfApproachSite")hfApproachSite:String?,
        @Field("hfEBoardName")hfEBoardName:String?,
        @Field("hfConsumerid")hfConsumerid:String?,
        @Field("hfCFullName")hfCFullName:String?,
        @Field("hfTotalStaff")hfTotalStaff:String?,
        @Field("hfNoBeds")hfNoBeds:String?,
        @Field("hfNoQuraters")hfNoQuraters:String?,
        @Field("hfDistanceQuarter")hfDistanceQuarter:String?,
        @Field("hfAmbulance")hfAmbulance:String?,
        @Field("hfDrinkingWater")hfDrinkingWater:String?,
        @Field("hfAgeBuilding")hfAgeBuilding:String?,
        @Field("hfRenovation")hfRenovation:String?,
        @Field("hfNoFloors")hfNoFloors:String?,
        @Field("hfNoBuilding")hfNoBuilding:String?,
        @Field("hfTemperature")hfTemperature:String?,
        @Field("hfDistanceCPlace")hfDistanceCPlace:String?,
        @Field("state_id") hfStateId:Int?,
        @Field("status") hfstatus:Int?,

    ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("add_ElectricalParameter.php")
    fun addElectricalDetailsApi(
        @Field("surveyId") surveyId:Int?,
        @Field("access_electricity") epAccess:String?,
        @Field("electricity_conn") epConnection:String?,
        @Field("electricity_bill") epBill:String?,
        @Field("peak_load_ebill") epPeakLoadBill:String?,
        @Field("connection_type") epConnctionType:String?,
        @Field("average_electricity") epAverage:String?,
        @Field("availb_main_line") epAvlMainLine:String?,
        @Field("voltage")epVoltage:String?,
        @Field("average_power") epAvgPower:String?,
        @Field("psource_supply") epPSSupply:String?,
        @Field("ssource_supply") epSSSupply:String?,
        @Field("meterconn_available") epMtrConnAvlbl:String?,
        @Field("ac_voltage") epAcVltag:String?,
        @Field("expansion_loads") epExpnLoads:String?,
        @Field("outdoor_lightings") epOutdrsLightng:String?,
        @Field("solar_system") epSolrSystm:String?,
        @Field("econsumption") epConsumption:String?,
        @Field("provisions_existing") epPrvsionsExistng:String?,
        @Field("energy_efficiency") epEnrgyEffcncy:String?,
        @Field("power_factor") epPwrFactr:String?,
        @Field("distribution_boards") epDistbtionBord:String?,
        @Field("noof_mcbs_five_ten") epNoMcbFive:String?,
        @Field("noof_mcbs_ten_uper") epNoMCBUptoTen:String?,
        @Field("mcbs_uptotwenty") epNoMCBUpToTwnty:String?,
        @Field("condition_wiring") epWiringConditns:String?,
        ):Call<SurveyorResponse>


    @FormUrlEncoded
    @POST("addSpaceAvilability.php")
    fun addSpaceAvilabilty(
        @Field("surveyId") surveyId:Int?,
        @Field("free_area_roof") spFreeAreaRoof:String?,
        @Field("open_ground") spOpenGround:String?,
        @Field("space_to_host")spHost:String?,
        @Field("secure_storage") spScurStorage:String?,
        @Field("available_earthing") spAvlblErthng:String?,
        @Field("la_installation") spInstallation:String?,
        @Field("sa_cpu") spCpu:String?,
        @Field("solar_pv_system") spSlrPVSystm:String?,
        @Field("theft_at_site") spTheftSite:String?,
        @Field("installation_location") spInstltionLoc:String?,
        @Field("type_of_roof") spTypeRoof:String?,
        @Field("orientation") spOrientation:String?,
        @Field("tilt_angle")spTiltAngle:String,
        @Field("total_area") spTotlArea:String?,
        @Field("total_shadow") spTotalShadow:String?,
        @Field("obstructions") spObstructions:String?,
        @Field("general_shape") spGnrlShape:String?,
        @Field("access_roof") spAccessRoof:String?,
        @Field("weight_restrictions") spWeightRestricton:String?,
        @Field("obstruction") spObstruction:String?,
        @Field("construction") spConstruction:String?,
        @Field("econdition") spEcondition:String?,
        @Field("roof_condition") spRoofConditons:String?,
        @Field("support_condition") spSupportConditons:String?,
        @Field("roof_material") spRoofMtrial:String?,
    ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("add_WaterSource.php")
    fun addWaterDetails(
        @Field("surveyId") surveyId:Int?,
        @Field("water_drinking") wDrinking:String?,
        @Field("water_storage") wStorage:String?,
        @Field("pumping_mechanism")wPumpingMechnism:String?,
        @Field("motor_pump_set") wMoterPumpSet:String?,
        @Field("operational_hours") wOperationalHrs:String?,
        @Field("qw_required") wQWReqiurd:String?,
        @Field("quality_water") wQuality:String?,
        @Field("water_table") wTable:String?,
        @Field("water_tank") wTank:String?,
        @Field("availability_water") wAvlbility:String?,

    ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("add_VaccineStorage.php")
    fun addVaccineDetails(
        @Field("surveyId") surveyId:Int?,
        @Field("refrigerator_available") vRefrigrotrAvl:String?,
        @Field("refrigerator_working") vRefrigrotrWorking:String?,
        @Field("solar_powered")vSolorPwrd:String?,
        @Field("temperature_maintained") vTempMentained:String?,
        @Field("usage_pattern") vUsagePattern:String?,

    ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("add_HealthRelatedInfo.php")
    fun addHealthRelatedInfo(
        @Field("surveyId") surveyId:Int?,
        @Field("total_papulation") hrTtlPapulation:String?,
        @Field("annual_target") hrAnnualTarget:String?,
        @Field("pregnant_women")hrPrgnentWomanTarget:String,
        @Field("averageno_opd")hrAverageNoOpd:String?,
        @Field("total_ipd") hrTtlIpd:String?,
        @Field("averageno_surgeries") hrAverageSurgeries:String?,
        @Field("averageno_deliveries") hrAvrNoDeliveries:String?,
        @Field("emergency_services") hrEmServices:String?,
        @Field("opd_services") hrOpdServices:String?,
        @Field("anti_rabies_vaccine") hrAntiRabiesVaccine:String?,
        @Field("babies_managed") hrBabiesManaged:String?,
        @Field("immunization") hrImmunization:String?,
        @Field("blood_examination") hrBloodExamination:String?,
        @Field("tele_medicine") hrTeleMedicine:String?,
        @Field("blood_storage") hrBloodStorage:String?,
        @Field("cold_chain") hrColdChain:String?,
        @Field("noof_ipds") hrNoofIpds:String?,
    ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("add_ConnectedLoad.php")
    fun addConnectedLoad(
        @Field("surveyId") surveyId:Int?,
        @Field("cl_category") clCategory:String?,
        @Field("cl_name") clName:String?,
        @Field("cl_wattage")clWattage:String?,
        @Field("cl_quantity") clQuantity:String?,
        @Field("cl_operational_day") clOperationalDay:String?,
        @Field("cl_operational_night") clOperationalNight:String?,
        @Field("critical_load") clCriticalLoad:String?,
        @Field("load_type") clLoadType:String?,
        @Field("energy_rating") clEnergyRating:String?,
        @Field("year_installation") clYearInstallation:String?,
    ):Call<SurveyorResponse>

 @FormUrlEncoded
 @POST("add_EnergyAssesment.php")
 fun addEnergyAssment(
     @Field("surveyId") surveyId:Int?,
     @Field("fuels_used") enFuelsUsed:String?,
     @Field("total_built_up_area") enTtlBuiltUpArea:String?,
     @Field("total_no_buildings")enTtlNoBuilding:String?,
     @Field("total_no_floors") enTtlNoFloors:String?,
     @Field("total_no_windows") enTtlNoWimdows:String?,
     @Field("total_install_capacity") enTtlInstlCapcity:String?,
     @Field("hot_water_consumption") enHotWtrConsumption:String?,
     @Field("thermostatic") enThermostatic:String?,
     @Field("fan_speed") enFanSpeed:String?,
     @Field("incandescent") enIncandescent:String?,
     @Field("equipments") enEquipments:String?,
     @Field("rooms") enRooms:String?,
     @Field("sensors") enSensors:String?,
     @Field("labelling") enLabelling:String?,
     @Field("refrigerators") enRefrigerators:String?,
     @Field("insulated_well") enInsulatedWell:String?,
     @Field("lighting_load") enLightingLoad:String?,
     @Field("energy_usage") enEnergyUsage:String?,
     @Field("energy_efficiency") enEnergyEfficiency:String?,
     @Field("lighting_circuits") enLightingCircuits:String?,
     @Field("humidity") enHumidity:String?,

 ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("add_EnergyAssSecond.php")
    fun addInputAssment(
        @Field("surveyId") surveyId:Int?,
        @Field("refrigerators_rating") InRefrigeratorsRating:String?,
        @Field("airconditioners_rating") InAirconditionersRating:String?,
        @Field("water_pump")InWaterPump:String?,
        @Field("electrical_loads") InElectricalLoads:String?,
        @Field("hcf") InHcf:String?,
        @Field("conservation") InConservation:String?,
        @Field("mequipment") InMequipment:String?,
        @Field("low_energy") InLowEnergy:String?,
        @Field("lighting_control") InLightingControl:String?,
        @Field("energy_efficient") InEnergyEfficient:String?,
        @Field("strategies") InStrategies:String?,
        @Field("carbon_features") InCarbonFeatures:String?,
        @Field("additional_remarks") InAditionalRemarks:String?
        ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("getCLoadData.php")
    fun getLoadData(@Field("surveyid") cLoadId:Int):Call<LoadItemResponse>

    @FormUrlEncoded
    @POST("getSurveyDetails.php")
    fun getSurveyData(@Field("surveyorId") surveyorId:Int):Call<getSurveyDetailsResponse>

    @FormUrlEncoded
    @POST("loadDelete.php")
    fun deleteLoad(@Field("load_id") cLoadId:Int):Call<SurveyorResponse>

    @GET("getHealthDetails.php")
    fun getHealthDetails(@Query("surveyId") surveyId:Int):Call<HealthDetailsModel>

    @GET("getElectricalData.php")
    fun getElectricalData(@Query("surveyId") surveyId:Int):Call<getElectricalResponse>

    @GET("getSpaceAvilabilty.php")
    fun getSpaceAvilabiltiy(@Query("surveyId") surveyId:Int):Call<GetSpaceResponse>

    @GET("getWaterSource.php")
    fun getWaterSource(@Query("surveyId") surveyId:Int):Call<GetWaterSourceResponse>

    @GET("getVaccineStorage.php")
    fun getVacination(@Query("surveyId") surveyId:Int):Call<GetVaccineStorageResponse>

    @GET("getHealthRelatedInfo.php")
    fun getHealthRelatedInfo(@Query("surveyId") surveyId:Int):Call<GetHealthInfoResponse>

    @GET("getEnergyAssment.php")
    fun getEnergyAssmentApi(@Query("surveyId") surveyId:Int):Call<GetEnergyAssments>



    @GET("getInputAssements.php")
    fun getInputAssment(@Query("surveyId") surveyId:Int):Call<GetInputAssments>

    @FormUrlEncoded
    @POST("updateHealthDetails.php")
    fun updateHealthDetailsApi(
        @Field("surveyId")surveyId:Int?,
        @Field("surveyorId")surveyorId:Int?,
        @Field("hfName")hfName:String?,
        @Field("hfCategory")hfCategory:String?,
        @Field("hfWellnessCentre")hfWellnessCentre:String?,
        @Field("hfDistrictName")hfDistrictName:String?,
        @Field("hfStateName")hfStateName:String?,
        @Field("hfMobileNetwork")hfMobileNetwork:String?,
        @Field("hfDistrictCate")hfDistrictCate:String?,
        @Field("hfAddressLocation")hfAddressLocation:String?,
        @Field("hfLattitude")hfLattitude:String?,
        @Field("hfLongitude")hfLongitude:String?,
        @Field("hfCountry")hfCountry:String?,
        @Field("hfPincode")hfPincode:String?,
        @Field("hfCPName")hfCPName:String?,
        @Field("hfCPEmail")hfCPEmail:String?,
        @Field("hfCPMobile")hfCPMobile:String?,
        @Field("hfWorkinghours")hfWorkinghours:String?,
        @Field("hfStartTime")hfStartTime:String?,
        @Field("hfEndTime")hfEndTime:String?,
        @Field("hfEWorkinghours")hfEWorkinghours:String?,
        @Field("hfLogisticfacility")hfLogisticfacility:String?,
        @Field("hfMainRoadDist")hfMainRoadDist:String?,
        @Field("hfApproachSite")hfApproachSite:String?,
        @Field("hfEBoardName")hfEBoardName:String?,
        @Field("hfConsumerid")hfConsumerid:String?,
        @Field("hfCFullName")hfCFullName:String?,
        @Field("hfTotalStaff")hfTotalStaff:String?,
        @Field("hfNoBeds")hfNoBeds:String?,
        @Field("hfNoQuraters")hfNoQuraters:String?,
        @Field("hfDistanceQuarter")hfDistanceQuarter:String?,
        @Field("hfAmbulance")hfAmbulance:String?,
        @Field("hfDrinkingWater")hfDrinkingWater:String?,
        @Field("hfAgeBuilding")hfAgeBuilding:String?,
        @Field("hfRenovation")hfRenovation:String?,
        @Field("hfNoFloors")hfNoFloors:String?,
        @Field("hfNoBuilding")hfNoBuilding:String?,
        @Field("hfTemperature")hfTemperature:String?,
        @Field("hfDistanceCPlace")hfDistanceCPlace:String?,
        @Field("state_id") hfStateId:Int?,
        @Field("status") hfstatus:Int?,
        ):Call<SurveyorResponse>


    @FormUrlEncoded
    @POST("updateElectricalData.php")
    fun updateElectricalDetailsApi(
        @Field("surveyId") surveyId:Int?,
        @Field("access_electricity") epAccess:String?,
        @Field("electricity_conn") epConnection:String?,
        @Field("electricity_bill") epBill:String?,
        @Field("peak_load_ebill") epPeakLoadBill:String?,
        @Field("connection_type") epConnctionType:String?,
        @Field("average_electricity") epAverage:String?,
        @Field("availb_main_line") epAvlMainLine:String?,
        @Field("voltage")epVoltage:String?,
        @Field("average_power") epAvgPower:String?,
        @Field("psource_supply") epPSSupply:String?,
        @Field("ssource_supply") epSSSupply:String?,
        @Field("meterconn_available") epMtrConnAvlbl:String?,
        @Field("ac_voltage") epAcVltag:String?,
        @Field("expansion_loads") epExpnLoads:String?,
        @Field("outdoor_lightings") epOutdrsLightng:String?,
        @Field("solar_system") epSolrSystm:String?,
        @Field("econsumption") epConsumption:String?,
        @Field("provisions_existing") epPrvsionsExistng:String?,
        @Field("energy_efficiency") epEnrgyEffcncy:String?,
        @Field("power_factor") epPwrFactr:String?,
        @Field("distribution_boards") epDistbtionBord:String?,
        @Field("noof_mcbs_five_ten") epNoMcbFive:String?,
        @Field("noof_mcbs_ten_uper") epNoMCBUptoTen:String?,
        @Field("mcbs_uptotwenty") epNoMCBUpToTwnty:String?,
        @Field("condition_wiring") epWiringConditns:String?,
    ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("updateSpaceAvilability.php")
    fun updateSpaceAvilabilty(
        @Field("surveyId") surveyId:Int?,
        @Field("free_area_roof") spFreeAreaRoof:String?,
        @Field("open_ground") spOpenGround:String?,
        @Field("space_to_host")spHost:String?,
        @Field("secure_storage") spScurStorage:String?,
        @Field("available_earthing") spAvlblErthng:String?,
        @Field("la_installation") spInstallation:String?,
        @Field("sa_cpu") spCpu:String?,
        @Field("solar_pv_system") spSlrPVSystm:String?,
        @Field("theft_at_site") spTheftSite:String?,
        @Field("installation_location") spInstltionLoc:String?,
        @Field("type_of_roof") spTypeRoof:String?,
        @Field("orientation") spOrientation:String?,
        @Field("tilt_angle")spTiltAngle:String,
        @Field("total_area") spTotlArea:String?,
        @Field("total_shadow") spTotalShadow:String?,
        @Field("obstructions") spObstructions:String?,
        @Field("general_shape") spGnrlShape:String?,
        @Field("access_roof") spAccessRoof:String?,
        @Field("weight_restrictions") spWeightRestricton:String?,
        @Field("obstruction") spObstruction:String?,
        @Field("construction") spConstruction:String?,
        @Field("econdition") spEcondition:String?,
        @Field("roof_condition") spRoofConditons:String?,
        @Field("support_condition") spSupportConditons:String?,
        @Field("roof_material") spRoofMtrial:String?,
    ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("updateWaterSourse.php")
    fun updateWaterSource(
        @Field("surveyId") surveyId: Int?,
        @Field("water_drinking") wDrinking: String?,
        @Field("water_storage") wStorage: String?,
        @Field("pumping_mechanism") wPumpingMechnism: String?,
        @Field("motor_pump_set") wMoterPumpSet: String?,
        @Field("operational_hours") wOperationalHrs: String?,
        @Field("qw_required") wQWReqiurd: String?,
        @Field("quality_water") wQuality: String?,
        @Field("water_table") wTable: String?,
        @Field("water_tank") wTank: String?,
        @Field("availability_water") wAvlbility: String?,
    ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("updateVaccineStorage.php")
    fun updateVaccineDetails(
        @Field("surveyId") surveyId:Int?,
        @Field("refrigerator_available") vRefrigrotrAvl:String?,
        @Field("refrigerator_working") vRefrigrotrWorking:String?,
        @Field("solar_powered")vSolorPwrd:String?,
        @Field("temperature_maintained") vTempMentained:String?,
        @Field("usage_pattern") vUsagePattern:String?,

        ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("updateHealthRelatedInfo.php")
    fun updateHealthRelatedInfo(
        @Field("surveyId") surveyId:Int?,
        @Field("total_papulation") hrTtlPapulation:String?,
        @Field("annual_target") hrAnnualTarget:String?,
        @Field("pregnant_women")hrPrgnentWomanTarget:String,
        @Field("averageno_opd")hrAverageNoOpd:String?,
        @Field("total_ipd") hrTtlIpd:String?,
        @Field("averageno_surgeries") hrAverageSurgeries:String?,
        @Field("averageno_deliveries") hrAvrNoDeliveries:String?,
        @Field("emergency_services") hrEmServices:String?,
        @Field("opd_services") hrOpdServices:String?,
        @Field("anti_rabies_vaccine") hrAntiRabiesVaccine:String?,
        @Field("babies_managed") hrBabiesManaged:String?,
        @Field("immunization") hrImmunization:String?,
        @Field("blood_examination") hrBloodExamination:String?,
        @Field("tele_medicine") hrTeleMedicine:String?,
        @Field("blood_storage") hrBloodStorage:String?,
        @Field("cold_chain") hrColdChain:String?,
        @Field("noof_ipds") hrNoofIpds:String?,
    ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("updateEnergyAssments.php")
    fun updateEnergyAssment(
        @Field("surveyId") surveyId:Int?,
        @Field("fuels_used") enFuelsUsed:String?,
        @Field("total_built_up_area") enTtlBuiltUpArea:String?,
        @Field("total_no_buildings")enTtlNoBuilding:String?,
        @Field("total_no_floors") enTtlNoFloors:String?,
        @Field("total_no_windows") enTtlNoWimdows:String?,
        @Field("total_install_capacity") enTtlInstlCapcity:String?,
        @Field("hot_water_consumption") enHotWtrConsumption:String?,
        @Field("thermostatic") enThermostatic:String?,
        @Field("fan_speed") enFanSpeed:String?,
        @Field("incandescent") enIncandescent:String?,
        @Field("equipments") enEquipments:String?,
        @Field("rooms") enRooms:String?,
        @Field("sensors") enSensors:String?,
        @Field("labelling") enLabelling:String?,
        @Field("refrigerators") enRefrigerators:String?,
        @Field("insulated_well") enInsulatedWell:String?,
        @Field("lighting_load") enLightingLoad:String?,
        @Field("energy_usage") enEnergyUsage:String?,
        @Field("energy_efficiency") enEnergyEfficiency:String?,
        @Field("lighting_circuits") enLightingCircuits:String?,
        @Field("humidity") enHumidity:String?,

        ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("updateInputsAssements.php")
    fun updateInputAssment(
        @Field("surveyId") surveyId:Int?,
        @Field("refrigerators_rating") InRefrigeratorsRating:String?,
        @Field("airconditioners_rating") InAirconditionersRating:String?,
        @Field("water_pump")InWaterPump:String?,
        @Field("electrical_loads") InElectricalLoads:String?,
        @Field("hcf") InHcf:String?,
        @Field("conservation") InConservation:String?,
        @Field("mequipment") InMequipment:String?,
        @Field("low_energy") InLowEnergy:String?,
        @Field("lighting_control") InLightingControl:String?,
        @Field("energy_efficient") InEnergyEfficient:String?,
        @Field("strategies") InStrategies:String?,
        @Field("carbon_features") InCarbonFeatures:String?,
        @Field("additional_remarks") InAditionalRemarks:String?
    ):Call<SurveyorResponse>

    @FormUrlEncoded
    @POST("finalSubmission.php")
    fun isUpdateStatus(
        @Field("surveyId") surveyId:Int?,
        @Field("status") status: Int?,

        ): Call<updateResponse>

    @FormUrlEncoded
    @POST("auditTrail.php")
    fun isAuditPage(
        @Field("surveyor_id") surveyorId:Int?,
        @Field("surveyor_name") surveyorName: String?,
        @Field("survey_id") surveyId:Int?,
        @Field("operation_type") operationType: String?,
        @Field("operation_page") operationPage: String?): Call<SurveyorResponse>

    @Multipart
    @POST("imagesUpload.php")
    fun imageUploadApi(
        @Part("surveyId") surveyId: RequestBody?,
        @Part image:MutableList<MultipartBody.Part>):Call<updateResponse>
}
