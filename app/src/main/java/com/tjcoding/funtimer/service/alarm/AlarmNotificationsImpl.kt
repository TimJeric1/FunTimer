package com.tjcoding.funtimer.service.alarm

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import androidx.core.app.NotificationCompat
import com.tjcoding.funtimer.R
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.model.toMessage
import com.tjcoding.funtimer.service.alarm.AlarmService.Companion.DISMISS_ALARM_ACTION
import javax.inject.Inject

class AlarmNotificationsImpl @Inject constructor() : AlarmNotifications {
    override fun showAlarmNotification(service: Service, timerItem: TimerItem) {

        val context = service.baseContext

        val notification = NotificationCompat.Builder(context, AlarmService.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_pool_24)
            .setContentTitle("Kraj zabave")
            .setContentText("Kraj zabave za brojeve ${timerItem.toMessage()}")
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            .setWhen(0)
            .setLocalOnly(true)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)



        // Setup Dismiss Action
        val dismissIntent = Intent(context, AlarmService::class.java)
        dismissIntent.action = DISMISS_ALARM_ACTION
        val dismissPendingIntent = PendingIntent.getService(
            context,
            101, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT+ PendingIntent.FLAG_IMMUTABLE
        )
        notification.addAction(
            R.drawable.baseline_alarm_off_24,
            "dismiss",
            dismissPendingIntent
        )

        // Setup Content Action
        val contentIntent = Intent(context, AlarmActivity::class.java)
        contentIntent.putExtra("TIMER_ITEM", timerItem)
        notification.setContentIntent(
            PendingIntent.getActivity(
                context,
                102, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
            )
        )


        val fullScreenIntent = Intent(context, AlarmActivity::class.java)
        fullScreenIntent.putExtra("TIMER_ITEM", timerItem)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 1,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )
        notification.setFullScreenIntent(fullScreenPendingIntent, true)


        service.startForeground(
            AlarmService.ALARM_NOTIFICATION_ID, notification.build(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED
        )
    }
}