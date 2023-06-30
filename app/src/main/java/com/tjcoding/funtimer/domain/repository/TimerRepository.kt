package com.tjcoding.funtimer.domain.repository

import com.tjcoding.funtimer.domain.model.TimerItem
import kotlinx.coroutines.flow.Flow

interface TimerRepository {

    fun getAllTimerItemsStream(): Flow<List<TimerItem>>
    suspend fun insertTimerItem(timerItem: TimerItem)
    suspend fun deleteTimerItem(timerItem: TimerItem)
}