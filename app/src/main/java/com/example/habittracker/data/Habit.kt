package com.example.habittracker.data
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
)
