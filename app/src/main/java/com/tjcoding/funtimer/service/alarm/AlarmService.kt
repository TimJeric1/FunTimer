package com.tjcoding.funtimer.service.alarm

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.tjcoding.funtimer.domain.model.TimerItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService: Service()
{


    @Inject lateinit var alarmNotifications: AlarmNotifications

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        if(intent?.action == null) return START_NOT_STICKY

        if(FIRE_ALARM_ACTION == intent.action) { val timerItem = intent.getParcelableExtra("TIMER_ITEM", TimerItem::class.java) ?: return START_NOT_STICKY
            alarmNotifications.showAlarmNotification(this, timerItem = timerItem)
            AlarmKlaxon.start(this)
        }
        else if(DISMISS_ALARM_ACTION == intent.action) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            AlarmKlaxon.stop(this)
        }

        return START_STICKY

    }




    companion object {
        var ALARM_NOTIFICATION_ID = 1
            get() = field++
            private set
        const val CHANNEL_ID = "fun_timer_alarm_channel"
        const val FIRE_ALARM_ACTION = "fire_alarm_action"
        const val DISMISS_ALARM_ACTION = "dismiss_alarm_action"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


}