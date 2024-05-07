package com.example.androidview.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidview.database.AppDatabase
import com.example.androidview.database.UserEntity
import com.example.androidview.database.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository

    // LiveData to handle login status
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> = _registrationResult

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
    }

    fun registerUser(userName: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.registerUser(userName, email, password)
            _registrationResult.postValue(result)
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.loginUser(email, password)
            _loginResult.postValue(result)
        }
    }

    fun getUserByEmail(email: String): UserEntity? {
        return userRepository.getUserByEmail(email)
    }

}