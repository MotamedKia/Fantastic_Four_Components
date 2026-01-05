package com.example.fantasticfourcomponents

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyForegroundService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundNotif()
        if (intent?.action == "STOP_SERVICE") {
            stopForeground(true)
            stopSelf()
            return START_NOT_STICKY
        }
        GlobalScope.launch {
            while (true) {
                logger("MyforegroundService says: Playing.. tick 🎵")
                delay(3000)
            }
        }
        return START_STICKY
    }

    private fun startForegroundNotif() {
        val channelId = "foreground notification"
        val notificationManager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "foreground notification", NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val stopIntent =
            Intent(this, MyForegroundService::class.java).apply { action = "STOP_SERVICE" }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification: Notification =
            NotificationCompat.Builder(this, channelId)
                .setContentTitle("Foreground Service")
                .setContentText("Foreground Service is working")
                .addAction(android.R.drawable.ic_delete, "Stop", stopPendingIntent)
                .setSmallIcon(android.R.drawable.ic_media_play).setOngoing(true).build()

        startForeground(1, notification)
    }

    override fun onBind(p0: Intent?): IBinder? = null
}