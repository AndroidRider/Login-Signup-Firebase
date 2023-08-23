package com.androidrider.login_signup_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidrider.login_signup_firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // logoutUser
        binding.btnLogout.setOnClickListener {
            val sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }
}