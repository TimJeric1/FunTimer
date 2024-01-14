package com.tjcoding.funtimer.presentation.past_timers

import com.tjcoding.funtimer.domain.model.TimerItem
import java.time.LocalDateTime

data class PastTimersState(
    var pastTimerItems: List<PastTimerItem> = emptyList()
)


data class PastTimerItem (
    val selectedNumbers: List<Int>,
    val triggerTime: LocalDateTime
)

fun TimerItem.toPastTimerItem(): PastTimerItem {
    return PastTimerItem(
        selectedNumbers = this.selectedNumbers,
        triggerTime = this.alarmTime.plusMinutes(this.extraTime.toLong())
    )
}
