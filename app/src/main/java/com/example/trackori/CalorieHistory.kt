package com.example.trackori

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackori.adapter.CalorieHistoryAdapter
import com.example.trackori.api.*
import com.example.trackori.databinding.ActivityCalorieHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalorieHistory : AppCompatActivity() {

    private lateinit var binding: ActivityCalorieHistoryBinding
    private lateinit var adapter: CalorieHistoryAdapter
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalorieHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        adapter = CalorieHistoryAdapter(listOf())
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter
        preferencesHelper = PreferencesHelper(this)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        getAllCalorieHistory()
    }

    private fun getAllCalorieHistory() {
        val uid = preferencesHelper.uid // replace with actual user id
        val apiService = ApiConfig.getApiService()

        if (uid != null) {
            apiService.getAllCalorieHistory(uid).enqueue(object : Callback<CalorieHistoryResponse> {
                override fun onResponse(
                    call: Call<CalorieHistoryResponse>,
                    response: Response<CalorieHistoryResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        val data = response.body()?.data
                        val aggregatedData: List<CalorieHistoryItemDetail> = response.body()?.data.orEmpty()
                            .groupBy { it.date }
                            .map { (date, items) ->
                                val foodList = items.filter { it.name?.isNotBlank() == true && it.calories > 0 }
                                    .groupBy { it.name }
                                    .map { (name, foods) ->
                                        FoodItemHistory(
                                            name = name.orEmpty(),
                                            calories = foods.sumByDouble { it.calories.toDouble() }.toInt()
                                        )
                                    }
                                CalorieHistoryItemDetail(
                                    id = "", // You might need to change this according to your requirement
                                    date = date,
                                    foodList = foodList
                                )
                            }
                        aggregatedData?.let { adapter.setData(it) }
                    }
                }

                override fun onFailure(call: Call<CalorieHistoryResponse>, t: Throwable) {
                    // Handle error here
                }
            })
        }
    }



}