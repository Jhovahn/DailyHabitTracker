package com.example.habittracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habittracker.viewmodel.HabitViewModel

@Composable
fun SuccessScreen(navController: NavController, id: Long, viewModel: HabitViewModel) {
    val habits by viewModel.habits.collectAsState()
    val habit = habits.find { it.id.toLong() == id }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val minutes = (habit?.timerDuration?.toInt() ?: 0) / 1000 / 60
                val minutesString = if (minutes > 1) "minutes" else "minute"
                Text(text = "✅ ${habit?.name}")
                Text(text = "⏰ $minutes $minutesString")
                Text(text = "🔥 ${habit?.streak} / 7")
            }

            Button(
                modifier = Modifier.padding(16.dp).height(35.dp),
                onClick = {
                    navController.navigate("home") {}
                },
            ) {
                Text("👈")
            }
        }
    }
}