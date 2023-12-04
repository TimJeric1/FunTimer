package com.tjcoding.funtimer.service.alarm

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.service.alarm.AlarmNotifications.Companion.getAlarmNotificationId
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService: Service()
{


    @Inject lateinit var alarmNotifications: AlarmNotifications

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        if(intent?.action == null) return START_NOT_STICKY

        val timerItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("TIMER_ITEM", TimerItem::class.java)
        } else {
            intent.getParcelableExtra("TIMER_ITEM")
        }

        when(intent.action) {
            FIRE_ALARM_ACTION -> {
                timerItem ?: return START_NOT_STICKY
                alarmNotifications.showAlarmNotification(this, timerItem = timerItem)
                AlarmKlaxon.start(this)
            }
            DISMISS_ALARM_ACTION -> {
                timerItem ?: return START_NOT_STICKY
                stopForeground(STOP_FOREGROUND_DETACH)
                AlarmKlaxon.stop(this)
                alarmNotifications.removeNotification(this, getAlarmNotificationId(timerItem))
            }
            DISMISS_AND_ADD_NOTIFICATION_ACTION -> {
                timerItem ?: return START_NOT_STICKY
                alarmNotifications.showPastTimerItemNotification(this, timerItem)
                AlarmKlaxon.stop(this)
                stopForeground(STOP_FOREGROUND_DETACH)
            }
            MUTE_ALARM_ACTION -> {
                AlarmKlaxon.stop(this)
            }
            else -> {
                return START_NOT_STICKY
            }
        }

        return START_STICKY

    }




    companion object {
        const val FIRE_ALARM_ACTION = "fire_alarm_action"
        const val DISMISS_ALARM_ACTION = "dismiss_alarm_action"
        const val DISMISS_AND_ADD_NOTIFICATION_ACTION = "dismiss_and_add_notification_alarm_action"
        const val MUTE_ALARM_ACTION = "mute_alarm_action"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


}