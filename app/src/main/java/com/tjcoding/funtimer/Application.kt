package com.tjcoding.funtimer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.tjcoding.funtimer.service.alarm.AlarmNotificationsManager
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val alarmChannel = NotificationChannel(
            AlarmNotificationsManager.CHANNEL_ID,
            "Alarm notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        alarmChannel.description = "Used for sending alarm notifications"

        val errorChannel = NotificationChannel(
            AlarmNotificationsManager.CHANNEL_ID,
            "Error notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        errorChannel.description = "Used for sending error notifications"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(alarmChannel)
        notificationManager.createNotificationChannel(errorChannel)
    }

}