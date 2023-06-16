package com.example.meongku.api

import com.example.meongku.preference.UserPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

    class RetrofitClient(private val userPreferences: UserPreferences) {

        private val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val sessionId = userPreferences.sessionId ?: ""
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "$sessionId")
                .build()
            chain.proceed(requestHeaders)
        }
        private val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("https://api-meongku-cjckv5ovwa-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        fun apiInstance(): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
