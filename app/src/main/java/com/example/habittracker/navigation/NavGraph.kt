package com.example.habittracker.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habittracker.screens.HomeScreen
import com.example.habittracker.screens.AddEditScreen
import com.example.habittracker.screens.InfoScreen
import com.example.habittracker.screens.StatsScreen
import com.example.habittracker.viewmodel.HabitViewModel
import com.example.habittracker.screens.SuccessScreen

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("ScheduleExactAlarm")
@androidx.annotation.RequiresPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM)
@Composable
fun NavGraph(viewModel: HabitViewModel, habitId: Long) {
    val navController = rememberNavController()
    LaunchedEffect(habitId) {
        if(habitId != -1L) {
            navController.navigate("success/${habitId}") {
                launchSingleTop = true
                popUpTo("home") { inclusive = false}
            }
        }
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, viewModel) }
        composable("add_edit") { AddEditScreen(navController, viewModel) }
        composable("stats") { StatsScreen(navController, viewModel) }
        composable("info") { InfoScreen(navController) }
        composable("success/${habitId}") { backstackEntry ->
            val id = backstackEntry.arguments?.getString("habitId")?.toLong() ?: -1L
            SuccessScreen(navController, habitId, viewModel)
        }
    }
}