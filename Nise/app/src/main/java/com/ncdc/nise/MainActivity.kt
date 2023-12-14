package com.ncdc.nise

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.ncdc.nise.databinding.ActivityMainBinding
import com.ncdc.nise.interfaces.FragPostionInterface
import com.ncdc.nise.ui.connected.fragment.EnergyAssementsFrag
import com.ncdc.nise.ui.connected.fragment.InputsAssementFrag
import com.ncdc.nise.ui.core.CoreActivity
import com.ncdc.nise.ui.fragment.ElectricalFragment
import com.ncdc.nise.ui.fragment.HealthDetailsFrag
import com.ncdc.nise.ui.fragment.HealthRelatedInfoFrag
import com.ncdc.nise.ui.fragment.SpaceAvailabiltyFrag
import com.ncdc.nise.ui.fragment.VaccineStorageFrag
import com.ncdc.nise.ui.fragment.WaterSourceFrag
import com.ncdc.nise.ui.survey.presentation.add.ViewSurveyorActivity
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference


class MainActivity : CoreActivity(), FragPostionInterface {

    private lateinit var binding: ActivityMainBinding
    private var pos = 0
    var surveyId: String? = null
    var isEdit:Boolean=false
    val fragment=HealthDetailsFrag()
    val electricalFragment=ElectricalFragment()
    val spaceAvailabiltyFrag=SpaceAvailabiltyFrag()


    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerInternetConnectionReceiver()

        val bundle = intent.extras
        if (bundle != null) {
            surveyId = bundle.getString("surveyId")
            isEdit=bundle.getBoolean("isEdit")

            var userId:Int=Integer.parseInt(surveyId)
            SharePreference.setIntPref(this@MainActivity, Constants.editSurveyId,userId)
            SharePreference.setBooleanPref(this@MainActivity, Constants.isEdit,isEdit)
        }
            binding.stepView.done(false)

            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.frameLayout, HealthDetailsFrag())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()


            binding.stepView.setOnStepClickListener {
                when (pos) {
                    0 -> {
                        Log.d("StepClick", "00000000")
                    }

                    1 -> {

                        pos = 0
                        binding.stepView.done(false)
                        binding.stepView.go(pos, true)
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frameLayout, fragment)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()

                    }

                    2 -> {
                        pos = 1
                        binding.stepView.done(false)
                        binding.stepView.go(pos, true)

                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frameLayout, electricalFragment)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()


                    }

                    3 -> {
                        pos = 2
                        binding.stepView.done(false)
                        binding.stepView.go(pos, true)

                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frameLayout, spaceAvailabiltyFrag)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()

                    }

                    4 -> {
                        pos = 3
                        binding.stepView.done(false)
                        binding.stepView.go(pos, true)

                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frameLayout, WaterSourceFrag())
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }

                    5 -> {
                        pos = 4
                        binding.stepView.done(false)
                        binding.stepView.go(pos, true)

                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frameLayout, VaccineStorageFrag())
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }

                    6 -> {
                        pos = 5
                        binding.stepView.done(false)
                        binding.stepView.go(pos, true)

                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frameLayout, HealthRelatedInfoFrag())
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }

                    7 -> {
                        pos = 6
                        binding.stepView.done(false)
                        binding.stepView.go(pos, true)

                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frameLayout, EnergyAssementsFrag())
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }

                    8 -> {
                        pos = 7
                        binding.stepView.done(false)
                        binding.stepView.go(pos, true)

                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.frameLayout, InputsAssementFrag())
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }
                }
            }
    }
    override fun onItemClick(position: Int){
        when(position){
            0 -> {
                pos= 0
                binding.stepView.done(false)
                binding.stepView.go(pos, true)

            }
            1 -> {

                pos= 1
                binding.stepView.done(false)
                binding.stepView.go(pos, true)


            }
            2 -> {

                pos = 2
                binding.stepView.done(false)
                binding.stepView.go(pos, true)

            }
            3 -> {

                pos = 3
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
            }
            4 -> {

                pos = 4
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
            }
            5 -> {

                pos = 5
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
            }
            6 -> {

                pos = 6
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
            }
            7 -> {

                pos = 7
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
            }
            8 -> {

                pos = 8
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
            }else -> {
                pos = 0
                binding.stepView.done(true)
                binding.stepView.go(8, true)

                // Go to another Activity or Fragment
            }

        }
    }
    @SuppressLint("SetTextI18n")
    override fun onBackPressed() {
        when(pos){
            0 ->{
                SharePreference.setBooleanPref(this@MainActivity,Constants.isBackPressed,true)
                val intent= Intent(this@MainActivity, ViewSurveyorActivity::class.java)
                startActivity(intent)
                finish()

            }
            1 ->{

                pos = 0
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
                SharePreference.setBooleanPref(this@MainActivity,Constants.isBackPressed,true)

                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout, HealthDetailsFrag())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()

            }
            2 ->{
                pos = 1
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
                SharePreference.setBooleanPref(this@MainActivity,Constants.isBackPressed,true)
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout, ElectricalFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            3 ->{
                pos = 2
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
                SharePreference.setBooleanPref(this@MainActivity,Constants.isBackPressed,true)
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout, SpaceAvailabiltyFrag())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            4 ->{
                pos = 3
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
                SharePreference.setBooleanPref(this@MainActivity,Constants.isBackPressed,true)
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout, WaterSourceFrag())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            5 ->{
                pos = 4
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
                SharePreference.setBooleanPref(this@MainActivity,Constants.isBackPressed,true)
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout, VaccineStorageFrag())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            6 ->{
                pos = 5
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
                SharePreference.setBooleanPref(this@MainActivity,Constants.isBackPressed,true)
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout, HealthRelatedInfoFrag())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            7 ->{
                pos = 6
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
                SharePreference.setBooleanPref(this@MainActivity,Constants.isBackPressed,true)
              /*  val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout, PhotographsFrag())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()*/
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout, EnergyAssementsFrag())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            8 ->{
                pos = 7
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
                SharePreference.setBooleanPref(this@MainActivity,Constants.isBackPressed,true)
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout, InputsAssementFrag())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            /*9 ->{
                pos = 8
                binding.stepView.done(false)
                binding.stepView.go(pos, true)
                SharePreference.setBooleanPref(this@MainActivity,Constants.isBackPressed,true)
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout, InputsAssementFrag())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }*/


        }
    }



}
