package com.example.trackori.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackori.api.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class FoodViewModel() : ViewModel() {
    private val trackoriApi = ApiConfig.getApiService()
    val foodData = MutableLiveData<FoodByIdData?>()

    val AllfoodData = MutableLiveData<List<AllFoodItem>?>()

    val foodHistoryData = MutableLiveData<List<CalorieHistoryItem>>()

    val userInfo = MutableLiveData<User>()


    private val TAG = "FoodViewModel" // define TAG for logging

    fun getFoodById(id: String) = viewModelScope.launch {
        trackoriApi.getFoodById(id).enqueue(object : Callback<FoodByIdResponse> {
            override fun onResponse(call: Call<FoodByIdResponse>, response: Response<FoodByIdResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        foodData.postValue(responseBody.data) // set data to LiveData
                    } else {
                        Log.e(TAG, "onResponse: response body is null")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FoodByIdResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getAllFood() = viewModelScope.launch {
        trackoriApi.getAllFoods().enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        AllfoodData.postValue(responseBody.data) // set data to LiveData
                    } else {
                        Log.e(TAG, "onResponse: response body is null")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    fun addCalorieHistory(uid: String, data: CalorieHistoryData) {
        viewModelScope.launch {
            trackoriApi.addCalorieHistory(uid, data).enqueue(object : Callback<CalorieHistoryDataResponse> {
                override fun onResponse(
                    call: Call<CalorieHistoryDataResponse>,
                    response: Response<CalorieHistoryDataResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            // Handle successful response
                            Log.d(TAG, "Successfully added calorie history: ${responseBody.data}")
                        } else {
                            Log.e(TAG, "onResponse: response body is null")
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<CalorieHistoryDataResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }


    }

    fun updateCalorieHistory(uid: String, docId: String, data: CalorieHistoryData) {
        viewModelScope.launch {
            trackoriApi.editCalorieHistory(uid, docId, data).enqueue(object : Callback<CalorieHistoryDataResponse> {
                override fun onResponse(
                    call: Call<CalorieHistoryDataResponse>,
                    response: Response<CalorieHistoryDataResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            // Handle successful response
                            Log.d(TAG, "Successfully updated calorie history: ${responseBody.data}")
                        } else {
                            Log.e(TAG, "onResponse: response body is null")
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<CalorieHistoryDataResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    fun fetchFoodHistoryData(uid: String, realDate: String) {
        trackoriApi.getCalorieHistoryByDate(uid, realDate).enqueue(object : Callback<CalorieHistoryResponse> {
            override fun onResponse(call: Call<CalorieHistoryResponse>, response: Response<CalorieHistoryResponse>) {
                val data = response.body()?.data.orEmpty().filter { it.name?.isNotBlank() == true }
                foodHistoryData.postValue(data)
            }

            override fun onFailure(call: Call<CalorieHistoryResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }

    fun fetchUserInfo(uid: String) {
        trackoriApi.getUserInfo(uid).enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null && userResponse.success) {
                        val user = userResponse.data
                        userInfo.postValue(user)
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // Handle error
            }
        })
    }

}