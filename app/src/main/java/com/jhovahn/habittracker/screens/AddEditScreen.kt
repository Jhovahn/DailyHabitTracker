package com.jhovahn.habittracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jhovahn.habittracker.viewmodel.HabitViewModel

@Composable
fun AddEditScreen(navController: NavController, viewModel: HabitViewModel) {

    var habitName by remember { mutableStateOf("") }
    var habitTimerDuration by remember { mutableStateOf("") }
    var weeklyGoal by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Center
                ) {
                    Text(text = "Task Streak")
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(
                        onClick = { navController.navigate("home") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Home, contentDescription = "Home Button")
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))

            TextField(
                value = habitName,
                onValueChange = { habitName = it },
                modifier = Modifier.padding(bottom = 16.dp),
                label = {
                    Text(
                        text = "Enter task here...", color = Color.Gray, fontSize = 14.sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            TextField(
                value = habitTimerDuration, onValueChange = { newValue ->
                habitTimerDuration = newValue.filter { it.isDigit() }
            }, label = {
                Text(
                    text = "Session length in minutes...", color = Color.Gray, fontSize = 14.sp
                )
            }, modifier = Modifier.padding(bottom = 16.dp), keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ), colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
            )
            TextField(
                value = weeklyGoal, onValueChange = { newValue ->
                weeklyGoal = newValue.filter { it.isDigit() }
            }, label = {
                Text(
                    text = "Weekly session goal...", color = Color.Gray, fontSize = 14.sp
                )
            }, modifier = Modifier.padding(bottom = 16.dp), keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ), colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
            )
            val isEnabled =
                habitTimerDuration.isNotBlank() && habitName.isNotBlank() && weeklyGoal.isNotBlank()
            Button(
                enabled = isEnabled, onClick = {
                    if (habitName.isNotBlank()) {
                        viewModel.addHabit(
                            name = habitName,
                            timerDuration = (habitTimerDuration.toLong()) * 60 * 1000,
                            weeklyGoal = (weeklyGoal.toInt())
                        )
                        navController.navigate("home")
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