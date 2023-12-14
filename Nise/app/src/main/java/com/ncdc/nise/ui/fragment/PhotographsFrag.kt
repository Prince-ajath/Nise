package com.ncdc.nise.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ncdc.nise.R
import com.ncdc.nise.databinding.FragmentPhotographsBinding
import com.ncdc.nise.interfaces.FragPostionInterface
import com.ncdc.nise.ui.connected.fragment.EnergyAssementsFrag
import com.ncdc.nise.ui.survey.presentation.add.ViewSurveyorActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class PhotographsFrag:Fragment() {

    lateinit var binding: FragmentPhotographsBinding
    var listener: FragPostionInterface? = null
    private val SELECT_FILE = 201
    private val REQUEST_CAMERA = 202
    private var mSelectedFileImg: File? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotographsBinding.inflate(inflater, container, false)
        val view = binding.root
        listener= activity as FragPostionInterface


        binding.tvNextPhotographs.setOnClickListener {

            listener?.onItemClick(7)
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.frameLayout, EnergyAssementsFrag())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.rgBuildBoard.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)
            when (radio) {
                binding.rbBuildBoardYes -> {
                    binding.cvUploadFront.visibility=View.VISIBLE
                }
                binding.rbBuildBoardNo -> {
                    binding.cvUploadFront.visibility=View.GONE
                }
            }
        }

        binding.rgMeterConn.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)
            when (radio) {
                binding.rbMeterConnYes -> {
                    binding.cvUploadFront.visibility=View.GONE
                    binding.cvUploadFrontMc.visibility=View.VISIBLE
                }
                binding.rbMeterConnNo -> {
                    binding.cvUploadFront.visibility=View.GONE
                    binding.cvUploadFrontMc.visibility=View.GONE
                }
            }
        }
        binding.cvUploadFront.setOnClickListener {
            getExternalStoragePermission()
        }



        return view


    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data)
            }
        }
    }

    private fun getExternalStoragePermission() {
        Dexter.withActivity(requireActivity())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {

                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_PICK
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                       // Common.settingDialog(requireActivity())
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .onSameThread()
            .check()
    }

  /*  @SuppressLint("InlinedApi")
    fun imageSelectDialog(act: Activity) {
        var dialog: Dialog? = null
        try {
            if (dialog != null) {
                dialog.dismiss()
            }
            dialog = Dialog(act, R.style.AppCompatAlertDialogStyleBig)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            val mInflater = LayoutInflater.from(act)
            val mView = mInflater.inflate(R.layout.dlg_externalstorage, null, false)

            val finalDialog: Dialog = dialog
            mView.tvSetImageCamera.setOnClickListener {
                finalDialog.dismiss()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(
                    intent,
                    REQUEST_CAMERA
                )
            }
            mView.tvSetImageGallery.setOnClickListener {
                finalDialog.dismiss()

            }
            dialog.setContentView(mView)
            if (!act.isFinishing) dialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }*/

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    private fun onSelectFromGalleryResult(data: Intent?) {
        var bm: Bitmap? = null
        if (data != null) {
            try {
               /* bm  = when {
                    Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        data.data
                    )
                    else -> {
                        val source = ImageDecoder.createSource(this.contentResolver, data.data!!)
                        ImageDecoder.decodeBitmap(source)
                    }
                }*/
                data.data
                val bytes = ByteArrayOutputStream()
              
                mSelectedFileImg = File(
                    Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis().toString() + ".jpg"
                )
                val fo: FileOutputStream
                try {
                    mSelectedFileImg!!.createNewFile()
                    fo = FileOutputStream(mSelectedFileImg!!)
                    fo.write(bytes.toByteArray())
                    fo.close()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        binding.llUploadFront.visibility=View.GONE
        binding.rlUploadCompleteFront.visibility=View.VISIBLE
        binding.imgUploadCompletedFront.setImageBitmap(bm)
    }

  /*  @Suppress("DEPRECATION")
    private fun onCaptureImageResult(data: Intent) {
        val thumbnail = data.extras!!["data"] as Bitmap?
        val bytes = ByteArrayOutputStream()
        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

        mSelectedFileImg = File(
            Environment.getExternalStorageDirectory(),
            System.currentTimeMillis().toString() + ".jpeg"
        )
        val fo: FileOutputStream
        try {
            mSelectedFileImg!!.createNewFile()
            fo = FileOutputStream(mSelectedFileImg)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Glide.with(this@AdvertismentActivity)
            .load(Uri.parse("file://" + mSelectedFileImg!!.getPath()))
            .into(imgUploadCompletedFront)
        llUploadFront.visibility=View.GONE
        rlUploadCompleteFront.visibility=View.VISIBLE
    }*/



}