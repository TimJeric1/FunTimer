package com.tjcoding.funtimer.domain.repository

import com.tjcoding.funtimer.domain.model.TimerItem

interface TimerRepository {

    suspend fun getAllTimerItems(): List<TimerItem>
    suspend fun insertTimerItem(timerItem: TimerItem)
    suspend fun deleteTimerItem(timerItem: TimerItem)
}