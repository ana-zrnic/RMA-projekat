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
    fun getAllPolls(userId: Int): LiveData<List<PollEntity>>

    @Query("SELECT * FROM options WHERE pollId = :pollId")
    fun getOptionsForPoll(pollId: Int): LiveData<List<OptionEntity>>
}