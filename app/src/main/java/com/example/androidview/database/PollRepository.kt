package com.example.androidview.database

import androidx.lifecycle.LiveData

class PollRepository(private val pollDao: PollDao) {
    fun getOptionsForPoll(pollId: Int): LiveData<List<OptionEntity>> {
        return pollDao.getOptionsForPoll(pollId)
    }
}