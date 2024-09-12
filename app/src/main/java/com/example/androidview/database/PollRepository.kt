package com.example.androidview.database

import android.util.Log
import androidx.lifecycle.LiveData
import org.mindrot.jbcrypt.BCrypt

class PollRepository(private val pollDao: PollDao) {
    fun getOptionsForPoll(pollId: Int): LiveData<List<OptionEntity>> {
        return pollDao.getOptionsForPoll(pollId)
    }

    fun joinPoll(pollId: Int, userId: Int): Boolean{
        return try {
            pollDao.addUserToPoll(userId, pollId)
            true
        } catch (e: Exception) {
            Log.e("PollRepository", "Error joining poll", e)
            false
        }

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