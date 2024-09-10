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
    fun getPollById(pollId: Int): PollEntity?

    @Query("INSERT INTO user_polls VALUES (:userId, :pollId)")
    fun addUserToPoll(userId: Int, pollId: Int)

    @Query("SELECT * FROM polls WHERE password = :password LIMIT 1")
    fun getPollByPassword(password: String): PollEntity?


}