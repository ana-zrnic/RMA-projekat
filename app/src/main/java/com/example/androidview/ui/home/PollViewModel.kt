package com.example.androidview.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidview.database.OptionEntity
import com.example.androidview.database.PollRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PollViewModel(private val repository: PollRepository) : ViewModel() {
    private val pollRepository: PollRepository = repository



    fun getOptionsForPoll(pollId: Int): LiveData<List<OptionEntity>> {
        return pollRepository.getOptionsForPoll(pollId)
    }

    fun joinPoll(pollId: Int, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = pollRepository.joinPoll(pollId, userId)
            // Handle the result here, e.g., update a LiveData
        }
    }

    fun getPollIdByPassword(password: String): LiveData<Int?> {
        val pollId = MutableLiveData<Int?>()
        viewModelScope.launch(Dispatchers.IO) {
            pollId.postValue(pollRepository.getPollIdByPassword(password))
        }
        Log.d("pollVM", "Poll ID: $pollId")
        return pollId
    }
}