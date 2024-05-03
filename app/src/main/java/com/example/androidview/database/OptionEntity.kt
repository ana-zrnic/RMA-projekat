package com.example.androidview.database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "options",
    foreignKeys = [
        ForeignKey(entity = PollEntity::class,
            parentColumns = arrayOf("pollId"),
            childColumns = arrayOf("pollId"),
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["pollId"])]
)
data class OptionEntity(
    @PrimaryKey(autoGenerate = true) var optionId: Int = 0,
    var pollId: Int,
    var optionText: String,
    var isUserAdded: Boolean = false
)