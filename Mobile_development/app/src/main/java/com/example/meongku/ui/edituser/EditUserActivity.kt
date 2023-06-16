package com.example.meongku.ui.edituser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.meongku.MainActivity
import com.example.meongku.R
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.user.UpdateUserRequest
import com.example.meongku.api.user.User
import com.example.meongku.databinding.ActivityEditUserBinding
import com.example.meongku.databinding.ActivityLoginBinding
import com.example.meongku.preference.UserPreferences
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserBinding
    private lateinit var userPreferences: UserPreferences
    private lateinit var retrofitClient: RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        userPreferences = UserPreferences(this)
        retrofitClient = RetrofitClient(userPreferences)
        val userData = userPreferences.userData
        if (userData != null) {
            val user = Gson().fromJson(userData, User::class.java)
            binding.nameEditText.setText(user.name)
            binding.phoneEditText.setText(user.phone)

        }
        binding.passwordInputLayout.setEndIconOnClickListener {

            val intent = Intent(this, EditPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.buttonSave.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()

            val requestBody = UpdateUserRequest(name, phone)

            val uid = userPreferences.uid

            retrofitClient.apiInstance().editUserByUid(uid!!, requestBody)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>)
                    {
                        if (response.isSuccessful) {
                            Log.d("EditUser", "Edit user profile successful: ${response.body()?.string()}")
                            val intent = Intent(this@EditUserActivity, MainActivity::class.java)
                            startActivity(intent)

                            finish()
                        } else {
                            Log.d("EditUser", "Edit user profile failed: ${response.errorBody()?.string()}")
                            Toast.makeText(this@EditUserActivity, "Failed to edit user profile", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("EditUser", "Error: ${t.message}")
                        Toast.makeText(this@EditUserActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }
}