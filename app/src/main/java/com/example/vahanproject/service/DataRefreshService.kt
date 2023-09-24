package com.example.vahanproject.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vahanproject.MainViewModel
import com.example.vahanproject.R
import com.example.vahanproject.repository.UniversityRepository
import com.example.vahanproject.util.ConnectivityObserver
import com.example.vahanproject.util.MyApplication
import kotlinx.coroutines.*

class DataRefreshService : LifecycleService() {

    private val scope = CoroutineScope(Dispatchers.IO)
    lateinit var viewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        val app = application as MyApplication
        viewModel = app.getSharedViewModel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Start fetching data every 10 seconds
        val notification = createNotification()
        startForeground(FOREGROUND_SERVICE_ID, notification)

        scope.launch {
            while (true) {
                viewModel.getUniversity()
                Log.d("TAG", "Running Foreground Service...")
                // Sleep for 10 seconds
                delay(10_000)
            }
        }

        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "API Refresh Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("API Refresh Service")
            .setContentText("Refreshing API every 10 seconds")
            .setSmallIcon(R.drawable.ic_baseline_notification) // Change this to your app's icon
            .build()

    }

    companion object {
        private const val FOREGROUND_SERVICE_ID = 101
        private const val CHANNEL_ID = "DataRefreshChannel"
    }
}