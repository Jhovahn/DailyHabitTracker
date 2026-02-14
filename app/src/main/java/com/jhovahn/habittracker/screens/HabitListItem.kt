package com.jhovahn.habittracker.screens

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.jhovahn.habittracker.alarm.AlarmService
import com.jhovahn.habittracker.alarm.AlarmState
import com.jhovahn.habittracker.alarm.cancelAlarm
import com.jhovahn.habittracker.data.Habit
import com.jhovahn.habittracker.ui.theme.listItemFontFamily
import com.jhovahn.habittracker.viewmodel.HabitViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HabitListItem(
    habit: Habit,
    modifier: Modifier,
    viewModel: HabitViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val isActive = habit.timerEnd != null
    val isDark = isSystemInDarkTheme()
    val darkerGreen = Color(0xFF6EE787)
    val lighterGreen = Color(0xFF34D399)
    val red = Color(0xFFE53935)
    val activeAlarmId by viewModel.activeAlarmId.collectAsState()
    val itemTriggeredAlarm = activeAlarmId.toInt() == habit.id
    val accentColor = if (isActive) if (isDark) darkerGreen else lighterGreen
    else if (AlarmState.isRinging(context) && itemTriggeredAlarm) red else MaterialTheme.colorScheme.outlineVariant
    var expanded by remember { mutableStateOf(false) }
    val serviceIntent = Intent(context, AlarmService::class.java)

    Card(
        border = BorderStroke(1.5.dp, accentColor),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (itemTriggeredAlarm) {
                    context.stopService(serviceIntent)
                } else {
                    expanded = !expanded
                }
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            var showConfirmDialog by remember { mutableStateOf(false) }
            val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = { value ->
                if (value == SwipeToDismissBoxValue.EndToStart) {
                    showConfirmDialog = true
                    viewModel.deleteHabit(habit)
                    navController.navigate("home")
                    context.stopService(serviceIntent)
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
                    val isConfirmed = dismissState.targetValue != SwipeToDismissBoxValue.Settled
                    val isReturning = dismissState.targetValue == SwipeToDismissBoxValue.Settled
                    val backgroundColor = when {
                        isConfirmed -> Color.Red.copy(alpha = 0.4f)
                        isReturning -> Color.Transparent
                        dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart -> {
                            lerp(
                                Color.Transparent, Color.Red.copy(alpha = 0.4f), progress
                            )
                        }

                        else -> Color.Transparent
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                backgroundColor,
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
                            tint = Color.White.copy(alpha = iconAlpha),
                            modifier = Modifier.absolutePadding(right = 16.dp)
                        )
                    }
                },
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val timerRunning =
                            habit.timerEnd != null && habit.timerEnd > System.currentTimeMillis()
                        val chevron = if (expanded) "\u25B2" else "\u25BC"
                        Text(
                            text = "${habit.name} $chevron",
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 16.dp, bottom = 16.dp),
                            fontFamily = listItemFontFamily,
                        )
                        if (!timerRunning) {
                            if (habit.weeklyCompleted < habit.weeklyGoal) {
                                Text("${habit.weeklyCompleted}/${habit.weeklyGoal}")
                            } else {
                                Text("🔥 ${habit.weeklyCompleted}")
                            }
                        }
                        if (itemTriggeredAlarm) {
                            IconButton(onClick = { context.stopService(serviceIntent) }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Alarm")
                            }
                        }
                        if (timerRunning) {
                            var remaining by remember {
                                mutableLongStateOf(
                                    habit.timerEnd.minus(
                                        System.currentTimeMillis()
                                    )
                                )
                            }
                            LaunchedEffect(habit.timerEnd) {
                                while (remaining > 0) {
                                    delay(1000)
                                    remaining =
                                        habit.timerEnd.minus(System.currentTimeMillis())
                                }
                            }
                            Text(formatMillis(remaining), color = accentColor)
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
                        } else if (!itemTriggeredAlarm) {
                            IconButton(
                                onClick = {
                                    val canScheduleExact = alarmManager.canScheduleExactAlarms()
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
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Start Timer",
                                )
                            }
                        }
                    }
                })
            if (dismissState.targetValue === SwipeToDismissBoxValue.EndToStart) expanded = false
            AnimatedVisibility(
                visible = expanded
            ) {
                HabitDetails(habit)
            }
        }
    }
}