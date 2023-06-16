package com.example.meongku.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CatBreedAPI {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://cat-breed-cjckv5ovwa-et.a.run.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(ApiService::class.java)
}