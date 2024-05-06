package com.example.androidview.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.androidview.R

class PollFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pollId = arguments?.getInt("pollId")
        val title = arguments?.getString("title")
        val description = arguments?.getString("description")
        // Retrieve other data as needed

        // Find the views in the layout and set their values
        val titleTextView = view.findViewById<TextView>(R.id.pollTitle)
        titleTextView.text = title
        val descriptionTextView = view.findViewById<TextView>(R.id.pollDescription)
        descriptionTextView.text = description
        // Do the same for the other views
    }
}