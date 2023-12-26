package com.tjcoding.funtimer.presentation.active_timers

import com.tjcoding.funtimer.domain.model.TimerItem
import java.time.LocalDateTime

data class ActiveTimersState(
    var activeTimerItemsUi: List<ActiveTimerItemUi> = emptyList()
)

data class ActiveTimerItemUi(
    val selectedNumbers: List<Int>,
    val alarmTime: LocalDateTime,
    val extraTime: Int,
)

fun TimerItem.toActiveTimerItemUi(): ActiveTimerItemUi {
    return ActiveTimerItemUi(
        selectedNumbers = this.selectedNumbers,
        alarmTime = this.alarmTime,
        extraTime = this.extraTime,
    )
}

fun ActiveTimerItemUi.toTimerItem(): TimerItem {
    return TimerItem(
        selectedNumbers = this.selectedNumbers,
        alarmTime = this.alarmTime,
        extraTime = this.extraTime,
        hasTriggered = false
    )
}