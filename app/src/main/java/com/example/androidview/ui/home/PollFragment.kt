package com.example.androidview.ui.home

import android.app.AlertDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.example.androidview.database.ResponseEntity
import java.util.Date
import java.util.Locale

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
        val sharedPref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", -1)

        super.onViewCreated(view, savedInstanceState)
        val pollId = arguments?.getInt("pollId")
        val title = arguments?.getString("title")
        val description = arguments?.getString("description")
        val allowMultipleVotes = arguments?.getBoolean("allowMultipleVotes") ?: false

        val titleTextView = view.findViewById<TextView>(R.id.pollTitle)
        titleTextView.text = title
        val descriptionTextView = view.findViewById<TextView>(R.id.pollDescription)
        descriptionTextView.text = description

        val confirmButton = view.findViewById<Button>(R.id.confirmButton)
        confirmButton.isEnabled = false
        val pollOptionsGroup = view.findViewById<RadioGroup>(R.id.pollOptionsGroup)

        if (pollId != null) {
            viewModel.getPoll(pollId).observe(viewLifecycleOwner, Observer { poll ->
                viewModel.getOptionsForPoll(pollId).observe(viewLifecycleOwner, Observer { options ->
                for (option in options) {
                    val optionView = if (allowMultipleVotes) {
                        CheckBox(context).apply {
                            id = option.optionId
                            text = option.optionText
                            setOnClickListener {
                                Log.d("PollFragment", "Option with ID ${option.optionId} clicked")
                                confirmButton.isEnabled = true
                            }
                        }

                    } else {
                        RadioButton(context).apply {
                            id = option.optionId
                            text = option.optionText
                            setOnClickListener {
                                Log.d("PollFragment", "Option with ID ${option.optionId} clicked")
                                confirmButton.isEnabled = true
                            }
                        }
                    }
                    pollOptionsGroup.addView(optionView)
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
                viewModel.hasUserVoted(userId, pollId).observe(viewLifecycleOwner, Observer { hasVoted ->
                    if (hasVoted) {
                        viewModel.getResponses(userId, pollId).observe(viewLifecycleOwner, Observer { responses ->
                        for (response in responses) {
                                for (i in 0 until pollOptionsGroup.childCount) {
                                    val childView = pollOptionsGroup.getChildAt(i)
                                    if (childView is RadioButton) {
                                        val optionView = childView
                                        if (optionView.id == response.optionId) {
                                            optionView.isChecked = true
                                        }
                                        optionView.isClickable = false
                                    } else if (childView is CheckBox) {
                                        val optionView = childView
                                        if (optionView.id == response.optionId) {
                                            optionView.isChecked = true
                                        }
                                        optionView.isClickable = false
                                    }
                                }
                            }
                        })
                        confirmButton.isEnabled = false
                    }
                })
            })

            confirmButton.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Potvrdi izbor")
                    .setMessage("Da li ste sigurni da želite da nastavite")
                    .setPositiveButton("Da") { _, _ ->
                        // Loop through all children of pollOptionsGroup
                        for (i in 0 until pollOptionsGroup.childCount) {
                            val childView = pollOptionsGroup.getChildAt(i)

                            if (childView is RadioButton) {
                                val optionView = childView
                                // Check if the RadioButton is selected
                                if (optionView.isChecked) {
                                    val votedAt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
                                    val response = ResponseEntity(optionId = optionView.id, pollId = pollId, userId = userId, votedAt = votedAt)
                                    viewModel.saveResponse(response)
                                }
                                // Disable further clicks on this RadioButton
                                optionView.isClickable = false
                            } else if (childView is CheckBox) {
                                Log.d("PollFragment", "CheckBox clicked")
                                val optionView = childView
                                // Check if the CheckBox is selected
                                if (optionView.isChecked) {
                                    val votedAt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
                                    Log.d("PollFragment", "Option with ID ${optionView.id} and poll ID $pollId clicked and user ID $userId")
                                    val response = ResponseEntity(optionId = optionView.id, pollId = pollId, userId = userId, votedAt = votedAt)
                                    viewModel.saveResponse(response)
                                }
                                // Disable further clicks on this CheckBox
                                optionView.isClickable = false
                            }
                        }

                        // Disable the confirm button after submitting
                        confirmButton.isEnabled = false

                        // Make all options unclickable
                        for (i in 0 until pollOptionsGroup.childCount) {
                            val optionView = pollOptionsGroup.getChildAt(i)
                            optionView.isClickable = false
                        }
                    }
                    .setNegativeButton("Ne", null)
                    .show()
            }
        }
    }
}