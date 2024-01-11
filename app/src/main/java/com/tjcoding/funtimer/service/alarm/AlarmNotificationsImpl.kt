package com.tjcoding.funtimer.service.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tjcoding.funtimer.R
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.model.toMessage
import com.tjcoding.funtimer.service.alarm.AlarmNotifications.Companion.CHANNEL_ID
import com.tjcoding.funtimer.service.alarm.AlarmNotifications.Companion.getAlarmNotificationId
import com.tjcoding.funtimer.service.alarm.AlarmNotifications.Companion.getReminderNotificationId
import com.tjcoding.funtimer.service.alarm.AlarmService.Companion.DISMISS_ALARM_ACTION
import com.tjcoding.funtimer.service.alarm.AlarmService.Companion.MUTE_ALARM_ACTION
import javax.inject.Inject


class AlarmNotificationsImpl @Inject constructor() : AlarmNotifications {

    override fun showAlarmNotification(service: Service, timerItem: TimerItem) {

        val context = service.baseContext

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_pool_24)
            .setContentTitle("Funtimer Alarm")
            .setContentText("End of playtime for numbers ${timerItem.toMessage()}")
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            .setWhen(0)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)


        // Setup Dismiss Action
        val dismissIntent = Intent(context, AlarmService::class.java)
        dismissIntent.action = DISMISS_ALARM_ACTION
        val dismissPendingIntent = PendingIntent.getService(
            context,
            101, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notification.addAction(
            R.drawable.baseline_alarm_off_24,
            "dismiss",
            dismissPendingIntent
        )


        // Setup Mute Action
        val muteIntent = Intent(context, AlarmService::class.java)
        muteIntent.action = MUTE_ALARM_ACTION
        val mutePendingIntent = PendingIntent.getService(
            context,
            102, muteIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notification.addAction(
            R.drawable.baseline_volume_mute_24,
            "mute",
            mutePendingIntent
        )

        // Setup Content Action
        val contentIntent = Intent(context, AlarmActivity::class.java)
        contentIntent.putExtra("TIMER_ITEM", timerItem)
        notification.setContentIntent(
            PendingIntent.getActivity(
                context,
                103, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )


        val fullScreenIntent = Intent(context, AlarmActivity::class.java)
        fullScreenIntent.putExtra("TIMER_ITEM", timerItem)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 1,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notification.setFullScreenIntent(fullScreenPendingIntent, true)

        clearNotification(context, getAlarmNotificationId(timerItem))
        service.startForeground(getAlarmNotificationId(timerItem), notification.build())


    }

    private fun clearNotification(context: Context, id: Int) {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(id)
    }

    override fun showMissedTimerItemNotification(context: Context, timerItem: TimerItem) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_pool_24)
            .setContentTitle("Missed FunTimer alarm")
            .setContentText("End of playtime for numbers ${timerItem.toMessage()}")
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()


        notificationManager.notify(getReminderNotificationId(timerItem), notification)

    }

    override fun removeNotification(context: Context, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}