package com.example.meongku.api.login

data class LoginResponse(
    val statusCode: Int,
    val status: String,
    val message: String,
    val data: Data
) {
    data class Data(
        val uid: String,
        val sessionId: String
    )
}