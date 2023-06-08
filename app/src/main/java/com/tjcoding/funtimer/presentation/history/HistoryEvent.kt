package com.tjcoding.funtimer.presentation.history

import com.tjcoding.funtimer.domain.model.TimerItem

sealed class HistoryEvent {
    data class onCardLongClick(val timerItem: TimerItem): HistoryEvent()
    object loadTimerItems: HistoryEvent()
}