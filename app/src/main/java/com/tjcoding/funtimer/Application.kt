package com.tjcoding.funtimer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.tjcoding.funtimer.service.alarm.AlarmService
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            AlarmService.CHANNEL_ID,
            "Alarm notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "Used for sending alarm notifications"
        channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
        channel.enableVibration(true)
        channel.enableLights(true)


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


    }

}