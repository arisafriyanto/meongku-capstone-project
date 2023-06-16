package com.example.meongku.api.user.editpassword

data class EditPasswordRequest(
    val currentPassword: String,
    val password: String
)
