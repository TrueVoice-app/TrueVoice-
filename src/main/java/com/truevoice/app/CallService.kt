package com.truevoice.app

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import androidx.core.app.NotificationCompat

class CallService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val number = intent?.getStringExtra("number") ?: "Unknown"

        startForeground(1, createNotification())

        val overlayIntent = Intent(this, OverlayService::class.java)
        overlayIntent.putExtra("number", number)
        startService(overlayIntent)

        return START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        val channelId = "call_service_channel"

        val channel = NotificationChannel(
            channelId,
            "Call Detection",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("TrueVoice Running")
            .setContentText("Monitoring calls...")
            .setSmallIcon(android.R.drawable.sym_call_incoming)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}