package com.example.androidview.database
import android.icu.text.SimpleDateFormat
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date
import java.util.Locale

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
){
    fun hasExpired(): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val expiresDate = expiresAt?.let { dateFormat.parse(it) }
        val currentDate = Date()
        return expiresDate?.before(currentDate) ?: false
    }
}