package com.jhovahn.habittracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.jhovahn.habittracker.data.Habit

@Composable
fun HabitDetails(habit: Habit) {
    val isActive = habit.timerEnd != null
    val weeklyProgress = if (isActive) habit.weeklyCompleted - 1 else habit.weeklyCompleted
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                "Duration: ${formatTime(habit.timerDuration)}\nWeekly target: ${habit.weeklyGoal}\nProgress: ${weeklyProgress}/${habit.weeklyGoal}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

fun formatTime(ms: Long): String {
    val hours = ms / (60 * 60 * 1000)
    val minutes = ms % (60 * 60 * 1000) / 60000
    val seconds = (ms % 60000) / 1000

    val hourString = if (hours == 0L) "" else if (hours == 1L) "1 hour" else "$hours hours"
    val minuteString =
        if (minutes == 0L) "" else if (minutes == 1L) "1 minute" else "$minutes minutes"
    val secondString =
        if (seconds == 0L) "" else if (seconds == 1L) "1 second" else "$seconds seconds"
    val hourSpace = if (hourString.isNotEmpty()) " " else ""
    val minuteSpace = if (minuteString.isNotEmpty()) " " else ""

    return "$hourString$hourSpace$minuteString$minuteSpace$secondString"
}