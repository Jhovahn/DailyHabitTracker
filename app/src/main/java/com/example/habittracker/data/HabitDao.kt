package com.example.habittracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit_table")
    fun getAll(): Flow<List<Habit>>

    @Insert
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT * FROM habit_table WHERE id = :id")
    suspend fun getById(id: Long): Habit?
}