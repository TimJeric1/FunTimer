package com.tjcoding.funtimer.service.alarm

import android.app.Service
import com.tjcoding.funtimer.domain.model.TimerItem

interface AlarmNotifications {
    fun showAlarmNotification(service: Service, timerItem: TimerItem)
}