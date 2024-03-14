package com.tjcoding.funtimer.service.alarm

import android.app.Service
import android.content.Context
import com.tjcoding.funtimer.domain.model.TimerItem

interface AlarmNotifications {
    fun showAlarmNotification(service: Service, timerItem: TimerItem)

    fun showMissedTimerItemNotification(context: Context, timerItem: TimerItem)


    fun removeNotification(context: Context ,notificationId: Int)

    companion object {
        const val CHANNEL_ID = "fun_timer_alarm_channel"

        fun getReminderNotificationId(timerItem: TimerItem): Int {
            return timerItem.id.hashCode()
        }
        fun getAlarmNotificationId(timerItem: TimerItem): Int {
            return timerItem.id.hashCode()*100
        }
    }
}