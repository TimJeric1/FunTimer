package com.tjcoding.funtimer.service.alarm

import com.tjcoding.funtimer.domain.model.TimerItem

interface AlarmScheduler {
    fun scheduleOrUpdateAlarm(timerItem: TimerItem)
    fun cancelAlarm(timerItem: TimerItem)
}