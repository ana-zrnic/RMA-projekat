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
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["optionId"])]
)
data class ResponseEntity(
    @PrimaryKey(autoGenerate = true) var responseId: Int = 0,
    var optionId: Int,
    var userId: Int?,  // Nullable for anonymous polls
    var votedAt: String  // Consider using a proper date type if using TypeConverters
)
