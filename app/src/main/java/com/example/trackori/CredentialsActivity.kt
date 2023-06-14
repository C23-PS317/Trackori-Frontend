package com.example.trackori

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackori.api.ApiConfig
import com.example.trackori.api.EditCredentials
import com.example.trackori.api.EditCredentialsResponse
import com.example.trackori.databinding.ActivityCredentialsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CredentialsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCredentialsBinding
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCredentialsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferencesHelper = PreferencesHelper(this)

        val uid = preferencesHelper.uid

        supportActionBar?.hide()

        binding.btnSave.setOnClickListener {
            val currentEmail = binding.edCurrentEmail.text.toString()
            val currentPassword = binding.edCurrentPassword.text.toString()
            val newEmail = binding.edNewEmail.text.toString()
            val newPassword = binding.edNewPassword.text.toString()

            val apiService = ApiConfig.getApiService()

            apiService.editUserCredentials(
                uid!!,
                EditCredentials(newEmail, newPassword, currentEmail, currentPassword)
            ).enqueue(object : Callback<EditCredentialsResponse> {
                override fun onResponse(call: Call<EditCredentialsResponse>, response: Response<EditCredentialsResponse>) {
                    if (response.isSuccessful) {
                        val editResponse = response.body()
                        if (editResponse?.success == true) {
                            // Handle successful credentials edit.
                            // Show a success message or perform any necessary action.
                            Toast.makeText(
                                this@CredentialsActivity,
                                "Credentials Edited Succesfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@CredentialsActivity,
                                    ProfileActivity::class.java
                                )
                            )
                        } else {
                            // Handle edit error
                            // Show an error message or perform any necessary action.
                        }
                    } else {
                        // Handle API call error
                        // Show an error message or perform any necessary action.
                    }
                }

                override fun onFailure(call: Call<EditCredentialsResponse>, t: Throwable) {
                    // Handle API call failure
                    // Show an error message or perform any necessary action.
                }
            })
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}