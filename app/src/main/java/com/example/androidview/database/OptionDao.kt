package com.example.androidview.database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OptionDao {
    @Insert
    fun insertOption(option: OptionEntity): Long

    @Query("SELECT * FROM options WHERE pollId = :pollId")
    fun getOptionsForPoll(pollId: Int): List<OptionEntity>
}