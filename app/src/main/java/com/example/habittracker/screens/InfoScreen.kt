package com.example.habittracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InfoScreen(navController: NavController) {
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
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Center
                ) {
                    Text("Task Streak")
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row() {
                    Text(
                        text = """          
                Think of this app as your organizer & pomodoro timer for everything you have to do this week. 
                
                Click + to add a task, the amount of sessions, and how long each session should be. 
                
                When you're ready to get to work, click the play button to start the timer.
                
                Once the timer is done, your session count increments automatically so you can see your progress all week.
                
                Session counts reset every Monday at 4AM, so hit your weekly goals before then! 
              
                """.trimIndent(),
                        modifier = Modifier.padding(4.dp),
                    )
                }
            }
        }
    }
}