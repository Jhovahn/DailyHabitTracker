package com.example.habittracker.screens

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.habittracker.viewmodel.HabitViewModel
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.S)
@androidx.annotation.RequiresPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM)
@Composable
fun HomeScreen(navController: NavController, viewModel: HabitViewModel) {
    val habits by viewModel.habits.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_edit") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }, floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            if (habits.isEmpty()) {
                Text(
                    text = "Not habits yet. Tap + to add one!", color = Color.Red
                )
            } else {
                LazyColumn {
                    if (habits.isNotEmpty()) {
                        items(habits) { habit ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val timerRunning =
                                    habit.timerEnd != null && habit.timerEnd > System.currentTimeMillis()
                                val context = LocalContext.current

                                Text(text = habit.name, modifier = Modifier.weight(1f))
                                val emojiUnicode = "\uD83D\uDD25"
                                val goal = 7
                                if (!timerRunning) {
                                    if (habit.streak < goal) {
                                        Text("${habit.streak} / $goal")
                                    } else {
                                        Text("$emojiUnicode ${habit.streak}")
                                    }
                                }
                                if (timerRunning) {
                                    var remaining by remember {
                                        mutableLongStateOf(
                                            habit.timerEnd?.minus(
                                                System.currentTimeMillis()
                                            ) ?: 0
                                        )
                                    }
                                    LaunchedEffect(habit.timerEnd) {
                                        while (remaining > 0) {
                                            delay(1000)
                                            remaining =
                                                habit.timerEnd?.minus(System.currentTimeMillis())
                                                    ?: 0
                                        }
                                    }
                                    Text(formatMillis(remaining))
                                }
                                if (timerRunning) {
                                    IconButton(
                                        onClick = { viewModel.resetHabitTimer(habit) }) {
                                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                                    }
                                } else {
                                    IconButton(
                                        onClick = {
                                            val alarmManager =
                                                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                            val canScheduleExact =
                                                alarmManager.canScheduleExactAlarms()
                                            if (!canScheduleExact) {
                                                val intent =
                                                    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                                        data = Uri.fromParts(
                                                            "package", context.packageName, null
                                                        )
                                                    }
                                                context.startActivity(intent)
                                            }
                                            viewModel.startHabitTimer(context, habit)
                                        }, modifier = Modifier.size(36.dp)
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Start Timer"
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { viewModel.deleteHabit(habit) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
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
