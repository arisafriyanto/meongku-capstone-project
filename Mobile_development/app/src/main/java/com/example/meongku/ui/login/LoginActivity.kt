package com.example.meongku.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.meongku.MainActivity
import com.example.meongku.R
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.login.LoginRequest
import com.example.meongku.api.login.LoginResponse
import com.example.meongku.databinding.ActivityLoginBinding
import com.example.meongku.preference.UserPreferences
import com.example.meongku.ui.onboarding.OnBoardingActivity
import com.example.meongku.ui.theme.SettingPreferences
import com.example.meongku.ui.theme.ThemeViewModel
import com.example.meongku.ui.theme.ThemeViewModelFactory
import com.example.meongku.ui.theme.dataStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPreferences: UserPreferences
    private lateinit var retrofitClient: RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        userPreferences = UserPreferences(this)
        retrofitClient = RetrofitClient(userPreferences)
        val pref = SettingPreferences.getInstance(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            retrofitClient.apiInstance().userLogin(LoginRequest(email, password)).enqueue(object :
                Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>)
                {
                    if (response.isSuccessful) {
                        Log.d("LOGIN", "LOGIN: ${response.body()?.message}")
                        response.body()?.data?.let {
                            userPreferences.uid = it.uid
                            userPreferences.sessionId = it.sessionId
                            userPreferences.isLoggedIn = true
                        }
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.d("LOGIN", "LOGIN: ${response.errorBody()?.string()}")
                        Toast.makeText(this@LoginActivity, "Email atau Password ada yang salah", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("LOGIN", "${t.message}")
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        supportActionBar?.hide()
        binding.backButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, OnBoardingActivity::class.java)
            startActivity(intent)
        }
    }
}