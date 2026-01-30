package com.jhovahn.habittracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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

    @Query("SELECT * FROM habit_table")
    suspend fun getAllOnce(): List<Habit>
}