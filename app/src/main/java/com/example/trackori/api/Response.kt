package com.example.trackori.api

data class LoginCredentials(
    val email: String,
    val password: String
)
data class RegisterCredentials(
    val username: String,
    val email: String,
    val password: String
)