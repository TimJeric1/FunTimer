package com.tjcoding.funtimer.domain.repository

import com.tjcoding.funtimer.domain.model.TimerItem
import kotlinx.coroutines.flow.Flow

interface TimerRepository {

    fun getAllActiveTimerItemsStream(): Flow<List<TimerItem>>
    suspend fun insertTimerItem(timerItem: TimerItem)

    suspend fun updateTimerItem(timerItem: TimerItem)

    suspend fun deleteTimerItem(timerItem: TimerItem)

    fun getAllTriggeredTimerItemsStream(): Flow<List<TimerItem>>

    suspend fun deleteAll()
}