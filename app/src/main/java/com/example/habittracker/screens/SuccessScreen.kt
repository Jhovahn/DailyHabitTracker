package com.example.habittracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
    val habit = habits.find { it.id.toLong() == id}
    Scaffold { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Success",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
            Text(text = "${habit?.name} complete!" )
            Text(text = "Duration: ${(habit?.timerDuration?.toInt() ?: 0) / 1000 / 60} minutes")

            Button(
                onClick = {
                    navController.navigate("home") {
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Home")
            }
        }
    }
}