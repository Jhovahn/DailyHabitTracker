package com.example.habittracker.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.habittracker.data.HabitDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeeklyResetReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = HabitDatabase.getDatabase(context).habitDao()

            val habits = dao.getAllOnce()
            val newWeekStart = getNextMonday4AM()

            habits.forEach { habit ->
                dao.update(
                    habit.copy(
                        weeklyCompleted = 0, weekStartTimestamp = newWeekStart
                    )
                )
            }
            scheduleWeeklyReset(context)
        }
    }
}