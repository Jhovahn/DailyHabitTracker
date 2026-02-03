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
    val weeklyProgress = if(isActive) habit.weeklyCompleted - 1 else habit.weeklyCompleted
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Text(
                "Duration: ${(habit.timerDuration) / (60 * 1000) } minutes\nWeekly target: ${habit.weeklyGoal}\nProgress: ${weeklyProgress}/${habit.weeklyGoal}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )
        }
    }
}