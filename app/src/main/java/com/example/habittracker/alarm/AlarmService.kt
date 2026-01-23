package com.example.habittracker.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.habittracker.R
import androidx.media.app.NotificationCompat as MediaNotificationCompat

class AlarmService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = "habit_alarm_service_channel"
        if (intent?.action === "STOP_ALARM") {
            stopSelf()
            return START_NOT_STICKY
        } else {
            val dismissIntent = Intent(this, AlarmService::class.java).apply {
                action = "STOP_ALARM"
            }
            val name = "Alarm Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val descriptionText = "Plays sound"
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), null)
                enableVibration(true)
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            val pendingDismiss =
                PendingIntent.getService(this, 0, dismissIntent, PendingIntent.FLAG_IMMUTABLE)

            val notification =
                NotificationCompat.Builder(this, channelId).setContentTitle("Timer Finished")
                    .setContentText("Alarming...")
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .addAction(R.drawable.ic_stop, "Stop", pendingDismiss)
                    .setStyle(MediaNotificationCompat.MediaStyle().setShowActionsInCompactView(0))
                    .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(99, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
            } else {
                startForeground(99, notification)
            }
            startAlarm()
            return START_STICKY
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startAlarm() {
        mediaPlayer =
            MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .apply {
                    isLooping = true
                    start()
                }
        vibrator = (getSystemService(VIBRATOR_SERVICE) as Vibrator).apply {
            vibrate(VibrationEffect.createWaveform(longArrayOf(0, 500, 500), 0))
        }
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        vibrator?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}