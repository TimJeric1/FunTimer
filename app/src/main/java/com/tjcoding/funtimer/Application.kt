package com.tjcoding.funtimer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import com.tjcoding.funtimer.service.notifications.NotificationsServiceImpl
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NotificationsServiceImpl.CHANNEL_ID,
            "Counter",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "Used for sending alarm notifications"
        channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
        channel.enableVibration(true)
        channel.enableLights(true)

//        val sound =
//            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.packageName + "/" + R.raw.custom_alarm)

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
//        channel.setSound(sound, attributes)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


    }

}