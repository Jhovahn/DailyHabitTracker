package com.jhovahn.habittracker.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Calendar


fun getNextMonday4AM(): Long {
    val cal = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        set(Calendar.HOUR_OF_DAY, 4)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    if (cal.timeInMillis <= System.currentTimeMillis()) {
        cal.add(Calendar.WEEK_OF_YEAR, 1)
    }
    return cal.timeInMillis
}

@RequiresApi(Build.VERSION_CODES.S)
fun scheduleWeeklyReset(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, WeeklyResetReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    if (alarmManager.canScheduleExactAlarms()) {

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, getNextMonday4AM(), pendingIntent
        )
    }
}