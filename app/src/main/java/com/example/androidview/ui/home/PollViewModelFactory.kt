package com.example.androidview.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidview.database.PollRepository

class PollViewModelFactory(private val repository: PollRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PollViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PollViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}