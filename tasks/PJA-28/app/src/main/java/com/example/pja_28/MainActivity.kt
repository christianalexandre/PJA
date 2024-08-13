package com.example.pja_28

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.pja_28.databinding.ActivityMainBinding
import com.example.pja_28.util.NetworkUtils

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }
    private fun setupListeners(){
        val retrorfitTest = NetworkUtils.getRetrofitInstance("https://cdn.jsdelivr.net/")

        with(binding) {
            textViewAPI.text = retrorfitTest.toString()
        }
    }
}