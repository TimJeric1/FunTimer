package com.tjcoding.funtimer.presentation.active_timers

import com.tjcoding.funtimer.domain.model.TimerItem

sealed interface ActiveTimersEvent {
    data class OnCardLongClick(val timerItem: TimerItem): ActiveTimersEvent
}