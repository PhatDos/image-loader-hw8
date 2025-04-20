package com.duyle.image_loader_hw8

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.app.Service
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.duyle.image_loader_hw8.MainActivity

class ImageService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            showNotification()
            handler.postDelayed(this, 1 * 60 * 1000) // 5 minutes interval
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(runnable)
        return START_STICKY // Ensures the service is restarted if killed
    }

    private fun showNotification() {
        val channelId = "image_loader_channel"
        val notificationManager = getSystemService(NotificationManager::class.java)

        // Create notification channel (Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Image Loader Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Image Loader Service")
            .setContentText("Image Loader Service is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

        // Show the notification
        notificationManager.notify(1, notification)
    }

    override fun onDestroy() {
        // Clean up when the service is destroyed
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        // Not binding, so return null
        return null
    }
}
