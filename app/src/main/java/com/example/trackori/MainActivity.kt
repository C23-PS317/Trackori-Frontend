package com.example.trackori

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.commit
import com.example.trackori.api.ApiConfig
import com.example.trackori.api.LogoutResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Trackori)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_info -> {
                    // Launch Info Activity or Fragment
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

        preferencesHelper = PreferencesHelper(this)

        val shouldLoadLanding = intent.getBooleanExtra("EXTRA_LOAD_LANDING", true)

        if (shouldLoadLanding && savedInstanceState == null && !preferencesHelper.isLoggedIn) {
            bottomNavigationView.visibility = View.GONE
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragmentContainer, LandingPageFragment())
            }
        } else {
            bottomNavigationView.visibility = View.VISIBLE
            supportActionBar?.show()
        }
    }
}