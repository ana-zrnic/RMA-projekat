package com.example.androidview.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button; in case of your activity, it's Up
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}