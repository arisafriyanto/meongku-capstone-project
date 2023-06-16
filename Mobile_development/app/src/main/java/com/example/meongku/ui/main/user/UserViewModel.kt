package com.example.meongku.ui.main.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meongku.api.RetrofitClient
import com.example.meongku.api.user.User
import com.example.meongku.api.user.UserResponse
import com.example.meongku.preference.UserPreferences
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//class UserViewModel(private val userPreferences: UserPreferences, private val retrofitClient: RetrofitClient): ViewModel() {
//
//    private val _userData = MutableLiveData<User>()
//    val logoutResponse = MutableLiveData<Boolean>()
//
//    fun fetchUser() {
//        val uid = userPreferences.uid
//
//        retrofitClient.apiInstance().getUserByUid(uid!!).enqueue(object : Callback<UserResponse> {
//            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
//                if (response.isSuccessful) {
//                    val user = response.body()?.user
//                    userPreferences.userData = Gson().toJson(user)
//                    _userData.value = user!!
//                } else {
//                    Log.d("User", response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                Log.d("User", "${t.message}")
//            }
//        })
//    }
//
//    fun signOut() {
//        val token = userPreferences.idToken
//        if (token != null) {
//            retrofitClient.apiInstance().logout(token).enqueue(object : Callback<ResponseBody> {
//                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                    if (response.isSuccessful) {
//                        userPreferences.clear()
//                        logoutResponse.value = true
//                        Log.d("Logout", "Logout successful: ${response.body()?.string()}")
//                    } else {
//                        logoutResponse.value = false
//                        Log.d("Logout", "Logout failed: ${response.errorBody()?.string()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    logoutResponse.value = false
//                    Log.d("Logout", "${t.message}")
//                }
//            })
//        }
//    }
//}