package com.example.trackori

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackori.R
import com.example.trackori.adapter.FoodListAdapter
import com.example.trackori.databinding.ActivityFoodListBinding
import com.example.trackori.viewmodel.FoodViewModel
import androidx.appcompat.widget.SearchView
import android.app.AlertDialog
import android.graphics.Color
import androidx.core.widget.addTextChangedListener
import com.example.trackori.api.FoodByIdData
import com.example.trackori.databinding.DialogPortionBinding


class FoodListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodListBinding
    private lateinit var viewModel: FoodViewModel
    private lateinit var adapter: FoodListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        supportActionBar?.hide()

        adapter = FoodListAdapter(listOf())
        binding.rvFoods.layoutManager = LinearLayoutManager(this)
        binding.rvFoods.adapter = adapter

        adapter.onAddButtonClick = { foodData ->
            // foodData is the data of the item where the add button was clicked
            showPortionDialog(foodData)
        }

        val foodName = intent.getStringExtra("searchQuery")
        foodName?.let { viewModel.getFoodById(it) }

        viewModel.foodData.observe(this, Observer { foodData ->
            if (foodData != null) {
                // Do something with the food data
                // For example, add it to a list and give that list to the adapter
                adapter.setData(listOf(foodData))
            } else {
                // Handle the error case
            }
        })
    }
    private fun showPortionDialog(foodData: FoodByIdData) {
        val dialogBinding = DialogPortionBinding.inflate(LayoutInflater.from(this))
        val dialogView = dialogBinding.root

        val satuanParts = foodData.satuan.split(" ")
        val defaultPortion = if (satuanParts[0].toIntOrNull() != null) satuanParts[0] else "1"

        // Set the default portion value
        dialogBinding.editTextPortion.setText(defaultPortion)

        // Display the unit
        val unit = if(satuanParts.size > 1) satuanParts[1] else ""
        dialogBinding.textViewPortionUnit.text = unit

        // Calculate and display the total calories
        val caloriesPerUnit = if (satuanParts[0].toIntOrNull() != null) foodData.kalori.toDouble() / satuanParts[0].toInt() else foodData.kalori.toDouble()
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

            positiveButton.setTextColor(Color.GREEN)
            negativeButton.setTextColor(Color.GREEN)

            positiveButton.setOnClickListener {
                val portion = dialogBinding.editTextPortion.text.toString().toInt()
                // Handle the portion here

                portionDialog.dismiss()
            }
        }

        // Update the total calories when the portion changes
        dialogBinding.editTextPortion.addTextChangedListener {
            val portion = it.toString().toIntOrNull() ?: 0
            dialogBinding.textViewTotalCalories.text = "Total Kalori: ${caloriesPerUnit * portion}"
        }

        portionDialog.show()
    }
}
