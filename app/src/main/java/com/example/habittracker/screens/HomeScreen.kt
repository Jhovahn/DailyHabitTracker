package com.example.habittracker.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Text("Home Screen - habits list will appear here")
}

@Composable
fun AddEditScreen(navController: NavController) {
    Text("Add/Edit Habit Screen")
}

@Composable
fun StatsScreen(navController: NavController) {
    Text("Stats Screen")
}
