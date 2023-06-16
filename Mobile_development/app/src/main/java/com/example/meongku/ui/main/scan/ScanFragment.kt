package com.example.meongku.ui.main.scan

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.meongku.R
import com.example.meongku.api.ApiService
import com.example.meongku.api.CatBreedAPI
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.ml.PredictionResponse
import com.example.meongku.databinding.FragmentScanBinding
import com.example.meongku.preference.UserPreferences
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var retrofitClient: RetrofitClient
    private val api: ApiService = CatBreedAPI.api
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        userPreferences = UserPreferences(requireContext())
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.SwitchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(requireActivity().application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        requireContext(),
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(
                        requireContext(),
                        "Berhasil mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    uploadImage(photoFile)
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
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
                Toast.makeText(
                    requireContext(),
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun showSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }


    private fun uploadImage(file: File) {
        val requestFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image_file", file.name, requestFile)
        val call: Call<PredictionResponse> = api.uploadImage(body)

        call.enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                if (response.isSuccessful) {
                    val prediction = response.body()?.predicted_class
                    Log.d("ScanActivity", "Image uploaded successfully!")
                    hideLoading()
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

    private fun startScanResultActivity(imagePath: String, isBackCamera: Boolean, predictedClass: String) {
        val resultIntent = Intent(requireContext(), ScanResultActivity::class.java).apply {
            putExtra("picture", imagePath)
            putExtra("isBackCamera", isBackCamera)
            putExtra("predicted_class", predictedClass)
        }
        try {
            startActivity(resultIntent)
        } catch (e: Exception) {
            Log.d("ScanActivity", "Error starting ScanResultActivity: ${e.localizedMessage}")
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("ScanFragment", "Fragment paused")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ScanFragment", "Fragment stopped")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        showSystemUI()
        Log.d("ScanFragment", "Fragment view destroyed")
    }
    override fun onResume() {
        super.onResume()
        Log.d("ScanFragment", "Fragment resumed, className=com.example.meongku.ui.main.scan.ScanFragment")
    }
}