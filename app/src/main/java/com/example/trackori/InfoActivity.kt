package com.example.trackori

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class InfoActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_profile

        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        val profileMenuItemView = menuView.getChildAt(2) as BottomNavigationItemView

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_info -> {
                    // Launch Info Activity or Fragment
                    val intent = Intent(this, InfoActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_camera -> {
                    // Launch Camera Activity or Fragment
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}