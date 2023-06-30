package com.tjcoding.funtimer.presentation.active_timers

import com.tjcoding.funtimer.domain.model.TimerItem

data class ActiveTimersState(
    var timerItems: List<TimerItem> = emptyList()
)