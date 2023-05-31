package com.example.trackori.api

import retrofit2.Call
import retrofit2.http.*

interface TrackoriApi {

    @POST("register")
    fun register(@Body user: RegisterCredentials): Call<RegisterResponse>

    @POST("login")
    fun login(@Body credentials: LoginCredentials): Call<LoginResponse>

    @GET("user/{uid}")
    fun getUserInfo(@Path("uid") uid: String): Call<UserResponse>

    @POST("users/{uid}/calorie-history")
    fun addCalorieHistory(@Path("uid") uid: String, @Body data: CalorieHistoryData): Call<CalorieHistoryDataResponse>

    @GET("users/{uid}/get-calorie-history")
    fun getCalorieHistoryByDate(@Path("uid") uid: String, @Query("date") date: String): Call<CalorieHistoryResponse>

    @GET("users/{uid}/all-calorie-history")
    fun getAllCalorieHistory(@Path("uid") uid: String): Call<CalorieHistoryResponse>

    @PUT("users/{uid}/calorie-history/{docId}")
    fun editCalorieHistory(@Path("uid") uid: String, @Path("docId") docId: String, @Body data: CalorieHistoryData): Call<CalorieHistoryDataResponse>

    @PUT("edit-info/{uid}")
    fun editUserInfo(@Path("uid") uid: String, @Body user: UserInfo): Call<UserInfoResponse>

    @PUT("edit-credential/{uid}")
    fun editUserCredentials(@Path("uid") uid: String, @Body credentials: EditCredentials): Call<EditCredentialsResponse>

    @POST("reset-password")
    fun resetPassword(@Body credentials: ResetPasswordCredentials): Call<ResetPasswordResponse>

    @POST("logout")
    fun logout(): Call<LogoutResponse>

    // For the protected resources
    @GET("protected")
    fun accessProtected(@Header("Authorization") token: String): Call<UserResponse>
}