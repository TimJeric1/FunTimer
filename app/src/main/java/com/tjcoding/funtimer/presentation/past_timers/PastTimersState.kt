package com.tjcoding.funtimer.presentation.past_timers

import com.tjcoding.funtimer.domain.model.TimerItem
import java.time.LocalDateTime

data class PastTimersState(
    var pastTimerItemsUi: List<PastTimerItemUi> = emptyList()
)


data class PastTimerItemUi (
    val selectedNumbers: List<Int>,
    val triggerTime: LocalDateTime
)

fun TimerItem.toPastTimerItemUi(): PastTimerItemUi {
    return PastTimerItemUi(
        selectedNumbers = this.selectedNumbers,
        triggerTime = this.alarmTime.plusMinutes(this.extraTime.toLong())
    )
}
