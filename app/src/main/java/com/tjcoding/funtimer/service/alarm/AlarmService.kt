package com.tjcoding.funtimer.service.alarm

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                val newVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0)
                val oldVolume =
                    intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0)
                if (newVolume < oldVolume) AlarmKlaxon.stop(context)
            }
        }
    }






    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter("android.media.VOLUME_CHANGED_ACTION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)
        else
            registerReceiver(receiver, filter)
    }




    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {




        intent ?: return START_NOT_STICKY
        intent.action ?: return START_NOT_STICKY

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
            stopAlarm()
        }
        currentAlarm = newAlarm
        alarmNotifications.showAlarmNotification(this, timerItem = currentAlarm!!)
        AlarmKlaxon.start(this)
    }
    private fun stopAlarm() {
        if (currentAlarm == null) {
            return
        }
        sendBroadcast(Intent(ALARM_DONE_ACTION))
        stopForeground(STOP_FOREGROUND_REMOVE)
        AlarmKlaxon.stop(this)
        currentAlarm = null
    }


    companion object {
        const val FIRE_ALARM_ACTION = "fire_alarm_action"
        const val DISMISS_ALARM_ACTION = "dismiss_alarm_action"
        const val MUTE_ALARM_ACTION = "mute_alarm_action"
        const val ALARM_DONE_ACTION = "alarm_done_action"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
        unregisterReceiver(receiver)
    }




}


