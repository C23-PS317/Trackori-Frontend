package com.example.trackori.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trackori.api.AllFoodItem
import com.example.trackori.api.FoodByIdData
import com.example.trackori.api.FoodItem
import com.example.trackori.databinding.ItemFoodBinding

class FoodListAdapter(private var foods: List<FoodByIdData>) : RecyclerView.Adapter<FoodListAdapter.FoodViewHolder>() {
    var onItemClick: ((FoodByIdData) -> Unit)? = null
    var onAddButtonClick: ((FoodByIdData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun getItemCount(): Int = foods.size

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foods[position])
    }

    fun setData(newFoods: List<FoodByIdData>) {
        this.foods = newFoods
        notifyDataSetChanged()
    }

    inner class FoodViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: FoodByIdData) {
            binding.foodName.text = "Nama Makanan: ${food.nama}"
            binding.foodKalori.text = "Total Kalori: ${food.kalori?.toString()} kcals"
            binding.foodSatuan.text = "Porsi: ${food.satuan}"

            Glide.with(binding.root)
            binding.buttonAdd.setOnClickListener {
                onAddButtonClick?.invoke(food)
            }
        }
    }
}