package com.example.androidview.database
import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ResponseDao {
    @Insert
    fun insertResponse(response: ResponseEntity): Long
}