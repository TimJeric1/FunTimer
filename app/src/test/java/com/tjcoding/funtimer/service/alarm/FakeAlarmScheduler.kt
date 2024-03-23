package com.tjcoding.funtimer.service.alarm

import com.tjcoding.funtimer.domain.model.TimerItem

class FakeAlarmScheduler : AlarmScheduler {

    val scheduledItems: MutableList<TimerItem> = mutableListOf()
    val canceledItems: MutableList<TimerItem> = mutableListOf()

    override fun scheduleOrUpdateAlarm(timerItem: TimerItem) {
        val oldTimerItem = scheduledItems.find { timerItem.id == it.id }
        scheduledItems.remove(oldTimerItem)
        scheduledItems.add(timerItem)
    }

    override fun cancelAlarm(timerItem: TimerItem) {
        val oldTimerItem = scheduledItems.find { timerItem.id == it.id }
        scheduledItems.remove(oldTimerItem)
        canceledItems.add(timerItem)
    }
}
