package com.jhovahn.habittracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jhovahn.habittracker.data.HabitDao

class HabitViewModelFactory(private val dao: HabitDao): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}