package com.duyle.image_loader_hw8

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat

class MainActivity : ComponentActivity() {
    private lateinit var networkReceiver: BroadcastReceiver
    private val viewModel: ImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Start notification service
        startService(Intent(this, ImageService::class.java))

        // Register BroadcastReceiver to listen for network changes
        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // Kiểm tra trạng thái kết nối mạng
                val connected = NetworkUtils.isNetworkAvailable(context)

                // Cập nhật trạng thái mạng trong ViewModel
                viewModel.setNetworkStatus(connected)

                // Hiển thị Toast thông báo trạng thái mạng
                Toast.makeText(context, if (connected) "Connected" else "Disconnected", Toast.LENGTH_SHORT).show()

                // Hiển thị thông báo Notification dựa trên trạng thái mạng
                val channelId = "network_status"
                val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(channelId, "Network Status", NotificationManager.IMPORTANCE_DEFAULT)
                    manager.createNotificationChannel(channel)
                }

                // Thông báo cho cả kết nối và ngắt kết nối
                val notification = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(android.R.drawable.stat_notify_error)
                    .setContentTitle("Network Status")
                    .setContentText(if (connected) "Connected to Internet" else "No Internet Connection")
                    .build()

                // Hiển thị thông báo
                manager.notify(2, notification)
            }
        }


        // Register the receiver to listen for network changes
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        setContent {
            ImageLoaderApp(viewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver) // Unregister the receiver to avoid memory leaks
    }
}
