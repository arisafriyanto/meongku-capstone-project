package com.example.meongku.ui.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.meongku.MainActivity
import com.example.meongku.R
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.login.LoginRequest
import com.example.meongku.api.login.LoginResponse
import com.example.meongku.api.register.RegisterRequest
import com.example.meongku.api.register.RegisterResponse
import com.example.meongku.databinding.ActivityLoginBinding
import com.example.meongku.databinding.ActivityRegisterBinding
import com.example.meongku.preference.UserPreferences
import com.example.meongku.ui.login.LoginActivity
import com.example.meongku.ui.onboarding.OnBoardingActivity
import com.example.meongku.ui.theme.SettingPreferences
import com.example.meongku.ui.theme.ThemeViewModel
import com.example.meongku.ui.theme.ThemeViewModelFactory
import com.example.meongku.ui.theme.dataStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var retrofitClient: RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        retrofitClient = RetrofitClient(UserPreferences(this))


        binding.registerButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val name = binding.nameInput.text.toString()
            val phone = binding.phoneInput.text.toString()

            retrofitClient.apiInstance().userRegister(RegisterRequest(email, password, name, phone)).enqueue(object :
                Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                )
                {
                    if (response.isSuccessful) {
                        Log.d("REGISTER", "REGISTER: ${response.body()?.message}")
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.d("REGISTER", "REGISTER: ${response.errorBody()?.string()}")
                        Toast.makeText(this@RegisterActivity, "Kesalahan input data", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.d("REGISTER", "${t.message}")
                    Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        supportActionBar?.hide()

        binding.backButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, OnBoardingActivity::class.java)
            startActivity(intent)
        }
    }

}