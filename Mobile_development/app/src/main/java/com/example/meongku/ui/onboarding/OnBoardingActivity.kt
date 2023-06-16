package com.example.meongku.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.meongku.R
import com.example.meongku.databinding.ActivityOnBoardingBinding
import com.example.meongku.ui.login.LoginActivity
import com.example.meongku.ui.register.RegisterActivity
import com.example.meongku.ui.theme.SettingPreferences
import com.example.meongku.ui.theme.ThemeViewModel
import com.example.meongku.ui.theme.ThemeViewModelFactory
import com.example.meongku.ui.theme.dataStore

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val fullText = binding.onboardingText.text
        val spannableString = SpannableString(fullText)

        val keyword = "kesayanganmu"
        val startIndex = fullText.indexOf(keyword)
        val endIndex = startIndex + keyword.length

        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.onboardingText.text = spannableString

        supportActionBar?.hide()
    }

}