package com.tjcoding.funtimer.service.alarm

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.tjcoding.funtimer.domain.model.TimerItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : Service() {


    @Inject
    lateinit var alarmNotifications: AlarmNotifications
    private var currentAlarm: TimerItem? = null





    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {




        if (intent?.action == null) return START_NOT_STICKY

        val newAlarm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("TIMER_ITEM", TimerItem::class.java)
        } else {
            intent.getParcelableExtra("TIMER_ITEM")
        }

        when (intent.action) {
            FIRE_ALARM_ACTION -> {
                newAlarm ?: return START_NOT_STICKY
                startAlarm(newAlarm)
            }

            DISMISS_ALARM_ACTION -> {
                newAlarm ?: return START_NOT_STICKY
                stopAlarm()
            }

            DISMISS_AND_ADD_NOTIFICATION_ACTION -> {
                newAlarm ?: return START_NOT_STICKY
                alarmNotifications.showMissedTimerItemNotification(this, newAlarm)
                stopAlarm()
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

    private fun startAlarm(newAlarm: TimerItem) {
        if(currentAlarm != null) {
            alarmNotifications.showMissedTimerItemNotification(this, currentAlarm!!)
        }
        currentAlarm = newAlarm
        alarmNotifications.showAlarmNotification(this, timerItem = currentAlarm!!)
        AlarmKlaxon.start(this)
    }
    private fun stopAlarm() {
        AlarmKlaxon.stop(this)
        stopForeground(STOP_FOREGROUND_REMOVE)
        currentAlarm = null
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


