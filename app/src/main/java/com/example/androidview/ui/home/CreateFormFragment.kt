package com.example.androidview.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidview.R
import com.example.androidview.database.AppDatabase
import com.example.androidview.database.OptionEntity
import com.example.androidview.database.PollEntity
import com.example.androidview.databinding.FragmentCreateFormBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale
import kotlinx.coroutines.*

class CreateFormFragment : Fragment() {

    private var _binding: FragmentCreateFormBinding? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreateFormBinding.inflate(inflater, container, false)
        view?.let { super.onViewCreated(it, savedInstanceState) }
        setupListeners()

        return binding.root
    }

    private fun setupListeners() {

        binding.firstRemoveButton.setOnClickListener {
            removeAnswerField(it)
        }

        binding.addAnswerButton.setOnClickListener {
            addAnswerField()
        }

        binding.deadlineButton.setOnClickListener {
            showDateTimePicker()
        }

        binding.joinButton.setOnClickListener {
            Log.d("CreatePoll", "Create button clicked")
            if (validateInputs()) {
                Log.d("CreatePoll", "Inputs are valid")
                showConfirmationDialog()
            } else {
                Log.d("CreatePoll", "Inputs are invalid")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        coroutineScope.cancel()
    }

    private fun addAnswerField() {
        val context = requireContext()  // Ensure you have a valid context

        val answerInput = EditText(context).apply {
            layoutParams = LinearLayout.LayoutParams( 0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f  // Use weight to make EditText expand and fill space
            )
            hint = getString(R.string.enter_answer)
            inputType = InputType.TYPE_CLASS_TEXT
        }

        val removeButton = ImageButton(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setImageResource(R.drawable.ic_remove)
            setOnClickListener { removeAnswerField(it) }
        }

        // Create a horizontal LinearLayout that will contain the remove button and the answer input
        val answerLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL

            addView(removeButton)  // Add the remove button first
            addView(answerInput)  // Then add the answer input field
        }

        // Add the new LinearLayout to the answer container
        binding.answerContainer.addView(answerLayout)
    }
    fun removeAnswerField(view: View) {
        val parentLayout = view.parent as? ViewGroup  // This should be the LinearLayout containing the button and the EditText
        parentLayout?.let { layout ->
            val container = layout.parent as? ViewGroup  // This should be the answerContainer
            container?.removeView(layout)
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                // Update TextView with selected date and time
                binding.selectedDateTimeText.text = dateFormat.format(calendar.time)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        DatePickerDialog(requireContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun validateInputs(): Boolean {
        val question = binding.questionInput.text.toString().trim()

        // Check if the question is filled
        if (question.isEmpty()) {
            binding.questionInput.error = "Molimo unesite pitanje"
            return false
        }

        // Check if at least one answer is filled
        var atLeastOneAnswerFilled = false
        binding.answerContainer.children.forEach { view ->
            if (view is LinearLayout) {
                val editText = view.getChildAt(1) as? EditText  // Assuming the EditText is the second child
                if (!editText?.text.toString().trim().isEmpty()) {
                    atLeastOneAnswerFilled = true
                }
            }
        }
        if (!atLeastOneAnswerFilled) {
            Toast.makeText(context, "Molimo unesite barem jedan odgovor", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(context)
            .setMessage("Jeste li sigurni da želite kreirati ovo glasanje?")
            .setPositiveButton("Da") { dialog, which ->
                savePollToDatabase()
            }
            .setNegativeButton("Ne") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun savePollToDatabase() {
        coroutineScope.launch {
            val poll = createPollEntity()
            val pollId = withContext(Dispatchers.IO) {  // Perform database operations on the IO dispatcher
                AppDatabase.getDatabase(requireContext()).pollDao().insertPoll(poll)
            }

            // Iterate over each LinearLayout containing the EditText and ImageButton
            binding.answerContainer.children.forEach { container ->
                if (container is LinearLayout) {
                    // Assuming EditText is the second child in the LinearLayout
                    val editText = container.getChildAt(1) as? EditText
                    editText?.let {
                        val answerText = it.text.toString().trim()
                        if (answerText.isNotEmpty()) {
                            saveOption(answerText, pollId)
                        }
                    }
                }
            }


            withContext(Dispatchers.Main) {
                showPollCreatedSuccess()
            }
        }
    }

    private fun createPollEntity(): PollEntity {
        return PollEntity(
            title = binding.questionInput.text.toString().trim(),
            description = binding.descriptionInput.text.toString().trim(),
            allowMultipleVotes = binding.multipleVotesSwitch.isChecked,
            allowVoterAddedAnswers = binding.allowAddAnswersSwitch.isChecked,
            isAnonymous = binding.showVoterDetailSwitch.isChecked,
            isPasswordProtected = binding.pollWithPasswordSwitch.isChecked,
            password = if (binding.pollWithPasswordSwitch.isChecked) binding.passwordInput.text.toString() else null,
            createdAt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(java.util.Calendar.getInstance().time),
            expiresAt = binding.selectedDateTimeText.text.toString().takeIf { it.isNotEmpty() }
        )
    }

    private suspend fun saveOption(optionText: String, pollId: Long) {
        val option = OptionEntity(
            pollId = pollId.toInt(),
            optionText = optionText,
            isUserAdded = false
        )
        withContext(Dispatchers.IO) {  // Ensure database operation is on the IO thread
            AppDatabase.getDatabase(requireContext()).optionDao().insertOption(option)
        }
    }


    private fun showPollCreatedSuccess() {
        Toast.makeText(context, "Glasanje je uspješno kreirano.", Toast.LENGTH_SHORT).show()
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.visibility = View.VISIBLE
        // Navigate back or to the poll display fragment/activity
        findNavController().navigate(R.id.navigation_home)
    }


}