package com.example.androidview.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.androidview.R
import com.example.androidview.databinding.FragmentDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addFab.setOnClickListener {
            // Handle the click event, e.g., navigate to another fragment or show a dialog
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            // Toggle visibility
            if (bottomNavigationView.isVisible) {
                bottomNavigationView.visibility = View.GONE // Hide
            } else {
                bottomNavigationView.visibility = View.VISIBLE // Show
            }
            findNavController().navigate(R.id.joinFormFragment)
            showBackButton(true)
        }

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
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