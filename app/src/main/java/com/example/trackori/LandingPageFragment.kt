package com.example.trackori

import ImageAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.trackori.databinding.ActivityLandingPageBinding
import com.example.trackori.databinding.ActivityLoginBinding
import java.lang.Math.abs
import java.util.*

class LandingPageFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var binding: ActivityLandingPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_landing_page, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        viewPager.adapter =
            ImageAdapter(listOf(R.drawable.landing_background))
        viewPager.isUserInputEnabled = false


        loginButton = view.findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            // Navigate to login
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton = view.findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            // Navigate to register
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}