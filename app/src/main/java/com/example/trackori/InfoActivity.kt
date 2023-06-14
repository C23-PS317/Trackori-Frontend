package com.example.trackori

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackori.adapter.FoodHistoryAdapter
import com.example.trackori.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.trackori.databinding.ActivityInfoBinding
import com.example.trackori.databinding.ActivityProfileBinding
import com.example.trackori.databinding.DialogPortionBinding
import com.example.trackori.viewmodel.FoodViewModel
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
    private lateinit var adapter: FoodHistoryAdapter
    private lateinit var viewModel: FoodViewModel

    private var finalDailyCalorie: Float = 0.0f
    private var finalCalorieHistory: Float = 0.0f
    private lateinit var docId : String
    private lateinit var itemId : String

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(FoodViewModel::class.java)

        supportActionBar?.hide()
        adapter = FoodHistoryAdapter(listOf(), onEditClickListener = { id ->

            itemId = id
        })

        adapter.onEditButtonClick = { foodData ->
            // foodData is the data of the item where the add button was clicked
            showPortionDialog(foodData)
        }



        binding.rvFoodHistory.layoutManager = LinearLayoutManager(this)
        binding.rvFoodHistory.adapter = adapter

        preferencesHelper = PreferencesHelper(this)

        if (!preferencesHelper.isLoggedIn) {
            // User is not logged in. Redirect them to the login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val infoButton: ImageButton = findViewById(R.id.infoButton)
        infoButton.setOnClickListener {
            val intent = Intent(this@InfoActivity, CalorieHistory::class.java).apply {
//                putExtra("activityOrigin", "InfoActivity")
            }
            startActivity(intent)
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
        val operan = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS")
        binding.dateNow.text = "Today, ${current.format(operan).slice(0..9)}"
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
                        ?.toDouble().toString()} kcals"
                    binding.tvTotalCalorie.text = tempTotalCalorie

                    updateCalorieHistoryView()
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
                        val ids = ArrayList<String>()
                        val data = calorieHistoryRes.data.orEmpty().filter { it.name?.isNotBlank() == true }
                        data.forEach { item ->
                            totalCalories += item.calories
                            ids.add(item.id)
                        }

                        // Directly set the data to the adapter without aggregation
                        adapter.setData(data)

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
//
        val tambahMakanan: MaterialButton = findViewById(R.id.tambahMakanan)
        tambahMakanan.setOnClickListener{
            val intent = Intent(this@InfoActivity, FoodListActivity::class.java).apply {
                putExtra("activityOrigin", "InfoActivity")
            }
            startActivity(intent)
        }

        api.getUserInfo(uid!!).enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null && userResponse.success) {
                        val user = userResponse.data
                        binding.tvCalorie.text = user.dailyCalorieNeeds?.toBigDecimal()
                            ?.setScale(0, RoundingMode.UP)
                            ?.toDouble().toString()
                        binding.tvPlan.text = user.plan.toString().split(" ").joinToString(separator = " ", transform = String::capitalize)
                        binding.tvAge.text = user.age.toString()

                        preferencesHelper.dailycalorie =
                            user.dailyCalorieNeeds?.toBigDecimal()?.setScale(0, RoundingMode.UP)?.toFloat()!!
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // Handle error
            }
        })





    }



    private fun showPortionDialog(foodData: CalorieHistoryItem) {
        val dialogBinding = DialogPortionBinding.inflate(LayoutInflater.from(this))
        val dialogView = dialogBinding.root

        val satuanParts = foodData.unit.split(" ")
        val defaultPortion = foodData.portion.toInt().toString()

        // Set the default portion value
        dialogBinding.editTextPortion.setText(defaultPortion)

        // Display the unit
        val unit = if(satuanParts.size > 1) satuanParts[1] else ""
        dialogBinding.textViewPortionUnit.text = unit

        // Calculate calories per unit
        val caloriesPerUnit = foodData.calories.toDouble() / foodData.portion

        // Display the total calories
        dialogBinding.textViewTotalCalories.text = "Total Kalori: ${caloriesPerUnit * defaultPortion.toDouble()}"

        val portionDialog = AlertDialog.Builder(this)
            .setTitle("Masukkan jumlah porsi")
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .setNegativeButton("Batal", null)
            .create()

        portionDialog.setOnShowListener {
            val positiveButton = portionDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = portionDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            positiveButton.setTextColor(Color.rgb(0,77,61))
            negativeButton.setTextColor(Color.RED)

            positiveButton.setOnClickListener {
                val portion = dialogBinding.editTextPortion.text.toString().toInt()
                val totalCalories = caloriesPerUnit * portion

                val userId = preferencesHelper.uid.toString()

                val calorieHistoryData = CalorieHistoryData(foodData.name, totalCalories.toFloat(), portion.toFloat(), foodData.unit)

                // Send the data to the API
                viewModel.updateCalorieHistory(userId, foodData.id, calorieHistoryData)

                portionDialog.dismiss()

                // Recreate the activity to reload the page
                recreate()
            }
        }

        // Update the total calories when the portion changes
        dialogBinding.editTextPortion.addTextChangedListener {
            val portion = it.toString().toIntOrNull() ?: 0
            dialogBinding.textViewTotalCalories.text = "Total Kalori: ${caloriesPerUnit * portion}"
        }

        portionDialog.show()
    }
// Ini buat ntar kalo misal nge add food ya mem jadi dia auto nge getall food





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