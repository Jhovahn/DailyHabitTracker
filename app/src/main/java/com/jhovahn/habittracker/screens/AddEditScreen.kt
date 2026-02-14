package com.jhovahn.habittracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jhovahn.habittracker.ui.theme.listItemFontFamily
import com.jhovahn.habittracker.viewmodel.HabitViewModel

@Composable
fun AddEditScreen(navController: NavController, viewModel: HabitViewModel) {

    var habitName by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }
    var seconds by remember { mutableStateOf("") }
    var goalCount by remember { mutableStateOf("") }

    fun getTotalTime(): Int {
        val h = if (hours.isBlank()) 0 else hours.toInt() * 60 * 60 * 1000
        val m = if (minutes.isBlank()) 0 else minutes.toInt() * 1 * 60 * 1000
        val s = if (seconds.isBlank()) 0 else seconds.toInt() * 1000
        return h + m + s
    }

    val totalTime = getTotalTime()

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
                    AppHeader("Task Streak", modifier = Modifier)
                }
            }

            Spacer(modifier = Modifier.height(100.dp))

            TextField(
                value = habitName,
                onValueChange = { habitName = it },
                modifier = Modifier.padding(bottom = 16.dp),
                label = {
                    Text(
                        text = "Enter task here...",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontFamily = listItemFontFamily
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 40.dp)

            ) {
                NumberScroll(
                    onValueChange = { hours = it }, limit = 101, beforeLabel = "H", default = 0
                )
                NumberScroll(
                    onValueChange = { minutes = it }, limit = 60, beforeLabel = "M", default = 25
                )
                NumberScroll(
                    onValueChange = { seconds = it }, limit = 60, beforeLabel = "S", default = 0
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                NumberScroll(
                    onValueChange = { goalCount = it },
                    limit = 1000000,
                    afterLabel = "times per week",
                    default = 5
                )
            }
            val isEnabled = totalTime > 0 && habitName.isNotBlank() && goalCount.toInt() > 0
            Button(
                enabled = isEnabled, onClick = {
                    if (habitName.isNotBlank()) {
                        viewModel.addHabit(
                            name = habitName,
                            timerDuration = totalTime.toLong(),
                            weeklyGoal = (goalCount.toInt())
                        )
                        navController.navigate("home")
                    }
                }, modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Save")
            }
        }
    }
}