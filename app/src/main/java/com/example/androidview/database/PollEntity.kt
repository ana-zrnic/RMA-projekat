package com.example.androidview.database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "polls",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PollEntity(
    @PrimaryKey(autoGenerate = true) var pollId: Int = 0,
    var userId: Int,
    var title: String,
    var description: String,
    var allowMultipleVotes: Boolean,
    var allowVoterAddedAnswers: Boolean,
    var isAnonymous: Boolean,
    var isPasswordProtected: Boolean,
    var password: String? = null,
    var createdAt: String,  // Consider using a proper date type if using TypeConverters
    var expiresAt: String? = null,
    val votesCount: Int = 0
)