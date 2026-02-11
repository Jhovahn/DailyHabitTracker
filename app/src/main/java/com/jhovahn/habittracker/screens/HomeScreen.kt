package com.jhovahn.habittracker.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jhovahn.habittracker.viewmodel.HabitViewModel

@RequiresApi(Build.VERSION_CODES.S)
@androidx.annotation.RequiresPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM)
@Composable
fun HomeScreen(navController: NavController, viewModel: HabitViewModel) {
    val habits by viewModel.habits.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .height(35.dp)
                    .width(30.dp),
                onClick = { navController.navigate("add_edit") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }, floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { heading() },
                    horizontalArrangement = Arrangement.Absolute.Center

                ) {
                    Text(
                        text = "Task Streak"
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (habits.isEmpty()) {
                Card(
                    border = BorderStroke(1.5.dp, Color.DarkGray),
                    modifier = Modifier.padding(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = """
                    |Add your task name, session duration and weekly session goal. 
                    |
                    |You will see a list with all yours tasks next to a start timer button.
                    |
                    |Start timer at beginning of each session. 
                    |
                    |App keeps count of each session completed and triggers an alarm at the end.
                    |
                    |Progress resets at 4 AM every Monday.  
                """.trimMargin(), modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                ) {
                    if (habits.isNotEmpty()) {
                        items(habits) { habit ->
                            HabitListItem(
                                habit = habit,
                                modifier = Modifier,
                                navController = navController,
                                viewModel = viewModel
                            )
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
