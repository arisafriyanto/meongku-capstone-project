package com.example.meongku.ui.edituser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.engine.Resource
import com.example.meongku.api.ApiService
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.user.editpassword.EditPasswordRequest
import com.example.meongku.api.user.editpassword.EditPasswordResponse
import com.example.meongku.preference.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ini kelas belum kepake
class EditPasswordViewModel(var apiService: ApiService, var userPreferences: UserPreferences) : ViewModel() {

    private val _isPasswordUpdated = MutableLiveData<Boolean>()
    val isPasswordUpdated: LiveData<Boolean>
        get() = _isPasswordUpdated

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun editPassword(currentPassword: String, newPassword: String) {
        val uid = userPreferences.uid
        uid?.let {
            val requestBody = EditPasswordRequest(currentPassword, newPassword)
            apiService.editPassword(uid, requestBody).enqueue(object : Callback<EditPasswordResponse> {
                override fun onResponse(
                    call: Call<EditPasswordResponse>,
                    response: Response<EditPasswordResponse>
                ) {
                    _isPasswordUpdated.postValue(response.isSuccessful)
                }

                override fun onFailure(call: Call<EditPasswordResponse>, t: Throwable) {
                    _errorMessage.postValue(t.message ?: "Error Occurred!")
                }
            })
        }
    }
}