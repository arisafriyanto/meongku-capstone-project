package com.example.meongku.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meongku.preference.UserPreferences

class HomeViewModel : ViewModel() {
    private lateinit var userPreferences: UserPreferences

    private val _text = MutableLiveData<String>().apply {
        value = "Hello "
    }
    val text: LiveData<String> = _text
}