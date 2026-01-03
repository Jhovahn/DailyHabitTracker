package com.example.habittracker.data

import androidx.room.*

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits")
    suspend fun getAll(): List<Habit>

    @Insert
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)
}