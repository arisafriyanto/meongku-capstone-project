package com.example.meongku.api.catlist

data class CatResponse(
    val statusCode: Int,
    val status: String,
    val message: String,
    val cats: List<Cat>
)
