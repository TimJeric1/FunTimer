package com.tjcoding.funtimer.presentation.active_timers


sealed interface ActiveTimersEvent {
    data class OnXIconClick(val activeTimerItem: ActiveTimerItem): ActiveTimersEvent
    data class OnEditIconClick(val activeTimerItem: ActiveTimerItem): ActiveTimersEvent

    data class OnAlertDialogDeleteClick(val activeTimerItem: ActiveTimerItem): ActiveTimersEvent
}