package com.example.habittracker.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.habittracker.viewmodel.HabitViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun AddEditScreen(navController: NavController, viewModel: HabitViewModel) {

    var habitName by remember { mutableStateOf("")  }

    Scaffold { innerPadding ->


        Column(modifier = Modifier.padding(16.dp).padding(innerPadding)) {
            TextField(
                value = habitName,
                onValueChange = { habitName = it },
                label = { Text("Habit Name") },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    if (habitName.isNotBlank()) {
                        viewModel.addHabit(habitName)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Save")
            }
        }
    }
}