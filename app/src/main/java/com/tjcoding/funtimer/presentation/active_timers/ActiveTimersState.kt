package com.tjcoding.funtimer.presentation.active_timers

import com.tjcoding.funtimer.domain.model.TimerItem
import java.time.LocalDateTime
import java.util.UUID

data class ActiveTimersState(
    var activeTimerItems: List<ActiveTimerItem> = emptyList()
)

data class ActiveTimerItem(
    val id: UUID,
    val selectedNumbers: List<Int>,
    val triggerTime: LocalDateTime,
    val alarmTime: Int,
    val extraTime: Int,
)

fun TimerItem.toActiveTimerItem(): ActiveTimerItem {
    return ActiveTimerItem(
        id = id,
        selectedNumbers = this.selectedNumbers,
        triggerTime = this.triggerTime,
        alarmTime = this.alarmTime,
        extraTime = this.extraTime,
    )
}

fun ActiveTimerItem.toTimerItem(): TimerItem {
    return TimerItem(
        id = id,
        selectedNumbers = this.selectedNumbers,
        triggerTime = this.triggerTime,
        alarmTime = this.alarmTime,
        extraTime = this.extraTime,
        hasTriggered = false
    )
}