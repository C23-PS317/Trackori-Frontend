package com.example.trackori

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trackori.databinding.ActivityLoginBinding
import com.example.trackori.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}