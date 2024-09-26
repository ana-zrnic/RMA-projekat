package com.example.androidview.database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "responses",
    foreignKeys = [
        ForeignKey(entity = OptionEntity::class,
            parentColumns = arrayOf("optionId"),
            childColumns = arrayOf("optionId"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = PollEntity::class,
            parentColumns = arrayOf("pollId"),
            childColumns = arrayOf("pollId"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = UserEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.SET_NULL)  // Assuming userId is nullable for anonymous polls
    ],
    indices = [Index(value = ["optionId"]), Index(value = ["pollId"]), Index(value = ["userId"])]
)
data class ResponseEntity(
    @PrimaryKey(autoGenerate = true) var responseId: Int = 0,
    var optionId: Int,
    var pollId: Int,
    var userId: Int?,  // Nullable for anonymous polls
    var votedAt: String  // Consider using a proper date type if using TypeConverters
)
