package com.tjcoding.funtimer.service.alarm

import com.tjcoding.funtimer.domain.model.TimerItem

class FakeAlarmScheduler : AlarmScheduler {

    private val scheduledItems: MutableList<TimerItem> = mutableListOf()
    private val canceledItems: MutableList<TimerItem> = mutableListOf()

    override fun scheduleOrUpdateAlarm(timerItem: TimerItem) {
        scheduledItems.add(timerItem)
    }

    override fun cancelAlarm(timerItem: TimerItem) {
        canceledItems.add(timerItem)
    }
}
