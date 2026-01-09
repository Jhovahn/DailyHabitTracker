package com.example.habittracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.habittracker.viewmodel.HabitViewModel
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController, viewModel: HabitViewModel) {
    val habits by viewModel.habits.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(

                onClick = { navController.navigate("add_edit")}
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }, floatingActionButtonPosition = FabPosition.End)
            { innerPadding ->

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            if (habits.isEmpty()) {
                Text(
                    text = "Not habits yet. Tap + to add one!"
                )
            } else {
                LazyColumn {
                    items(habits) { habit ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text=habit.name, modifier=Modifier.weight(1f))

                            if(habit.completed) {
                                val emojiUnicode = "\uD83D\uDD25"
                                Text("$emojiUnicode ${habit.streak}")
                            } else if(habit.timerEnd != null && habit.timerEnd > System.currentTimeMillis()) {
                                var remaining by remember { mutableLongStateOf( habit.timerEnd - System.currentTimeMillis()) }
                                LaunchedEffect(habit.timerEnd) {
                                    while(remaining > 0) {
                                        delay(1000)
                                        remaining = habit.timerEnd - System.currentTimeMillis()
                                    }
                                }
                                Text("${formatMillis(remaining)} remaining")
                            } else {
                                Button(onClick = { viewModel.startHabitTimer(habit)}) {
                                    Text("Start")
                                }
                            }
                            IconButton(
                                onClick = { viewModel.deleteHabit(habit) }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatMillis(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
