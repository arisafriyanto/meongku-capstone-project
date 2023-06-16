package com.example.meongku.api.user

data class UserResponse(
    val statusCode: Int,
    val status: String,
    val message: String,
    val user: User
)
