package com.example.androidview.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.androidview.R
import com.example.androidview.databinding.FragmentCreateFormBinding
import java.util.Locale

class CreateFormFragment : Fragment() {

    private var _binding: FragmentCreateFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreateFormBinding.inflate(inflater, container, false)

        binding.addAnswerButton.setOnClickListener {
            addAnswerField()
        }

        binding.deadlineButton.setOnClickListener {
            showDateTimePicker()
        }

        binding.joinButton.setOnClickListener {
            if (validateInputs()) {
                showConfirmationDialog()
            }
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addAnswerField() {
        val context = requireContext()  // Ensure you have a valid context

        val answerInput = EditText(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            hint = getString(R.string.enter_answer)
            inputType = InputType.TYPE_CLASS_TEXT
        }

        val removeButton = ImageButton(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setImageResource(R.drawable.ic_remove)  // Ensure you have this drawable
            setOnClickListener {
                // Remove the entire LinearLayout (answerLayout) containing both the button and the input
                binding.answerContainer.removeView((it.parent as View))
            }
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
        val answer = binding.answerInput.text.toString().trim()

        var isValid = true

        // Check if the question is filled
        if (question.isEmpty()) {
            binding.questionInput.error = "Molimo unesite pitanje"
            isValid = false
        }

        // Check if at least one answer is filled
        if (answer.isEmpty()) {
            binding.answerInput.error = "Molimo unesite barem jedan odgovor"
            isValid = false
        }

        // Add more checks as needed (e.g., for additional dynamically added answers)

        return isValid
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(context)
            .setMessage("Jeste li sigurni da želite kreirati ovo glasanje?")
            .setPositiveButton("Da") { dialog, which ->
                // Handle the confirmation of creating the poll
            }
            .setNegativeButton("Ne") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }


}