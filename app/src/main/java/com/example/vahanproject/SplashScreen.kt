package com.example.vahanproject

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.vahanproject.databinding.ActivitySlpashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySlpashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlpashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashLogo.speed = 1.5f

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Finish the current (source) activity
            finish()
        }, 700) // 5000 milliseconds (5 seconds)

    }
}