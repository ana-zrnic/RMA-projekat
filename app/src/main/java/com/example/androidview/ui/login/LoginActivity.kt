package com.example.androidview.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.androidview.MainActivity
import com.example.androidview.databinding.ActivityLoginBinding
import com.example.androidview.ui.UserViewModel
import com.example.androidview.ui.signup.SignupActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                lifecycleScope.launch(Dispatchers.IO) {
                    // Fetch the user's information
                    val user = viewModel.getUser(binding.email.text.toString())

                    // Store the user's information in the shared preferences
                    val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("email", user?.email)
                        putString("username", user?.userName)
                        putInt("userId", user?.id ?: -1)
                        apply()
                    }
                }
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