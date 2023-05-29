package com.example.trackori

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.commit
import com.example.trackori.api.ApiConfig
import com.example.trackori.api.LogoutResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var preferencesHelper: PreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Trackori)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferencesHelper = PreferencesHelper(this)

        supportActionBar?.hide()

        val shouldLoadLanding = intent.getBooleanExtra("EXTRA_LOAD_LANDING", true)


        if (shouldLoadLanding && savedInstanceState == null && !preferencesHelper.isLoggedIn) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragmentContainer, LandingPageFragment())
            }
        }

        val logoutButton: Button = findViewById(R.id.btnLogout)
        logoutButton.setOnClickListener {
            logoutUser()
        }

        if (preferencesHelper.isLoggedIn) {
            logoutButton.visibility = View.VISIBLE
        } else {
            logoutButton.visibility = View.GONE
        }
    }
    private fun logoutUser() {
        val apiService = ApiConfig.getApiService()
        val call = apiService.logout()
        call.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    val logoutResponse = response.body()
                    if (logoutResponse?.success == true) {
                        preferencesHelper.clear()


                        val intent = Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Handle logout error
                        // Show an error message or perform any necessary action
                    }
                } else {
                    // Handle API call error
                    // Show an error message or perform any necessary action
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                // Handle API call failure
                // Show an error message or perform any necessary action
            }
        })
    }
}