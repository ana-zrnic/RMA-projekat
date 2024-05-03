package com.example.androidview.database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PollDao {
    @Insert
    fun insertPoll(poll: PollEntity): Long

    @Query("SELECT * FROM polls")
    fun getAllPolls(): List<PollEntity>
}