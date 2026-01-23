package com.example.habittracker.screens

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habittracker.alarm.cancelAlarm
import com.example.habittracker.viewmodel.HabitViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.S)
@androidx.annotation.RequiresPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM)
@Composable
fun HomeScreen(navController: NavController, viewModel: HabitViewModel) {
    val habits by viewModel.habits.collectAsState()
    val context = LocalContext.current
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

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
                            var showConfirmDialog by remember { mutableStateOf(false) }
                            val dismissState =
                                rememberSwipeToDismissBoxState(confirmValueChange = { value ->
                                    if (value == SwipeToDismissBoxValue.EndToStart) {
                                        showConfirmDialog = true
                                        viewModel.deleteHabit(habit)
                                        navController.navigate("home")
                                        true
                                    } else {
                                        false
                                    }
                                }, positionalThreshold = { totalDistance -> totalDistance * 0.5F })
                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromEndToStart = true,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = {
                                    val progress = dismissState.progress
                                    val isConfirmed =
                                        dismissState.targetValue != SwipeToDismissBoxValue.Settled
                                    val isReturning =
                                        dismissState.targetValue == SwipeToDismissBoxValue.Settled
                                    val backgroundColor = when {
                                        isConfirmed -> Color.Red.copy(alpha = 0.4f)
                                        isReturning -> Color.Transparent
                                        dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart -> {
                                            lerp(
                                                Color.Transparent,
                                                Color.Red.copy(alpha = 0.4f),
                                                progress
                                            )
                                        }

                                        else -> Color.Transparent
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                backgroundColor, shape = RoundedCornerShape(16.dp)
                                            )
                                            .padding(horizontal = 20.dp)
                                            .clip(
                                                RoundedCornerShape(16.dp)
                                            ),
                                        contentAlignment = Alignment.CenterEnd,

                                        ) {
                                        val iconAlpha = if (isReturning) 0f else 1.0f
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Dismiss",
                                            tint = Color.White.copy(alpha = iconAlpha)
                                        )
                                    }
                                },
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        val timerRunning =
                                            habit.timerEnd != null && habit.timerEnd > System.currentTimeMillis()
//                                        val context = LocalContext.current

                                        Text(text = habit.name, modifier = Modifier.weight(1f))
                                        if (!timerRunning) {
                                            if (habit.weeklyCompleted < habit.weeklyGoal) {
                                                Text("${habit.weeklyCompleted}/${habit.weeklyGoal}")
                                            } else {
                                                Text("🔥 ${habit.weeklyCompleted}")
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
                                                onClick = {
                                                    cancelAlarm(context, habit)
                                                    viewModel.resetHabitTimer(habit)
                                                }) {
                                                Icon(
                                                    Icons.Default.Refresh,
                                                    contentDescription = "Refresh",
                                                )
                                            }
                                        } else {
                                            IconButton(
                                                onClick = {
                                                    val canScheduleExact =
                                                        alarmManager.canScheduleExactAlarms()
                                                    if (!canScheduleExact) {
                                                        val intent =
                                                            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                                                data = Uri.fromParts(
                                                                    "package",
                                                                    context.packageName,
                                                                    null
                                                                )
                                                            }
                                                        context.startActivity(intent)
                                                    }
                                                    viewModel.startHabitTimer(context, habit)
                                                },
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.PlayArrow,
                                                    contentDescription = "Start Timer"
                                                )
                                            }
                                        }
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
}

fun formatMillis(ms: Long): String {
    val totalSeconds = ms / 1000
    var minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val hours = minutes / 60
    if (hours > 0) minutes %= 60
    return if (hours > 0) "%d:%02d:%02d".format(hours, minutes, seconds) else "%02d:%02d".format(
        minutes, seconds
    )
}
