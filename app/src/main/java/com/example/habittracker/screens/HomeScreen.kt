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
                            Checkbox(
                                checked = habit.completed,
                                onCheckedChange = { viewModel.toggleCompleted(habit) }
                            )
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

