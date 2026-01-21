package com.example.habittracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.habittracker.viewmodel.HabitViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AddEditScreen(navController: NavController, viewModel: HabitViewModel) {

    var habitName by remember { mutableStateOf("") }
    var habitTimerDuration by remember { mutableStateOf("25") }
    var weeklyGoal by remember { mutableStateOf("5") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .padding(bottom = 200.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = habitName,
                onValueChange = { habitName = it },
                modifier = Modifier.padding(bottom = 16.dp),
                label = { Text("Enter habit here...") },
            )

            TextField(
                value = habitTimerDuration,
                onValueChange = { newValue ->
                    habitTimerDuration = newValue.filter { it.isDigit() }
                },
                label = { Text("Session length in minutes") },
                modifier = Modifier.padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            TextField(
                value = weeklyGoal,
                onValueChange = { newValue ->
                    weeklyGoal = newValue.filter { it.isDigit() }
                },
                label = { Text("Weekly session goal count") },
                modifier = Modifier.padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Button(
                enabled = habitTimerDuration.isNotBlank() && habitName.isNotBlank(), onClick = {
                    if (habitName.isNotBlank()) {
                        viewModel.addHabit(
                            name = habitName,
                            timerDuration = (habitTimerDuration.toLong()) * 60 * 1000,
                            weeklyGoal = (weeklyGoal.toInt())
                        )
                        navController.popBackStack()
                    }
                    if (habitTimerDuration.toFloat() < 0) {
                        habitTimerDuration = (habitTimerDuration.toLong() * 60 * 1000).toString()
                    }
                }, modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Save")
            }
        }
    }
}