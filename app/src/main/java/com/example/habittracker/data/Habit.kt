package com.example.habittracker.data
import android.icu.util.Calendar
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration

@Entity(tableName = "habit_table")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val completed: Boolean = false,
    val streak: Int = 0,
    val lastCompleted: Long? = null,
    val timerDuration: Long = 25 * 60 * 1000,
    val timerEnd: Long? = null,
    val weeklyGoal: Int = 5,
    val weeklyCompleted: Int = 0,
    val weekStartTimestamp: Long = 0,
)

fun getMonday4AM(timestamp: Long): Long {
    val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 4)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    if(calendar.timeInMillis > timestamp) {
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
    }
    return calendar.timeInMillis
}
