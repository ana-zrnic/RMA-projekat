package com.example.androidview.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.androidview.database.OptionEntity
import com.example.androidview.database.PollRepository

class PollViewModel(private val repository: PollRepository) : ViewModel() {
    fun getOptionsForPoll(pollId: Int): LiveData<List<OptionEntity>> {
        return repository.getOptionsForPoll(pollId)
    }
}