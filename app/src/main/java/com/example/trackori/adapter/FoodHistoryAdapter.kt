package com.example.trackori.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.trackori.api.CalorieHistoryItem
import com.example.trackori.databinding.ItemHistoryBinding

class FoodHistoryAdapter(private var foodHistoryList: List<CalorieHistoryItem>) :
    RecyclerView.Adapter<FoodHistoryAdapter.FoodHistoryViewHolder> () {

    inner class FoodHistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalorieHistoryItem) {
            binding.foodName.text = item.name.split(" ", "_").joinToString(separator = " ", transform = String::capitalize)
            binding.foodKalori.text = "${item.calories} kcals"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodHistoryList.size
    }

    override fun onBindViewHolder(holder: FoodHistoryViewHolder, position: Int) {
        holder.bind(foodHistoryList[position])
    }

    fun setData(it: List<CalorieHistoryItem>) {
        this.foodHistoryList = it
        notifyDataSetChanged()
    }


}