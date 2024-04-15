package com.example.androidview.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidview.MainActivity
import com.example.androidview.databinding.ActivityLoginBinding
import com.example.androidview.ui.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            if (username == "admin" && password == "admin") {
                // Navigate to MainActivity on successful login
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Close login activity
            } else {
                // Login failed
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show()
            }
        }

        binding.gotoSignupButton.setOnClickListener {
            // Navigate to Signup Activity
            startActivity(SignupActivity.newIntent(this))
        }
    }
}