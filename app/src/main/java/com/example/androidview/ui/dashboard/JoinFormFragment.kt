package com.example.androidview.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidview.database.AppDatabase
import com.example.androidview.database.PollRepository
import com.example.androidview.databinding.FragmentJoinFormBinding
import com.example.androidview.ui.home.PollViewModel
import com.example.androidview.ui.home.PollViewModelFactory

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
            binding.passwordInput.error = null
            joinPoll(password)
            // Perform the join operation or whatever should happen on successful input
            updateUserJoinedPolls()
        }
    }

    private fun joinPoll(password: String) {
        val pollDao = AppDatabase.getDatabase(requireContext()).pollDao()
        val repository = PollRepository(pollDao)
        val factory = PollViewModelFactory(repository)

        val pollViewModel = ViewModelProvider(this, factory).get(PollViewModel::class.java)



        //val pollId = pollViewModel.getPollIdByPassword(password)

        //Log.d("JoinPoll", "Poll ID: $pollId")
        pollViewModel.getPollIdByPassword(password).observe(viewLifecycleOwner, Observer { pollId ->
            if (pollId != null) {
                val sharedPref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                val userId = sharedPref.getInt("userId", -1)

                pollViewModel.joinPoll(pollId, userId)
                Log.d("JoinPoll", "Join")
            } else {
                binding.passwordInput.error = "Anketa ne postoji ili je unesena pogresna lozinka"
            }
        })
    }

    private fun updateUserJoinedPolls() {
        // Here you should implement the logic to update the user's joined polls
        // This is just a placeholder and should be replaced with your actual implementation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
