package com.tjcoding.funtimer.presentation.active_timers


sealed interface ActiveTimersEvent {
    data class OnXClick(val activeTimerItem: ActiveTimerItem): ActiveTimersEvent
    data class OnEditClick(val activeTimerItem: ActiveTimerItem): ActiveTimersEvent

    data class OnAlertDialogDeleteClick(val activeTimerItem: ActiveTimerItem): ActiveTimersEvent
}