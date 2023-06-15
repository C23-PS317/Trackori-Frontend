package com.example.trackori

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.trackori.api.ApiConfig
import com.example.trackori.api.RegisterCredentials
import com.example.trackori.api.RegisterResponse
import com.example.trackori.databinding.ActivityLoginBinding
import com.example.trackori.databinding.ActivityRegisterBinding
import isValidEmail
import isValidPassword
import isValidUsername
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val api by lazy { ApiConfig.getApiService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupSpinnerGender()
//        setupSpinnerDietPlan()
        setupViews()
    }

    private fun setupViews() {
        binding.btnRegister.setOnClickListener { registerUser() }

        val loginText: TextView = binding.tvLogin
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun registerUser() {
        // Get data from EditTexts and Spinners
        val username = binding.edRegisterUsername.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        val age = binding.edRegisterAge.text.toString().toIntOrNull()
        val weight = binding.edRegisterWeight.text.toString().toFloatOrNull()
        val height = binding.edRegisterHeight.text.toString().toFloatOrNull()

        val gender = binding.spinnerGender.selectedItem.toString()

        if (username.isBlank() || email.isBlank() || password.isBlank() || age == null || weight == null || height == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (gender == "Select Gender") {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
            return
        }

        // Check for empty fields

        // Validate email, username, password, age, weight, and height
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidUsername(username)) {
            Toast.makeText(this, "Username should be at least 3 characters and not contain spaces", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password should be at least 8 characters and contain at least one number", Toast.LENGTH_SHORT).show()
            return
        }

        if (age < 13 || age > 100) {
            Toast.makeText(this, "Please enter a valid age between 13 and 100", Toast.LENGTH_SHORT).show()
            return
        }

        if (weight < 20 || weight > 300) {
            Toast.makeText(this, "Please enter a valid weight between 20 and 300 kg", Toast.LENGTH_SHORT).show()
            return
        }

        if (height < 100 || height > 250) {
            Toast.makeText(this, "Please enter a valid height between 100 and 250 cm", Toast.LENGTH_SHORT).show()
            return
        }

        var no_diet = "no plan"

            if (username.isNotBlank() && email.isNotBlank() && password.isNotBlank() && gender.isNotBlank() &&
                weight != null && height != null
            ) {
                val registerCredentials = RegisterCredentials(
                    username,
                    email,
                    password,
                    age,
                    gender,
                    weight,
                    height,
                    no_diet
                )

                binding.loadingOverlay.visibility = View.VISIBLE

                api.register(registerCredentials).enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        // Handle response
                        binding.loadingOverlay.visibility = View.GONE
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody?.error == false) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registered Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        LoginActivity::class.java
                                    )
                                )
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    responseBody?.message ?: "Unknown error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registration failed: ${response.errorBody()?.string()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        binding.loadingOverlay.visibility = View.GONE
                        Toast.makeText(
                            this@RegisterActivity,
                            "Network error: ${t.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
    }
    private fun setupSpinnerGender() {
        val genderOptions = arrayOf("Select Gender","male", "female")
        val genderAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, genderOptions)
        binding.spinnerGender.adapter = genderAdapter
    }

//    private fun setupSpinnerDietPlan() {
//        val dietPlanOptions = arrayOf("Select Diet Plan","Defisit Calorie", "Bulking", "No Plan")
//        val dietPlanAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, dietPlanOptions)
//        binding.spinnerDietPlan.adapter = dietPlanAdapter
//    }


}