package com.example.habittracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habittracker.screens.HomeScreen
import com.example.habittracker.screens.AddEditScreen
import com.example.habittracker.screens.StatsScreen
import com.example.habittracker.viewmodel.HabitViewModel


@Composable
fun NavGraph(viewModel: HabitViewModel) {
    val navController = rememberNavController()
    NavHost(navController=navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, viewModel) }
        composable("add_edit") { AddEditScreen(navController, viewModel) }
        composable("stats") { StatsScreen(navController, viewModel) }
    }
}