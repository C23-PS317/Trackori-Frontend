package com.example.trackori.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trackori.api.AllFoodItem
import com.example.trackori.api.FoodByIdData
import com.example.trackori.api.FoodItem
import com.example.trackori.databinding.ItemFoodBinding

class FoodListAdapter(private var foods: List<FoodByIdData>, private var allFoods: List<AllFoodItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick: ((FoodByIdData) -> Unit)? = null
    var onAddButtonClick: ((FoodByIdData) -> Unit)? = null

    private val TYPE_FOOD_BY_ID = 0
    private val TYPE_ALL_FOOD = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when(viewType) {
            TYPE_FOOD_BY_ID -> FoodByIdViewHolder(binding)
            TYPE_ALL_FOOD -> AllFoodViewHolder(binding)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = foods.size + allFoods.size

    override fun getItemViewType(position: Int): Int {
        return if (position < foods.size) {
            TYPE_FOOD_BY_ID
        } else {
            TYPE_ALL_FOOD
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FoodByIdViewHolder) {
            holder.bind(foods[position])
        } else if (holder is AllFoodViewHolder) {
            holder.bind(allFoods[position - foods.size]) // offset position by the size of the foods list
        }
    }

    fun setData(newFoods: List<FoodByIdData>) {
        this.foods = newFoods
        notifyDataSetChanged()
    }

    fun setDataAllFood(newAllFoods: List<AllFoodItem>) {
        this.allFoods = newAllFoods
        notifyDataSetChanged()
    }

    inner class FoodByIdViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: FoodByIdData) {
            binding.foodName.text = "Nama Makanan: ${food.nama}"
            binding.foodKalori.text = "Total Kalori: ${food.kalori?.toString()} kcals"
            binding.foodSatuan.text = "Porsi: ${food.satuan}"


            Glide.with(binding.root).load(food.image_db)
                .into(binding.foodImage)
            binding.buttonAdd.setOnClickListener {
                onAddButtonClick?.invoke(food)
            }
        }
    }

    inner class AllFoodViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: AllFoodItem) {
            binding.foodName.text = "Nama Makanan: ${food.nama.split("_", " ").joinToString(separator = " ", transform = String::capitalize)}"
            binding.foodKalori.text = "Total Kalori: ${food.kalori?.toString()} kcals"
            binding.foodSatuan.text = "Porsi: ${food.satuan.split(" ").joinToString(separator = " ", transform = String::capitalize)}"

            Glide.with(binding.root).load(food.image_db)
                .into(binding.foodImage)
            binding.buttonAdd.setOnClickListener {
                // Handle this case appropriately as AllFoodItem might not have the same properties as FoodByIdData
                val foodByIdData = FoodByIdData(food.id, food.nama, food.kalori, food.satuan, food.image_db)
                onAddButtonClick?.invoke(foodByIdData)
            }
        }
    }
}