package com.example.meongku.ui.main.scan

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.meongku.R
import com.example.meongku.api.ApiService
import com.example.meongku.api.CatBreedAPI
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.ml.PredictionResponse
import com.example.meongku.databinding.ActivityScanBinding
import com.example.meongku.preference.UserPreferences
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var retrofitClient: RetrofitClient
    private val api: ApiService = CatBreedAPI.api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.captureImage.setOnClickListener {
            takePhoto()
        }

        binding.SwitchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
        binding.captureImage.isEnabled = false
        binding.SwitchCamera.isEnabled = false
    }

    private fun hideLoading() {
        binding.loading.visibility = View.GONE
        binding.captureImage.isEnabled = true
        binding.SwitchCamera.isEnabled = true
    }

    private fun takePhoto() {
        showLoading()
        Log.d("ScanActivity", "Taking photo...")
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.d("ScanActivity", "Error taking photo: ${exc.localizedMessage}")
                    Toast.makeText(
                        this@ScanActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.d("ScanActivity", "Photo saved.")
                    val myFile = photoFile
                    uploadImage(myFile)
                }
            }
        )
    }

    private fun startScanResultActivity(imagePath: String, isBackCamera: Boolean, predictedClass: String) {
        val resultIntent = Intent(this@ScanActivity, ScanResultActivity::class.java).apply {
            putExtra("picture", imagePath)
            putExtra("isBackCamera", isBackCamera)
            putExtra("predicted_class", predictedClass)
        }
        try {
            hideLoading()
            Log.d("ScanActivity", "Starting ScanResultActivity with image path: $imagePath and predicted class: $predictedClass")
            startActivity(resultIntent)
        } catch (e: Exception) {
            Log.d("ScanActivity", "Error starting ScanResultActivity: ${e.localizedMessage}")
        }
    }

    private fun startCamera() {
        Log.d("ScanActivity", "Starting camera...")
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Log.d("ScanActivity", "Error starting camera: ${exc.localizedMessage}")
                Toast.makeText(
                    this@ScanActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun uploadImage(file: File) {

        val requestFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image_file", file.name, requestFile)

        val call: Call<PredictionResponse> = api.uploadImage(body)

        call.enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                if (response.isSuccessful) {
                    val prediction = response.body()?.predicted_class
                    Log.d("ScanActivity", "Image uploaded successfully! Prediction: $prediction")

                    startScanResultActivity(file.absolutePath, cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA, prediction ?: "")
                } else {

                    Log.d("ScanActivity", "Image upload failed: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {

                Log.d("ScanActivity", "Error: ${t.message}")
            }
        })
    }
}
