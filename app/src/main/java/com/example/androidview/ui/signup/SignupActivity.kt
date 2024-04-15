package com.example.androidview.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidview.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SignupActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupButton.setOnClickListener {
            val username = binding.newUsername.text.toString()
            val password = binding.newPassword.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Sign up logic here, usually involving a database
                Toast.makeText(this, "Signup Successful", Toast.LENGTH_LONG).show()
                // Optionally navigate back to login
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
            }
        }
    }
}