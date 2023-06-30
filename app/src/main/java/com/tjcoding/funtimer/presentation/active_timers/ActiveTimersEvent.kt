package com.tjcoding.funtimer.presentation.active_timers

import com.tjcoding.funtimer.domain.model.TimerItem

sealed class ActiveTimersEvent {
    data class onCardLongClick(val timerItem: TimerItem): ActiveTimersEvent()
}