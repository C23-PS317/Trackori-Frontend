package com.example.trackori.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackori.PreferencesHelper
import com.example.trackori.api.CalorieHistoryItemDetail
import com.example.trackori.databinding.ItemCalorieBinding

class CalorieHistoryAdapter(private var calorieHistoryList: List<CalorieHistoryItemDetail>) :
    RecyclerView.Adapter<CalorieHistoryAdapter.CalorieHistoryViewHolder>() {
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalorieHistoryViewHolder {
        val binding = ItemCalorieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        preferencesHelper = PreferencesHelper(parent.context)
        return CalorieHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalorieHistoryViewHolder, position: Int) {
        holder.bind(calorieHistoryList[position])
    }

    override fun getItemCount(): Int = calorieHistoryList.size

    fun setData(newData: List<CalorieHistoryItemDetail>) {
        this.calorieHistoryList = newData
        notifyDataSetChanged()
    }

    inner class CalorieHistoryViewHolder(private val binding: ItemCalorieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalorieHistoryItemDetail) {
            binding.date.text = item.date
            val itemCalorie = item.totalCalories.toDouble()
            binding.totalCalorie.text = "${itemCalorie.toInt()} kcal"

            // Menghitung total kalori di semua item
            val totalCalorie = calorieHistoryList.sumOf { it.totalCalories.toDouble() }

            // Menghitung persentase kalori untuk item ini
            val percentage = ((itemCalorie / preferencesHelper.dailycalorie) * 100).toInt()

            binding.tvCurrCalorie.text = "${itemCalorie.toInt()}"
            binding.tvTotalCalorie.text = "of ${preferencesHelper.dailycalorie} kcal"

            // Mengatur persentase pada CircularProgressIndicator
            binding.circularProgressIndicator.progress = percentage

            binding.detailsLayout.visibility = View.GONE
            binding.extendButton.setOnClickListener {
                if (binding.detailsLayout.visibility == View.GONE) {
                    binding.detailsLayout.visibility = View.VISIBLE
                    binding.foodName.text = item.foodList.joinToString("\n") { "${it.name} : ${it.calories} kcal" } // This will display the food name and its calories
                } else {
                    binding.detailsLayout.visibility = View.GONE
                }
            }
        }
    }
}
