package com.tjcoding.funtimer.service.alarm

import com.tjcoding.funtimer.domain.model.TimerItem

interface AlarmScheduler {
    fun schedule(timerItem: TimerItem)
    fun cancel(timerItem: TimerItem)
}