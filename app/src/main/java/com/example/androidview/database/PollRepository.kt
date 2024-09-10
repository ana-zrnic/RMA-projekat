package com.example.androidview.database

import android.util.Log
import androidx.lifecycle.LiveData
import org.mindrot.jbcrypt.BCrypt

class PollRepository(private val pollDao: PollDao) {
    fun getOptionsForPoll(pollId: Int): LiveData<List<OptionEntity>> {
        return pollDao.getOptionsForPoll(pollId)
    }

    fun joinPoll(pollId: Int, userId: Int) {
            pollDao.addUserToPoll(userId, pollId)
            Log.d("pollrepo", "ODRADJENO")
    }
    fun getPollIdByPassword(password: String): Int? {
        val polls = pollDao.getAllPolls()

        for (poll in polls) {
            if (BCrypt.checkpw(password, poll.password)) {
                return poll.pollId
            }
        }
        return null
    }
}