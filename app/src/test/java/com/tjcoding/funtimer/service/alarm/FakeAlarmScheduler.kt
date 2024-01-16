package com.tjcoding.funtimer.service.alarm

import com.tjcoding.funtimer.domain.model.TimerItem

class FakeAlarmScheduler : AlarmScheduler {

    private val scheduledItems: MutableList<TimerItem> = mutableListOf()
    private val canceledItems: MutableList<TimerItem> = mutableListOf()

    override fun schedule(timerItem: TimerItem) {
        scheduledItems.add(timerItem)
    }

    override fun cancel(timerItem: TimerItem) {
        canceledItems.add(timerItem)
    }
}
