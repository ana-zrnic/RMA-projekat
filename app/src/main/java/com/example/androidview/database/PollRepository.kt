package com.example.androidview.database

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt

class PollRepository(private val pollDao: PollDao) {

    suspend fun saveResponse(response: ResponseEntity) {
        withContext(Dispatchers.IO) {
            pollDao.saveResponse(response)
        }
    }

    fun getPoll(id: Int): LiveData<PollEntity> {
        return pollDao.getPollById(id)
    }
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

    fun hasUserVoted(userId: Int, pollId: Int): LiveData<Boolean> {
        return pollDao.hasUserVoted(userId, pollId)
    }

    fun getResponses(userId: Int, pollId: Int): LiveData<List<ResponseEntity>> {
        return pollDao.getResponses(userId, pollId)
    }
}