package com.jhovahn.habittracker.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jhovahn.habittracker.alarm.AlarmService
import com.jhovahn.habittracker.viewmodel.HabitViewModel

@Composable
fun SuccessScreen(navController: NavController, id: Long, viewModel: HabitViewModel) {
    val habits by viewModel.habits.collectAsState()
    val habit = habits.find { it.id.toLong() == id }
    val context = LocalContext.current
    val serviceIntent = Intent(context, AlarmService::class.java)
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),

                ) {
                val minutes = (habit?.timerDuration?.toInt() ?: 0) / 1000 / 60
                val minutesString = if (minutes > 1) "mins" else "min"
                Text(text = "✅ ${habit?.name}")
                Text(text = "⏰ $minutes $minutesString")
                Text(text = "🔥 ${habit?.weeklyCompleted} / ${habit?.weeklyGoal}")
            }

            IconButton(
                modifier = Modifier
                    .size(15.dp)
                    .offset(y = 32.dp),
                onClick = {
                    navController.navigate("home") {}
                    context.stopService(serviceIntent)
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }
    }
}