package com.tjcoding.funtimer.service.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tjcoding.funtimer.domain.model.TimerItem
import java.time.ZoneId
import javax.inject.Inject


class AlarmSchedulerImpl @Inject constructor(
    private val context: Context,
    private val isInDebugMode: Boolean
) : AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)





    override fun schedule(timerItem: TimerItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("TIMER_ITEM", timerItem)
        }

        val triggerAtMillis = if(isInDebugMode) timerItem.getAlarmTriggerTimeDebug().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
        else timerItem.getAlarmTriggerTime().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            PendingIntent.getBroadcast(
                context,
                timerItem.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(timerItem: TimerItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                timerItem.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }


}