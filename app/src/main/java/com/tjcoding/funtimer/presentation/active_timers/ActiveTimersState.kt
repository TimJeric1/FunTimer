package com.tjcoding.funtimer.presentation.active_timers

import com.tjcoding.funtimer.domain.model.TimerItem
import java.time.LocalDateTime

data class ActiveTimersState(
    var activeTimerItems: List<ActiveTimerItem> = emptyList()
)

data class ActiveTimerItem(
    val selectedNumbers: List<Int>,
    val triggerTime: LocalDateTime,
    val alarmTime: Int,
    val extraTime: Int,
)

fun TimerItem.toActiveTimerItem(): ActiveTimerItem {
    return ActiveTimerItem(
        selectedNumbers = this.selectedNumbers,
        triggerTime = this.triggerTime,
        alarmTime = this.alarmTime,
        extraTime = this.extraTime,
    )
}

fun ActiveTimerItem.toTimerItem(): TimerItem {
    return TimerItem(
        selectedNumbers = this.selectedNumbers,
        triggerTime = this.triggerTime,
        alarmTime = this.alarmTime,
        extraTime = this.extraTime,
        hasTriggered = false
    )
}