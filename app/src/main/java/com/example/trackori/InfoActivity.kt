package com.example.trackori

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.trackori.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.trackori.databinding.ActivityInfoBinding
import com.example.trackori.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.runBlocking
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class InfoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var preferencesHelper: PreferencesHelper

    private var finalDailyCalorie: Float = 0.0f
    private var finalCalorieHistory: Float = 0.0f
    private lateinit var docId : String

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        preferencesHelper = PreferencesHelper(this)

        if (!preferencesHelper.isLoggedIn) {
            // User is not logged in. Redirect them to the login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val uid = preferencesHelper.uid
        val api = ApiConfig.getApiService()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_info

        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        val infoMenuItemView = menuView.getChildAt(0) as BottomNavigationItemView

        infoMenuItemView.setIconTintList(ContextCompat.getColorStateList(this, R.color.trackori))


        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_info -> {
                    // Launch Info Activity or Fragment
                    val intent = Intent(this, InfoActivity::class.java)
                    startActivity(intent)
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

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)

        val realDate = formatted.slice(0..9)


        // Get total calorie

        api.getUserInfo(uid!!).enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                val userResponse = response.body()
                if (userResponse != null && userResponse.success) {
                    val user = userResponse.data
                    val dailyCalorie = user.dailyCalorieNeeds!!
                    finalDailyCalorie = dailyCalorie
                    val tempTotalCalorie = "of ${dailyCalorie.toBigDecimal()
                        ?.setScale(0, RoundingMode.UP)
                        ?.toDouble().toString()} kcal"
                    binding.tvTotalCalorie.text = tempTotalCalorie
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        // Get calorie history

        api.getCalorieHistoryByDate(uid!!, realDate!!)
            .enqueue(object : Callback<CalorieHistoryResponse> {
                override fun onResponse(
                    call: Call<CalorieHistoryResponse>,
                    response: Response<CalorieHistoryResponse>
                ) {
                    val calorieHistoryRes = response.body()
                    if (calorieHistoryRes != null && calorieHistoryRes.success) {
                        var totalCalories = 0.0f
                        var ids = ArrayList<String>()

                        for (item in calorieHistoryRes.data) {
                            totalCalories += item.calories
                            ids.add(item.id)
                        }

                        finalCalorieHistory = totalCalories
                        docId = ids.joinToString(",") // Combine all ids into a single string with comma separators

                        updateCalorieHistoryView()
                        binding.tvCurrCalorie.text = finalCalorieHistory.toString()
                    } else {
                        finalCalorieHistory = 0.0f
                        updateCalorieHistoryView()
                        binding.tvCurrCalorie.text = finalCalorieHistory.toString()
                    }
                }

                override fun onFailure(call: Call<CalorieHistoryResponse>, t: Throwable) {
                    Log.e("API_ERROR", "Failure: ${t.message}")
                }
            })
//        val tempAddButton: MaterialButton = findViewById(R.id.testButton)
//
//        tempAddButton.setOnClickListener {
//            val tempNum = finalCalorieHistory + 100.0f
//            val tempCal = CalorieHistoryData(tempNum)
//            api.editCalorieHistory(uid!!, docId, tempCal).enqueue(object: Callback<CalorieHistoryDataResponse> {
//                override fun onResponse(
//                    call: Call<CalorieHistoryDataResponse>,
//                    response: Response<CalorieHistoryDataResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val intent = Intent(this@InfoActivity, ProfileActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//
//                override fun onFailure(call: Call<CalorieHistoryDataResponse>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//        }
    }


    private fun updateCalorieHistoryView() {
        binding.tvCurrCalorie.text = finalCalorieHistory.toString()
        val percentageCal = if (finalDailyCalorie == 0.0f) 0f else (finalCalorieHistory / finalDailyCalorie) * 100
        binding.circularProgressIndicator.progress = percentageCal.roundToInt()
    }

    private fun setCalorieHistory(cal : Float) {
        finalCalorieHistory = cal
    }

    private fun setDailyCalorie(cal : Float) {
        finalDailyCalorie = cal
    }
}