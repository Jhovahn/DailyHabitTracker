package com.example.habittracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.HabitDao
import com.example.habittracker.data.Habit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.delay


class HabitViewModel(private val dao: HabitDao): ViewModel() {
    val habits = dao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            dao.delete(habit)
        }
    }

    fun addHabit(name: String, timerDuration: Long) {
        viewModelScope.launch {
            dao.insert(Habit(name = name, timerDuration = timerDuration))
        }
    }

    fun startHabitTimer(habit: Habit) {
        viewModelScope.launch {
            val endTime = System.currentTimeMillis() + habit.timerDuration
            dao.update(habit.copy(timerEnd = endTime))
            launch {
                delay(habit.timerDuration)
                dao.update(habit.copy(timerEnd = null, completed = true))
            }
        }
    }
}