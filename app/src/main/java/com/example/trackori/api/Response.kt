package com.example.trackori.api

data class LoginCredentials(
    val email: String,
    val password: String
)
data class RegisterCredentials(
    val username: String,
    val email: String,
    val password: String,
    val age: Int,
    val gender : String,
    val weight : Float,
    val height : Float,
    val plan : String?,
)

data class RegisterResponse(
    val error: Boolean,
    val message: String
)

// Additional Models

data class UserResponse(
    val success: Boolean,
    val message: String,
    val data: User
)

data class User(
    val uid: String,
    val email: String,
    val username: String,
    val gender: String,
    val age: Int,
    val weight: Float,
    val height: Float,
    val dailyCalorieNeeds: Float? = null, // Nullable as it's optional
    val plan: String? = null // Nullable as it's optional
)

data class CalorieHistoryItem(
    val id: String,
    val calories: Float,
    val date: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData
)

data class LoginData(
    val uid: String,
    val email: String,
    val username: String,
    val accessToken: String
)

data class CalorieHistoryResponse(
    val success: Boolean,
    val message: String,
    val data: List<CalorieHistoryItem>
)

data class EditCredentials(
    val email: String?,
    val password: String?,
    val currentEmail: String,
    val currentPassword: String
)

data class EditCredentialsResponse(
    val success: Boolean,
    val message: String,
    val data: User
)

data class ResetPasswordCredentials(
    val email: String
)

data class ResetPasswordResponse(
    val success: Boolean,
    val message: String,
    val data: ResetPasswordCredentials
)

data class LogoutResponse(
    val success: Boolean,
    val message: String
)

// For data in calorie-history
data class CalorieHistoryData(
    val calories: Float
)

data class CalorieHistoryDataResponse(
    val success: Boolean,
    val message: String,
    val data: CalorieHistoryData
)
