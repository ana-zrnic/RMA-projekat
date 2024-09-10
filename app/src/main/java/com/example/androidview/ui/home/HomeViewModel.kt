package com.example.androidview.ui.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidview.database.AppDatabase
import androidx.lifecycle.AndroidViewModel
import com.example.androidview.database.PollEntity
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "Trenutno nemate aktivnih anketa"
    }
    val text: LiveData<String> = _text
    fun updateText(newText: String) {
        _text.value = newText
    }

    private val _polls = MutableLiveData<List<PollEntity>>()
    val polls: LiveData<List<PollEntity>> = _polls
    init {
        loadPolls()
    }

    private fun loadPolls() {
        viewModelScope.launch {
            val database = AppDatabase.getDatabase(getApplication<Application>().applicationContext)
            val sharedPref = getApplication<Application>().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            val userId = sharedPref.getInt("userId", -1)
            val pollsFromDb = database.pollDao().getAllPollsByUser(userId)
            pollsFromDb.observeForever { polls ->
                _polls.postValue(polls)
                if (polls.isEmpty()) {
                    updateText("Trenutno nemate aktivnih anketa")
                }
            }
        }
    }

}