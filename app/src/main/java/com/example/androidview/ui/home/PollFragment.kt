package com.example.androidview.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidview.R
import com.example.androidview.database.AppDatabase
import com.example.androidview.database.PollDao
import com.example.androidview.database.PollRepository

class PollFragment : Fragment() {
    private lateinit var viewModel: PollViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val pollDao = AppDatabase.getDatabase(requireContext()).pollDao()
        val repository = PollRepository(pollDao)
        val factory = PollViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PollViewModel::class.java)
        return inflater.inflate(R.layout.fragment_poll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pollId = arguments?.getInt("pollId")
        val title = arguments?.getString("title")
        val description = arguments?.getString("description")
        // Retrieve other data as needed
        val allowMultipleVotes = arguments?.getBoolean("allowMultipleVotes") ?: false

        // Find the views in the layout and set their values
        val titleTextView = view.findViewById<TextView>(R.id.pollTitle)
        titleTextView.text = title
        val descriptionTextView = view.findViewById<TextView>(R.id.pollDescription)
        descriptionTextView.text = description

        // Do the same for the other views
        val pollOptionsGroup = view.findViewById<RadioGroup>(R.id.pollOptionsGroup)
        if (pollId != null) {
            viewModel.getPoll(pollId).observe(viewLifecycleOwner, Observer { poll ->
            viewModel.getOptionsForPoll(pollId).observe(viewLifecycleOwner, Observer { options ->
                // Add a RadioButton for each option
                for (option in options) {
                    if (allowMultipleVotes) {
                        val checkBox = CheckBox(context)
                        checkBox.text = option.optionText
                        pollOptionsGroup.addView(checkBox)
                    } else {
                        Log.d("PollFragment", "Adding radio button")
                        val radioButton = RadioButton(context)
                        radioButton.text = option.optionText
                        pollOptionsGroup.addView(radioButton)
                    }
                }


                //val poll = viewModel.getPollById(pollId)
                if (poll.hasExpired()) {
                    for (i in 0 until pollOptionsGroup.childCount) {
                        val answerView = pollOptionsGroup.getChildAt(i)
                        //answerView.isEnabled = false
                        answerView.isClickable = false
                    }
                }
            })
            })
        }
    }
}