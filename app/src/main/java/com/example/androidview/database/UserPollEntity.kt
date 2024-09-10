package com.example.androidview.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.androidview.database.PollEntity
import com.example.androidview.database.UserEntity

@Entity(
    tableName = "user_polls",
    primaryKeys = ["userId", "pollId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PollEntity::class,
            parentColumns = ["pollId"],
            childColumns = ["pollId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["pollId"])
    ]
)
data class UserPollEntity(
    val userId: Int,
    val pollId: Int
)