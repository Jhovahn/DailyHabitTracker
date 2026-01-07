package com.example.habittracker.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.habittracker.viewmodel.HabitViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun StatsScreen(navController: NavController, viewModel: HabitViewModel) {
    val habits by viewModel.habits.collectAsState()

    val total = habits.size
    val completed = habits.count { it.completed }
    val progress = if (total > 0) completed.toFloat() / total else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Total habits: $total")
        Text(text = "Completed habits: $completed")

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth()
                .height(8.dp),
        )
    }
}