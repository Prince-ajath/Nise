package com.ncdc.nise.ui.splashScreen

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ncdc.nise.R
import com.ncdc.nise.databinding.ActivitySplashScreenBinding
import com.ncdc.nise.ui.core.CoreActivity
import com.ncdc.nise.ui.login.LoginActivity
import com.ncdc.nise.ui.survey.presentation.add.ViewSurveyorActivity
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference

class SplashScreen :CoreActivity(){
    lateinit var binding:ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerInternetConnectionReceiver()


        Handler(Looper.getMainLooper()).postDelayed({
            if(!hasInternet){
                showSnackBar(getString(R.string.no_internet))

            }else{
                if(SharePreference.getBooleanPref(this@SplashScreen,Constants.isLogin)){
                    val intent = Intent(this, ViewSurveyorActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

        }, 2000)
    }
    private fun showSnackBar(title: String) {
        val snackBar = Snackbar.make(binding.rootView, title,
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackBar.setActionTextColor(Color.BLACK)
        snackBar.show()
    }
}