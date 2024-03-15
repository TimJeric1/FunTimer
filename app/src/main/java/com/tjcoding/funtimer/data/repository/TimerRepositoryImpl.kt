package com.tjcoding.funtimer.data.repository

import com.tjcoding.funtimer.data.local.dao.TimerDao
import com.tjcoding.funtimer.data.mapper.toEntitiesPair
import com.tjcoding.funtimer.data.mapper.toTimerItem
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.utility.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.util.UUID


class TimerRepositoryImpl(
    private val timerDao: TimerDao,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : TimerRepository {


    override fun getAllTriggeredTimerItemsStream(): Flow<List<TimerItem>> {
        // no need for flowOn(Dispatchers.IO) because room automatically does that.
        // flowOn(Dispatchers.Default) is used for cpu intensive tasks
        // and it applies the Dispatcher for all operations that come before it (in this case the .map function and .getAllTimerItemsMap function).
        return timerDao.getAllTriggeredTimerItemsAsMapsStream()
            .retryWhen { cause, attempt -> Util.shouldRetry(cause, attempt) }
            .map { timerItemMap -> timerItemMap.map { it.toPair().toTimerItem() } }
            .flowOn(defaultDispatcher)
    }
    
    override fun getAllActiveTimerItemsStream(): Flow<List<TimerItem>> {
        // no need for flowOn(Dispatchers.IO) because room automatically does that.
        // flowOn(Dispatchers.Default) is used for cpu intensive tasks
        // and it applies the Dispatcher for all operations that come before it (in this case the .map function and .getAllTimerItemsMap function).
        return timerDao.getAllNotTriggeredTimerItemsAsMapsStream()
            .retryWhen { cause, attempt -> Util.shouldRetry(cause, attempt) }
            .map { timerItemMap -> timerItemMap.map { it.toPair().toTimerItem() } }
            .flowOn(defaultDispatcher)
    }

    override suspend fun insertTimerItem(timerItem: TimerItem) {
        // no need for withContext(Dispatcher.io) because room automatically does that
        timerDao.insertTimerItemAsPair(timerItem.toEntitiesPair())
    }

    override suspend fun updateTimerItem(originalTimerItem: TimerItem, newTimerItem: TimerItem) {
        // no need for withContext(Dispatcher.io) because room automatically does that
        timerDao.updateTimerItemAsPair(
            originalItemPair = originalTimerItem.toEntitiesPair(),
            newTimerItemPair = newTimerItem.toEntitiesPair()
        )
    }

    override suspend fun deleteTimerItem(timerItem: TimerItem) {
        val timeEntity = timerItem.toEntitiesPair().first
        // on delete cascading foreign key will also delete the appropriate selectedTimeEntities
        timerDao.deleteTimeEntity(timeEntity)
    }

    override suspend fun deleteAll() {
        timerDao.deleteAll()
    }

    override suspend fun getTimerItemById(timerItemId: UUID): TimerItem? {
        val timerItemPair = timerDao.getTimerItemByIdAsPair(timerItemId)
        val timerItem = timerItemPair?.toTimerItem()
        return timerItem
    }





}