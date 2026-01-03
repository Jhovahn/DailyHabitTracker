package com.example.habittracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habittracker.screens.HomeScreen
import com.example.habittracker.screens.AddEditScreen
import com.example.habittracker.screens.StatsScreen


@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController=navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("add_edit") { AddEditScreen(navController) }
        composable("stats") { StatsScreen(navController) }
    }
}