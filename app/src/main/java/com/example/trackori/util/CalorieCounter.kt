package com.example.trackori.util

fun calculateDailyCalorieNeeds(height: Float, weight: Float, age: Int, gender: String?, plan: String): Float {
    val bmr: Float
    val genderFactor: Double
    val planFactor: Double

    if (gender == "male") {
        bmr = 10 * weight + 6.25f * height - 5f * age + 5f
    } else {
        bmr = 10 * weight + 6.25f * height - 5f * age - 161f
    }

    when (plan) {
        "defisit" -> planFactor = 1.225 // Reduce calorie intake by 21%
        "bulking" -> planFactor = 1.875 // Increase calorie intake by 21%
        else -> planFactor = 1.55 // No plan, maintain current calorie intake
    }

    return (bmr * planFactor).toFloat()
}