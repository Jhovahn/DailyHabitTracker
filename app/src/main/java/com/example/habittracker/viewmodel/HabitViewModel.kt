package com.example.habittracker.viewmodel

import com.example.habittracker.alarm.HabitAlarmReceiver
import android.Manifest
import android.content.Intent
import android.app.PendingIntent
import android.app.AlarmManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.HabitDao
import com.example.habittracker.data.Habit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.delay
import androidx.core.content.getSystemService


class HabitViewModel(private val dao: HabitDao) : ViewModel() {
    val habits = dao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            dao.delete(habit)
        }
    }

    fun addHabit(name: String, timerDuration: Long) {
        viewModelScope.launch {
            dao.insert(Habit(name = name, timerDuration = timerDuration))
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    fun startHabitTimer(context: Context, habit: Habit) {
        val endTime = System.currentTimeMillis() + habit.timerDuration

        viewModelScope.launch {
            dao.update(habit.copy(timerEnd = endTime, completed = false, streak = habit.streak + 1))
        }

        scheduleAlarm(context, habit.id.toLong(), endTime)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    fun scheduleAlarm(context: Context, habitId: Long, endTime: Long) {

        val alarmManager = context.getSystemService<AlarmManager>()

        val alarmIntent = Intent(context, HabitAlarmReceiver::class.java).apply {
            putExtra("habitId", habitId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habitId.toInt(),
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (alarmManager?.canScheduleExactAlarms() == false) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        } else {
            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, endTime, pendingIntent
            )
        }
    }

    fun resetHabitTimer(habit: Habit) {
        viewModelScope.launch {
            dao.update(habit.copy(timerEnd = null, completed = true))
        }
    }
}