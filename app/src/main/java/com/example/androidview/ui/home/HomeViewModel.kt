package com.example.androidview.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidview.database.AppDatabase
import androidx.lifecycle.AndroidViewModel
import com.example.androidview.database.PollEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
            val pollsFromDb = withContext(Dispatchers.IO) {
                database.pollDao().getAllPolls()
            }
            _polls.postValue(pollsFromDb)
        }
    }

}