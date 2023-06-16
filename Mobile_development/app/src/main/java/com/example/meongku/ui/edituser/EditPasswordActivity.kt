package com.example.meongku.ui.edituser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meongku.MainActivity
import com.example.meongku.R
import com.example.meongku.api.ApiService
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.user.editpassword.EditPasswordRequest
import com.example.meongku.api.user.editpassword.EditPasswordResponse
import com.example.meongku.databinding.ActivityEditPasswordBinding
import com.example.meongku.preference.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPasswordBinding
    private lateinit var apiService: ApiService
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // inisiasi ApiService and UserPreferences
        userPreferences = UserPreferences(this)
        val retrofitClient = RetrofitClient(userPreferences)
        apiService = retrofitClient.apiInstance()

        binding.saveButton.setOnClickListener {
            val currentPassword = binding.currentPasswordEditText.text.toString()
            val newPassword = binding.newPasswordEditText.text.toString()
            val uid = userPreferences.uid

            if (uid != null) {
                val editPasswordRequest = EditPasswordRequest(currentPassword, newPassword)
                apiService.editPassword(uid, editPasswordRequest)
                    .enqueue(object : Callback<EditPasswordResponse> {
                        override fun onResponse(
                            call: Call<EditPasswordResponse>,
                            response: Response<EditPasswordResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("EditPassword", "Password updated successfully: ${response.body()?.message}")
                                val intent = Intent(this@EditPasswordActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Log.d("EditPassword", "Failed to update password: ${response.errorBody()?.string()}")
                                Toast.makeText(this@EditPasswordActivity, "Failed to update password", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<EditPasswordResponse>, t: Throwable) {
                            Log.d("EditPassword", "Error: ${t.message}")
                            Toast.makeText(this@EditPasswordActivity, t.message, Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }
    }
}