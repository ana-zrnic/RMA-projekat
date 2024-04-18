package com.example.androidview.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidview.MainActivity
import com.example.androidview.databinding.ActivityLoginBinding
import com.example.androidview.ui.UserViewModel
import com.example.androidview.ui.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            viewModel.loginUser(email, password)
        }
        viewModel.loginResult.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                // Navigate to next screen
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            } else {
                // Show error
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        })

        binding.gotoSignupButton.setOnClickListener {
            // Navigate to Signup Activity
            startActivity(SignupActivity.newIntent(this))
        }
    }
}