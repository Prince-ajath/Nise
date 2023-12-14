package com.ncdc.nise.ui.photographs

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncdc.nise.data.model.SurveyorResponse
import com.ncdc.nise.data.remote.RetrofitClient
import com.ncdc.nise.databinding.ActivityPhotographsBinding
import com.ncdc.nise.ui.photographs.adapter.ImageAdapter
import com.ncdc.nise.ui.survey.model.updateResponse
import com.ncdc.nise.ui.survey.presentation.add.ViewSurveyorActivity
import com.ncdc.nise.utils.Constants
import com.ncdc.nise.utils.SharePreference
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.ThreadLocalRandom


class PhotographsActvity:AppCompatActivity() {
    lateinit var binding:ActivityPhotographsBinding
    private val PICK_IMAGE_CAMERA = 1
    private val PICK_IMAGE_GALLERY = 2
    var destination:File?=null
    var list: ArrayList<File> = ArrayList()
    var adapter:ImageAdapter?=null
    var surveyId:String?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotographsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras

        if (bundle != null) {
            surveyId = bundle.getString("surveyId")

        }


        binding.cvUploadFront.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)==PackageManager.PERMISSION_DENIED ){
                    val permissions = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else{
                    selectImage()

                }
            }else{
                selectImage()

            }
        }
        binding.tvUpload.setOnClickListener{
            imageUploadApi(surveyId!!)
        }

    }

    private fun selectImage() {
        try {

                val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
                val builder: AlertDialog.Builder =  AlertDialog.Builder(this)
                builder.setTitle("Select Option")
                builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
                    if (options[item] == "Take Photo") {
                        dialog.dismiss()
                        cameraIntent()
                    } else if (options[item] == "Choose From Gallery") {
                        dialog.dismiss()
                        list.clear()
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent, "Selcet Picture"),PICK_IMAGE_GALLERY)

                    } else if (options[item] == "Cancel") {
                        dialog.dismiss()
                    }
                })
                builder.show()

        } catch (e: Exception) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
    private fun cameraIntent() {
        list.clear()// get picture
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            destination = createImageFile()
            if (destination != null) {
                val photoURI = FileProvider.getUriForFile(
                    this,
                    "com.ncdc.nise.fileprovider",
                    destination!!
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        startActivityForResult(intent, PICK_IMAGE_CAMERA)
    }


    private fun createImageFile(): File {
      //  val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName ="profile"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,  // prefix
            ".jpg",  // suffix
            storageDir // directory
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PICK_IMAGE_CAMERA) {
            list.add(destination!!.getAbsoluteFile())
            if(list.size >0){
                binding.rvImageView.layoutManager = LinearLayoutManager(this)
                binding.rvImageView.setHasFixedSize(true)
                adapter = ImageAdapter(this,list)
                binding.rvImageView.adapter = adapter

            }

        } else if (requestCode === PICK_IMAGE_GALLERY) {
            if (data!!.clipData != null) {
                val x = data.clipData!!.itemCount
                for (i in 0 until x) {
                    val uri = data.clipData!!.getItemAt(i).uri
                    if (uri != null) {
                        try {
                            destination = copyUriToExternalFilesDir(data.clipData!!.getItemAt(i).uri, ThreadLocalRandom.current().nextInt(0, 100001)
                                .toString()+"profile.jpg")
                            /*destination = copyUriToExternalFilesDir(
                                data.clipData!!.getItemAt(i).uri,
                                "PROFILE_PICTURE.jpg" + ThreadLocalRandom.current().nextInt(0, 100001)
                                    .toString()
                            )*/
                            if (destination != null) {
                                list.add(destination!!)
                                binding.rvImageView.layoutManager = LinearLayoutManager(this)
                                binding.rvImageView.setHasFixedSize(true)
                                adapter = ImageAdapter(this,list)
                                binding.rvImageView.adapter = adapter

                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            } else if (data.data != null) {
                try {
                    destination = copyUriToExternalFilesDir(data.data!!, ThreadLocalRandom.current().nextInt(0, 100001)
                        .toString()+"profile.jpg")
                    /*destination = copyUriToExternalFilesDir(
                        data.data!!,
                        "PROFILE_PICTURE.jpg" + ThreadLocalRandom.current().nextInt(0, 100001)
                            .toString()
                    )*/
                    if (destination != null) {
                        list.add(destination!!)
                        binding.rvImageView.layoutManager = LinearLayoutManager(this)
                        binding.rvImageView.setHasFixedSize(true)
                        adapter = ImageAdapter(this,list)
                        binding.rvImageView.adapter = adapter
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }
    }

    private fun copyUriToExternalFilesDir(uri: Uri, fileName: String): File? {
        var filePath = File("")
        var inputStream: InputStream? = null
        try {
            inputStream = contentResolver.openInputStream(uri)
            val tempDir = getExternalFilesDir("temp")
            if (inputStream != null && tempDir != null) {
                filePath = File(tempDir, fileName)
                val fos = FileOutputStream(filePath)
                val bis = BufferedInputStream(inputStream)
                val bos = BufferedOutputStream(fos)
                val buffer = ByteArray(1024)
                var read: Int
                while (bis.read(buffer).also { read = it } != -1) {
                    bos.write(buffer, 0, read)
                }
                bos.close()
                fos.close()
                Log.e("copyExternalFilesDir", "" + filePath)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return filePath
    }


    companion object {
        private val PERMISSION_CODE = 1001
    }

    fun imageUploadApi(surveyId:String) {

            binding.progressBar.visibility=View.VISIBLE
            binding.tvUpload.visibility=View.GONE
            val multipartTypedOutput: MutableList<MultipartBody.Part> =ArrayList()
            for (index in list.indices) {
                val file = File(list[index].absolutePath)
                val requestBody=file.asRequestBody("image/*".toMediaTypeOrNull())
                multipartTypedOutput.add(createFormData("files[]", file.path, requestBody))

            }
            val surveyId:RequestBody=RequestBody.create(MultipartBody.FORM, surveyId)
            val call = RetrofitClient.apiInterface.imageUploadApi(surveyId,multipartTypedOutput)

            call.enqueue(object: Callback<updateResponse> {

                override fun onFailure(call: Call<updateResponse>, t: Throwable) {
                    Log.v("DEBUG : ", t.message.toString())
                    binding.progressBar.visibility=View.GONE
                    binding.tvUpload.visibility=View.VISIBLE
                }

                override fun onResponse(
                    call: Call<updateResponse>,
                    response: Response<updateResponse>
                ) {
                    if (response.isSuccessful) {
                        binding.progressBar.visibility=View.GONE
                        binding.tvUpload.visibility=View.VISIBLE
                        val data = response.body()
                        val status=data!!.status
                        val msg=data.message
                        if(status){
                            AddAuditApi()
                            Toast.makeText(this@PhotographsActvity, msg, Toast.LENGTH_LONG).show()
                            val intent=Intent(this@PhotographsActvity, ViewSurveyorActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }else{
                        binding.progressBar.visibility=View.GONE
                        binding.tvUpload.visibility=View.VISIBLE
                    }
                }
            })

        }
    fun AddAuditApi(){
        var userId:Int= SharePreference.getIntPref(this@PhotographsActvity, Constants.UserId)
        var userName:String?= SharePreference.getStringPref(this@PhotographsActvity, Constants.UserName)
        var surveyId:Int= SharePreference.getIntPref(this@PhotographsActvity, Constants.SurveyId)
        val call = RetrofitClient.apiInterface.isAuditPage(userId,userName,surveyId,"UPLOAD","PHOTOGRAPHS")

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
                }


            }
        })
    }
}


