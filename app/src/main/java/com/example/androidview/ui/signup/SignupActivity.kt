package com.example.androidview.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.androidview.databinding.ActivitySignupBinding
import com.example.androidview.ui.UserViewModel
import kotlin.math.log

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel: UserViewModel by viewModels()

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SignupActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupUI()

    }

    private fun setupUI() {
        binding.signupButton.setOnClickListener {
            val username = binding.newUsername.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.newPassword.text.toString().trim()

            if (validateInput(username, email, password)) {
                viewModel.registerUser(username, email, password)
                Log.d("SignupActivity", "Registering user")
                observeViewModel()
            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        Log.d("SignupActivity", "Observing ViewModel")
        viewModel.registrationResult.observe(this) { isSuccess ->
            if (isSuccess) {
                Log.d("SignupActivity", "Registration successful")
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Registration failed or user exists", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun validateInput(username: String, email: String, password: String): Boolean {
        // Add your input validation logic here (e.g., check for empty fields, valid email pattern)
        return username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}