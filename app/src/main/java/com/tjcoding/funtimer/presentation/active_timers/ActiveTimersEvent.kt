package com.tjcoding.funtimer.presentation.active_timers


sealed interface ActiveTimersEvent {
    data class OnCardLongClick(val activeTimerItem: ActiveTimerItem): ActiveTimersEvent
}