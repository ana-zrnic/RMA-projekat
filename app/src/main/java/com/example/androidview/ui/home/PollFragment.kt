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
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
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
        val pollOptionsLayout = view.findViewById<LinearLayout>(R.id.pollOptionsLayout)

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
                    val infoButton = ImageButton(context)
                    infoButton.setImageResource(R.drawable.ic_info)
                    infoButton.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                    ).apply { addRule(RelativeLayout.ALIGN_PARENT_RIGHT) }

                    infoButton.setOnClickListener {
                        viewModel.getResponsesForOption(option.optionId).observe(viewLifecycleOwner, Observer { responses ->
                            val message = StringBuilder()
                            message.append("Broj glasova: ${responses.size}\n")
                            if (!poll.isAnonymous) {
                                val userIds = responses.map { it.userId }.filterNotNull()
                                viewModel.getUsernames(userIds).observe(viewLifecycleOwner, Observer { usernames ->
                                    message.append("Korisnici koji su glasali za opciju:\n")
                                    for (username in usernames) {
                                        message.append("$username\n")
                                    }
                                    // Show the AlertDialog
                                    AlertDialog.Builder(requireContext())
                                        .setTitle("Info for option ${option.optionText}")
                                        .setMessage(message.toString())
                                        .setPositiveButton("OK", null)
                                        .show()
                                })
                            }
                            else {
                                val message = StringBuilder()
                                message.append("Broj glasova: ${responses.size}\n")
                                // Show the AlertDialog
                                AlertDialog.Builder(requireContext())
                                    .setTitle("Info for option ${option.optionText}")
                                    .setMessage(message.toString())
                                    .setPositiveButton("OK", null)
                                    .show()
                                }
                        })
                    }
                    val layout = RelativeLayout(context)
                    layout.addView(optionView)
                    layout.addView(infoButton)

                    pollOptionsLayout.addView(layout)
                }

                //val poll = viewModel.getPollById(pollId)
                if (poll.hasExpired()) {
                    for (i in 0 until pollOptionsLayout.childCount) {
                        val answerView = pollOptionsLayout.getChildAt(i)
                        //answerView.isEnabled = false
                        answerView.isClickable = false
                    }
                }
                })
                viewModel.hasUserVoted(userId, pollId).observe(viewLifecycleOwner, Observer { hasVoted ->
                    if (hasVoted) {
                        viewModel.getResponses(userId, pollId).observe(viewLifecycleOwner, Observer { responses ->
                            for (response in responses) {
                                for (i in 0 until pollOptionsLayout.childCount) {
                                    val layout = pollOptionsLayout.getChildAt(i) as RelativeLayout
                                    for (j in 0 until layout.childCount) {
                                        val childView = layout.getChildAt(j)
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
                        // Loop through all children of pollOptionsLayout
                        for (i in 0 until pollOptionsLayout.childCount) {
                            val layout = pollOptionsLayout.getChildAt(i) as RelativeLayout
                            for (j in 0 until layout.childCount) {
                                val childView = layout.getChildAt(j)
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
                                    val optionView = childView
                                    // Check if the CheckBox is selected
                                    if (optionView.isChecked) {
                                        val votedAt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
                                        val response = ResponseEntity(optionId = optionView.id, pollId = pollId, userId = userId, votedAt = votedAt)
                                        viewModel.saveResponse(response)
                                    }
                                    // Disable further clicks on this CheckBox
                                    optionView.isClickable = false
                                }
                            }
                        }

                        // Disable the confirm button after submitting
                        confirmButton.isEnabled = false

                        // Make all options unclickable
                        for (i in 0 until pollOptionsLayout.childCount) {
                            val optionView = pollOptionsLayout.getChildAt(i)
                            optionView.isClickable = false
                        }
                    }
                    .setNegativeButton("Ne", null)
                    .show()
            }
        }
    }
}