package com.example.meongku.ui.main.scan

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.meongku.R
import com.example.meongku.databinding.ActivityScanResultBinding
import java.io.File

class ScanResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding

    companion object {
        const val CAMERA_X_RESULT = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_result)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val predictedClass = intent.getStringExtra("predicted_class")
        binding.raceTextView.text = predictedClass

        try {
            val myFile = File(intent.getStringExtra("picture") ?: "")
            val isBackCamera = intent.getBooleanExtra("isBackCamera", true)
            binding.imageView.setImageBitmap(BitmapFactory.decodeFile(myFile.path))
            Log.d("ScanResultActivity", "Image displayed successfully.")
        } catch (e: Exception) {
            Log.e("ScanResultActivity", "Error in onCreate: ${e.localizedMessage}")
        }


        //launcherIntentCameraX.launch(Intent(this, ScanActivity::class.java))
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERA_X_RESULT) {
            try {
                val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getSerializableExtra("picture") as? File
                } else {
                    @Suppress("DEPRECATION")
                    result.data?.getSerializableExtra("picture") as? File
                }
                val isBackCamera = result.data?.getBooleanExtra("isBackCamera", true) as Boolean
                myFile?.let { file ->
                    binding.imageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
                    Log.d("ScanResultActivity", "Image updated successfully.")
                }
            } catch (e: Exception) {
                Log.e("ScanResultActivity", "Error in onActivityResult: ${e.localizedMessage}")
            }
        }
    }
}