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


class TimerRepositoryImpl(
    private val timerDao: TimerDao,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : TimerRepository {
    override fun getAllTimerItemsStream(): Flow<List<TimerItem>> {
        // no need for flowOn(Dispatchers.IO) because room automatically does that.
        // flowOn(Dispatchers.Default) is used for cpu intensive tasks
        // and it applies the Dispatcher for all operations that come before it (in this case the .map function and .getAllTimerItemsMap function).
        return timerDao.getAllTimerItemsAsMapsStream()
            .retryWhen { cause, attempt -> Util.shouldRetry(cause, attempt) }
            .map { timerItemMap -> timerItemMap.map { it.toPair().toTimerItem() } }
            .flowOn(defaultDispatcher)
    }

    override suspend fun insertTimerItem(timerItem: TimerItem) {
        // no need for withContext(Dispatcher.io) because room automatically does that
        timerDao.insertTimerItemAsPair(timerItem.toEntitiesPair())
    }

    override suspend fun deleteTimerItem(timerItem: TimerItem) {
        // no need for withContext(Dispatcher.io) because room automatically does that
        val timeEntity = timerItem.toEntitiesPair().first
        timerDao.deleteTimeEntity(timeEntity)
    }


}