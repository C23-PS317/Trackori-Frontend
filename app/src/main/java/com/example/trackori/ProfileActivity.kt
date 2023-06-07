package com.example.trackori

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.trackori.ImageProcessingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.trackori.api.ApiConfig
import com.example.trackori.api.LogoutResponse
import com.example.trackori.api.UserResponse
import com.example.trackori.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.math.RoundingMode


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var preferencesHelper: PreferencesHelper

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)

        val uid = preferencesHelper.uid
        val api = ApiConfig.getApiService()

        supportActionBar?.hide()
        binding.btnSettings.setOnClickListener {
            val intent = Intent(this, CredentialsActivity::class.java)
            startActivity(intent)
        }

        val logoutButton: Button = findViewById(R.id.btnLogout)
        logoutButton.setOnClickListener {
            logoutUser()}

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_profile

        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        val profileMenuItemView = menuView.getChildAt(2) as BottomNavigationItemView

        profileMenuItemView.setIconTintList(ContextCompat.getColorStateList(this, R.color.trackori))



        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_info -> {
                    // Launch Info Activity or Fragment
                    true
                }
                R.id.nav_camera -> {
                    val intent = Intent(this, ImageProcessingActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        api.getUserInfo(uid!!).enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null && userResponse.success) {
                        val user = userResponse.data
                        binding.tvUsername.text = user.username
                        binding.tvEmail.text = user.email
                        binding.tvCalorie.text = user.dailyCalorieNeeds?.toBigDecimal()
                            ?.setScale(0, RoundingMode.UP)
                            ?.toDouble().toString()
                        binding.tvGender.text = user.gender
                        binding.tvHeight.text = user.height.toString()
                        binding.tvWeight.text = user.weight.toString()
                        binding.tvPlan.text = user.plan
                        binding.tvAge.text = user.age.toString()
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // Handle error
            }
        })

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("username", binding.tvUsername.text.toString())
            intent.putExtra("age", binding.tvAge.text.toString().toInt())
            intent.putExtra("weight", binding.tvWeight.text.toString().toFloat())
            intent.putExtra("height", binding.tvHeight.text.toString().toFloat())
            intent.putExtra("gender", binding.tvGender.text.toString())
            intent.putExtra("plan", binding.tvPlan.text.toString())
            intent.putExtra("calorie", binding.tvCalorie.text.toString().toFloat())
            startActivity(intent)
        }
    }
    private fun logoutUser() {
        val apiService = ApiConfig.getApiService()
        val call = apiService.logout()
        call.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    val logoutResponse = response.body()
                    if (logoutResponse?.success == true) {
                        preferencesHelper.clear()


                        val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Handle logout error
                        // Show an error message or perform any necessary action
                    }
                } else {
                    // Handle API call error
                    // Show an error message or perform any necessary action
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                // Handle API call failure
                // Show an error message or perform any necessary action
            }
        })

    }

}