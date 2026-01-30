package com.jhovahn.habittracker.alarm


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.jhovahn.habittracker.MainActivity
import com.jhovahn.habittracker.data.Habit
import com.jhovahn.habittracker.data.HabitDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HabitAlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = HabitDatabase.getDatabase(context)
                val dao = db.habitDao()
                val action = intent.action

                val habitId = intent.getLongExtra("habitId", -1L)
                if (habitId != -1L) {
                    dao.getById(habitId)?.let { habit ->
                        dao.update(habit.copy(completed = true, timerEnd = null))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            showNotification(context, habit)
                        }
                    }
                }

                if (action == Intent.ACTION_BOOT_COMPLETED || action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) {
                    val habits = dao.getAll().first()
                    habits.forEach { habit ->
                        if (habit.timerEnd != null && habit.timerEnd > System.currentTimeMillis()) {
                            rescheduleAlarm(context, habit)
                        }
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}

@SuppressLint("ScheduleExactAlarm")
fun rescheduleAlarm(context: Context, habit: Habit) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, HabitAlarmReceiver::class.java).apply {
        putExtra("habitId", habit.id.toLong())
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, habit.id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    habit.timerEnd?.let { endTime ->
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, endTime, pendingIntent
        )
    }
}

fun cancelAlarm(context: Context, habit: Habit) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, HabitAlarmReceiver::class.java).apply {
        putExtra("habitId", habit.id.toLong())
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, habit.id, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    )
    if (pendingIntent != null) {
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
private fun showNotification(context: Context, habit: Habit) {
    val channelId = "habit_channel"
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
    val fullScreenIntent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtra("habitId", habit.id.toLong())
    }
    val fullScreenPendingIntent = PendingIntent.getActivity(
        context,
        habit.id,
        fullScreenIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    if (!notificationManager.canUseFullScreenIntent()) {
        val settingsIntent = Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(settingsIntent)
    }
    val channel = android.app.NotificationChannel(
        channelId, "Habit Notifications", android.app.NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Channel for habit timer completions"
        enableVibration(true)
    }
    notificationManager.createNotificationChannel(channel)
    val notification = NotificationCompat.Builder(context, "habit_channel")
        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setContentTitle("Way to go! 🔥🔥🔥")
        .setContentText("${habit.name} session complete!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setFullScreenIntent(fullScreenPendingIntent, true)
        .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI).setAutoCancel(true).setOngoing(false)
        .setFullScreenIntent(fullScreenPendingIntent, true)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).build()
    notificationManager.notify(SystemClock.uptimeMillis().toInt(), notification)

    val serviceIntent = Intent(context, AlarmService::class.java)
    ContextCompat.startForegroundService(context, serviceIntent)
}