package com.tjcoding.funtimer.data.repository

import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeTimerRepository: TimerRepository {

    val timerItems = MutableStateFlow<List<TimerItem>>(mutableListOf())
    override fun getAllActiveTimerItemsStream(): Flow<List<TimerItem>> {
        return timerItems.map { timerItems -> timerItems.filter { timerItem -> timerItem.hasTriggered == false } }
    }

    override suspend fun insertTimerItem(timerItem: TimerItem) {
        timerItems.update {
            it.plus(timerItem)
        }
    }

    override suspend fun updateTimerItem(timerItem: TimerItem) {
        if(!timerItems.value.contains(timerItem)) return
        timerItems.update {
            it.minus(timerItem)
            it.plus(timerItem)
        }
    }

    override suspend fun deleteTimerItem(timerItem: TimerItem) {
        timerItems.update {
            it.minus(timerItem)
        }
    }

    override fun getAllTriggeredTimerItemsStream(): Flow<List<TimerItem>> {
        return timerItems.map { timerItems -> timerItems.filter { timerItem -> timerItem.hasTriggered == true } }
    }
}