package com.tjcoding.funtimer.data.repository

import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

class FakeTimerRepository: TimerRepository {

    val timerItems = MutableStateFlow<List<TimerItem>>(emptyList())
    override fun getAllActiveTimerItemsStream(): Flow<List<TimerItem>> {
        return timerItems.map { timerItems -> timerItems.filter { timerItem -> !timerItem.hasTriggered } }
    }

    override suspend fun insertTimerItem(timerItem: TimerItem) {
        timerItems.update {
            it.plus(timerItem)
        }
    }

    override suspend fun updateTimerItem(originalTimerItem: TimerItem, newTimerItem: TimerItem) {
        if(!timerItems.value.contains(originalTimerItem)) return
        timerItems.update {
            it.minus(originalTimerItem).plus(newTimerItem)
        }
    }


    override suspend fun deleteTimerItem(timerItem: TimerItem) {
        timerItems.update {
            it.minus(timerItem)
        }
    }

    override fun getAllTriggeredTimerItemsStream(): Flow<List<TimerItem>> {
        return timerItems.map { timerItems -> timerItems.filter { timerItem -> timerItem.hasTriggered } }
    }

    override suspend fun deleteAll() {
        timerItems.update {
            emptyList()
        }
    }

    override suspend fun getTimerItemById(timerItemId: UUID): TimerItem? {
        return timerItems.value.find { it.id == timerItemId }
    }
}