package com.example.androidview.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidview.databinding.FragmentJoinFormBinding

class JoinFormFragment : Fragment() {

    private var _binding: FragmentJoinFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentJoinFormBinding.inflate(inflater, container, false)

        binding.joinButton.setOnClickListener {
            validatePassword()
        }

        return binding.root
    }

    private fun validatePassword() {
        val password = binding.passwordInput.text.toString().trim()
        if (password.isEmpty()) {
            binding.passwordInput.error = "Molimo unesite lozinku"
        } else {
            // Proceed with the operation, e.g., join the form
            // You might want to clear the error if the user corrects it
            binding.passwordInput.error = null
            // Perform the join operation or whatever should happen on successful input
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
