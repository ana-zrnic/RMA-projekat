package com.example.androidview.database
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PollDao {
    @Insert
    fun insertPoll(poll: PollEntity): Long

    @Query("SELECT * FROM polls WHERE userId = :userId")
    fun getAllPollsByUser(userId: Int): LiveData<List<PollEntity>>

    @Query("SELECT * FROM polls")
    fun getAllPolls(): List<PollEntity>

    @Query("SELECT * FROM options WHERE pollId = :pollId")
    fun getOptionsForPoll(pollId: Int): LiveData<List<OptionEntity>>

    @Query("SELECT * FROM polls WHERE pollId = :pollId")
    fun getPollById(pollId: Int): LiveData<PollEntity>

    @Query("INSERT INTO user_polls VALUES (:userId, :pollId)")
    fun addUserToPoll(userId: Int, pollId: Int)

    @Query("SELECT * FROM polls WHERE pollId IN (SELECT pollId FROM user_polls WHERE userId = :userId)")
    fun getAllUserPollsByUser(userId: Int): LiveData<List<PollEntity>>

    @Query("SELECT * FROM polls WHERE password = :password LIMIT 1")
    fun getPollByPassword(password: String): PollEntity?

    @Insert
    fun saveResponse(response: ResponseEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM responses WHERE userId = :userId AND pollId = :pollId)")
    fun hasUserVoted(userId: Int, pollId: Int): LiveData<Boolean>

    @Query("SELECT * FROM responses WHERE userId = :userId AND pollId = :pollId")
    fun getResponses(userId: Int, pollId: Int): LiveData<List<ResponseEntity>>

    @Query("SELECT COUNT(*) FROM responses WHERE pollId = :pollId")
    fun getVoteCount(pollId: Int): LiveData<Int>

    @Query("SELECT * FROM responses WHERE optionId = :optionId")
    fun getResponsesForOption(optionId: Int): LiveData<List<ResponseEntity>>

    @Query("SELECT user_name FROM users WHERE id IN (:userIds)")
    fun getUsernames(userIds: List<Int>): LiveData<List<String>>


}