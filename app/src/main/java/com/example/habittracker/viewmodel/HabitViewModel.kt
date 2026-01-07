package com.example.habittracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.HabitDao
import com.example.habittracker.data.Habit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn


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

    fun toggleCompleted(habit: Habit) {
        viewModelScope.launch {
            dao.update(habit.copy(completed = !habit.completed))
        }
    }

    fun addHabit(name: String) {
        viewModelScope.launch {
            dao.insert(Habit(name = name))
        }
    }
}