package com.example.androidview.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Trenutno nemate aktivnih anketa"
    }
    val text: LiveData<String> = _text
}