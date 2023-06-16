package com.example.meongku.api.catlist

data class CatIdResponse(
    val statusCode: Int,
    val status: String,
    val message: String,
    val cat: Cat
)
