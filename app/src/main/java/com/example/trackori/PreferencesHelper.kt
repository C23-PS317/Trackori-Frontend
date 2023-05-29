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

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}