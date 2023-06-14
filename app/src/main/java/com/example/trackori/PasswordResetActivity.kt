package com.example.trackori

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trackori.api.ApiConfig
import com.example.trackori.api.ResetPasswordCredentials
import com.example.trackori.api.ResetPasswordResponse
import com.example.trackori.databinding.ActivityPasswordResetBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordResetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordResetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReset.setOnClickListener {
            val email = binding.edResetEmail.text.toString()

            val apiService = ApiConfig.getApiService()

            apiService.resetPassword(
                ResetPasswordCredentials(email)
            ).enqueue(object : Callback<ResetPasswordResponse> {
                override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                    if (response.isSuccessful) {
                        val resetResponse = response.body()
                        if (resetResponse?.success == true) {
                            Toast.makeText(
                                this@PasswordResetActivity,
                                "an Email has been sent",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@PasswordResetActivity,
                                    LoginActivity::class.java
                                )
                            )
                        } else {
                            // Handle reset error
                            // Show an error message or perform any necessary action.
                            Toast.makeText(
                                this@PasswordResetActivity,
                                resetResponse.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@PasswordResetActivity,
                            "An Error Occured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
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