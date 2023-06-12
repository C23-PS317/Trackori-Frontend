package com.example.trackori.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackori.api.CalorieHistoryItem
import com.example.trackori.databinding.ActivityCalorieHistoryBinding
import com.example.trackori.databinding.ItemCalorieBinding

class CalorieHistoryAdapter(private var calorieHistoryList: List<CalorieHistoryItem>) :
    RecyclerView.Adapter<CalorieHistoryAdapter.CalorieHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalorieHistoryViewHolder {
        val binding = ItemCalorieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalorieHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalorieHistoryViewHolder, position: Int) {
        holder.bind(calorieHistoryList[position])
    }

    override fun getItemCount(): Int = calorieHistoryList.size

    fun setData(newData: List<CalorieHistoryItem>) {
        this.calorieHistoryList = newData
        notifyDataSetChanged()
    }

    inner class CalorieHistoryViewHolder(private val binding: ItemCalorieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalorieHistoryItem) {
            binding.date.text = item.date
            binding.totalCalorie.text = item.calories.toString()
        }
    }
}