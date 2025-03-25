package com.example.todo.modules.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.databinding.ActivitySplashBinding
import com.example.todo.modules.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    private var binding: ActivitySplashBinding? = null
    private var splashViewModel: SplashViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        actionBar?.hide()
        splashViewModel?.getAllTasks()

        splashViewModel?.getTasksSuccess?.observe(this) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }

    }
}