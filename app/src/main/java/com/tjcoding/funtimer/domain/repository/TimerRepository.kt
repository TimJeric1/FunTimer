package com.tjcoding.funtimer.domain.repository

import com.tjcoding.funtimer.domain.model.TimerItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TimerRepository {

    fun getAllActiveTimerItemsStream(): Flow<List<TimerItem>>
    suspend fun insertTimerItem(timerItem: TimerItem)

    suspend fun updateTimerItem(originalTimerItem: TimerItem, newTimerItem: TimerItem)

    suspend fun deleteTimerItem(timerItem: TimerItem)

    fun getAllTriggeredTimerItemsStream(): Flow<List<TimerItem>>

    suspend fun deleteAll()
    suspend fun getTimerItemById(timerItemId: UUID): TimerItem?
}