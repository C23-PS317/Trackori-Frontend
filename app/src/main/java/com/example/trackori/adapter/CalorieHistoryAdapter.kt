package com.example.trackori.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackori.api.CalorieHistoryItem
import com.example.trackori.api.CalorieHistoryItemDetail
import com.example.trackori.api.GroupedCalorieHistoryItem
import com.example.trackori.databinding.ActivityCalorieHistoryBinding
import com.example.trackori.databinding.ItemCalorieBinding

class CalorieHistoryAdapter(private var calorieHistoryList: List<CalorieHistoryItemDetail>) :
    RecyclerView.Adapter<CalorieHistoryAdapter.CalorieHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalorieHistoryViewHolder {
        val binding = ItemCalorieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun bind(item: CalorieHistoryItemDetail) { // changed from GroupedCalorieHistoryItem to CalorieHistoryItemDetail
            binding.date.text = item.date
            binding.totalCalorie.text = item.totalCalories.toString() + " kcals"

            binding.detailsLayout.visibility = View.GONE
            binding.extendButton.setOnClickListener {
                if (binding.detailsLayout.visibility == View.GONE) {
                    binding.detailsLayout.visibility = View.VISIBLE
                    binding.foodName.text = item.foodList.joinToString("\n") { "${it.name.split("_", " ").joinToString(separator = " ", transform = String::capitalize)} : ${it.calories} kcals" } // This will display the food name and its calories
                    //binding.foodCalories.text = item.totalCalories.toString()
                } else {
                    binding.detailsLayout.visibility = View.GONE
                }
            }
        }
    }

}