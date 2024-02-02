package com.example.cantaraapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.example.cantaraapps.R
import com.example.cantaraapps.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.root.startAnimation(fadeIn)

        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        Handler().postDelayed({
            val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            binding.root.startAnimation(fadeOut)
            if (isLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 3000)
    }
}