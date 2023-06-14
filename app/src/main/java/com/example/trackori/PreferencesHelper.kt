package com.example.trackori

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean("isLoggedIn", false)
        set(value) = sharedPreferences.edit().putBoolean("isLoggedIn", value).apply()

    var token: String?
        get() = sharedPreferences.getString("token", null)
        set(value) = sharedPreferences.edit().putString("token", value).apply()

    var uid: String?
        get() = sharedPreferences.getString("uid", null)
        set(value) = sharedPreferences.edit().putString("uid", value).apply()

    var username: String?
        get() = sharedPreferences.getString("username", null)
        set(value) = sharedPreferences.edit().putString("username", value).apply()

    var dailycalorie: Float
        get() = sharedPreferences.getFloat("dailyCalorieNeeds", 0F) // Here you should specify a valid default value like 0F
        set(value) = sharedPreferences.edit().putFloat("dailyCalorieNeeds", value).apply()

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}