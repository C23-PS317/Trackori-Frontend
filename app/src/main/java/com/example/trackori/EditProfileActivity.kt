package com.example.trackori

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.trackori.api.*
import com.example.trackori.databinding.ActivityEditProfileBinding
import com.example.trackori.util.calculateDailyCalorieNeeds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val api by lazy { ApiConfig.getApiService() }
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferencesHelper = PreferencesHelper(this)

        supportActionBar?.hide()

        val previousUsername = intent.getStringExtra("username")
        val previousAge = intent.getIntExtra("age", 0)
        val previousWeight = intent.getFloatExtra("weight", 0f)
        val previousHeight = intent.getFloatExtra("height", 0f)
        val previousGender = intent.getStringExtra("gender")
        val previousPlan = intent.getStringExtra("plan")

        binding.edEditUsername.setText(previousUsername)
        binding.edEditAge.setText(previousAge.toString())
        binding.edEditWeight.setText(previousWeight.toString())
        binding.edEditHeight.setText(previousHeight.toString())


        val genderOptions = arrayOf("Select Gender", "male", "female")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderOptions)
        binding.spinnerGender.adapter = genderAdapter
        val genderPosition = genderOptions.indexOf(previousGender)
        if (genderPosition != -1) {
            binding.spinnerGender.setSelection(genderPosition)
        }

        val dietPlanOptions = arrayOf("Select Diet Plan", "Defisit Calorie", "Bulking", "No Plan")
        val dietPlanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dietPlanOptions)
        binding.spinnerDietPlan.adapter = dietPlanAdapter
        val dietPlanPosition = dietPlanOptions.indexOf(previousPlan)
        if (dietPlanPosition != -1) {
            binding.spinnerDietPlan.setSelection(dietPlanPosition)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

        setupViews()

    }

    private fun setupViews() {
        binding.btnSave.setOnClickListener { SaveEdit() }

    }

    private fun setupSpinnerGender() {
        val genderOptions = arrayOf("Select Gender","male", "female")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderOptions)
        binding.spinnerGender.adapter = genderAdapter
    }

    private fun setupSpinnerDietPlan() {
        val dietPlanOptions = arrayOf("Select Diet Plan","Defisit Calorie", "Bulking", "No Plan")
        val dietPlanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dietPlanOptions)
        binding.spinnerDietPlan.adapter = dietPlanAdapter
    }



    private fun SaveEdit() {
        // Get data from EditTexts and Spinners
        val username = binding.edEditUsername.text.toString()
        val age = binding.edEditAge.text.toString().toInt()
        val weight = binding.edEditWeight.text.toString().toFloat()
        val height = binding.edEditHeight.text.toString().toFloat()
        val uid = preferencesHelper.uid
        val previousCalorie = intent.getFloatExtra("calorie", 0f)
        val previousGender = intent.getStringExtra("gender")
        val api = ApiConfig.getApiService()

        val gender = binding.spinnerGender.selectedItem.toString()
        if (gender == "Select Gender") {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
            return
        }
        var no_diet:String? = ""
        var total_calorie= 0f

        val plan = binding.spinnerDietPlan.selectedItem.toString()
        if (plan == "Select Diet Plan") {
            Toast.makeText(this, "Please select a diet plan", Toast.LENGTH_SHORT).show()
        }
        else if(plan == "No Plan"){
            no_diet = "no plan"
            total_calorie = calculateDailyCalorieNeeds(height,weight,age,previousGender,no_diet)

        }
        else if(plan == "Bulking"){
            no_diet = "bulking"
            total_calorie = calculateDailyCalorieNeeds(height,weight,age,previousGender,no_diet)
        }
        else if(plan == "Defisit Calorie"){
            no_diet = "defisit"
            total_calorie = calculateDailyCalorieNeeds(height,weight,age,previousGender,no_diet)
        }

        if (username.isNotBlank() && gender.isNotBlank() && gender.isNotBlank() &&
            weight != null && height != null && plan.isNotBlank()
        ) {
            val editCredentials = UserInfo(
                    username,
                    age,
                    weight,
                    height,
                    total_calorie,
                    no_diet,
                )

            binding.loadingOverlay.visibility = View.VISIBLE
            val call = editCredentials.let { ApiConfig.getApiService().editUserInfo(uid!!, it) }

            println(" Ini total kalori valen ${total_calorie}")
            println(" Ini plan kalori valen ${no_diet}")

            if (call != null) {
                call.enqueue(object : Callback<UserInfoResponse> {
                    override fun onResponse(
                        call: Call<UserInfoResponse>,
                        response: Response<UserInfoResponse>
                    ) {
                        // Handle response
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody?.success == true) {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Profile Edited Succesfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(
                                        this@EditProfileActivity,
                                        LoginActivity::class.java
                                    )
                                )
                            } else {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    responseBody?.message ?: "Unknown error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Edit Profile failed: ${response.errorBody()?.string()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                        binding.loadingOverlay.visibility = View.GONE
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Network error: ${t.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }



    }
}