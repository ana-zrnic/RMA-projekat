package com.example.androidview.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidview.R
import com.example.androidview.database.PollEntity
import com.example.androidview.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.androidview.ui.home.OnPollClickListener

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setupRecyclerView()
        setupListeners()

        viewModel.polls.observe(viewLifecycleOwner) { polls ->
            (binding.pollsRecyclerView.adapter as PollAdapter).updatePolls(polls)
        }
    }

    private fun setupListeners() {
        binding.addFabHome.setOnClickListener {
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigationView.visibility = View.GONE // Hide
            findNavController().navigate(R.id.createFormFragment)
            showBackButton(true)
        }
    }

    private fun setupRecyclerView() {
        binding.pollsRecyclerView.layoutManager = LinearLayoutManager(context) // Set the LayoutManager
        binding.pollsRecyclerView.adapter = PollAdapter(emptyList(), object : OnPollClickListener {
            override fun onPollClick(poll: PollEntity) {
                Log.d("MyTag", "CLICKED")
                val bundle = Bundle().apply {
                    putInt("pollId", poll.pollId)
                    putString("title", poll.title)
                    putString("description", poll.description)
                    // Add other data as needed
                    putBoolean("allowMultipleVotes", poll.allowMultipleVotes)
                }
                val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
                bottomNavigationView.visibility = View.GONE
                findNavController().navigate(R.id.action_navigation_home_to_pollFragment, bundle)
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showBackButton(enable: Boolean) {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(enable)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowHomeEnabled(enable)
    }
}