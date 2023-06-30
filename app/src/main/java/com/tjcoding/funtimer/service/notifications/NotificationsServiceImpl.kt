package com.tjcoding.funtimer.service.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.tjcoding.funtimer.R

class NotificationsServiceImpl(
    private val context: Context,
): NotificationsService {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    override fun showNotification(message: String) {

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_pool_24)
            .setContentTitle("Kraj zabave")
            .setContentText("Kraj zabave za brojeve $message")
            .build()

        notificationManager.notify(
            ALARM_NOTIFICATION_ID,
            notification
        )
    }

    companion object {
        const val ALARM_NOTIFICATION_ID = 1
        const val CHANNEL_ID = "fun_timer_alarm_channel"
    }

}