package com.example.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.habittracker.data.HabitDatabase
import com.example.habittracker.navigation.NavGraph
import com.example.habittracker.ui.theme.HabitTrackerTheme
import com.example.habittracker.viewmodel.HabitViewModel
import com.example.habittracker.viewmodel.HabitViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = HabitDatabase.getDatabase(applicationContext)
        val dao = db.habitDao()
        val viewModelFactory = HabitViewModelFactory(dao)
        setContent {
            HabitTrackerTheme {
                val viewModel: HabitViewModel = viewModel(factory = viewModelFactory)
                NavGraph(viewModel)
            }
        }
    }

}