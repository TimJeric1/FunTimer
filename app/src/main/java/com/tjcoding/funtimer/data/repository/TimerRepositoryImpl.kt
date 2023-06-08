package com.tjcoding.funtimer.data.repository

import com.tjcoding.funtimer.data.local.daos.SelectedNumberDao
import com.tjcoding.funtimer.data.local.daos.TimerDao
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.mapper.toEntities
import com.tjcoding.funtimer.data.mapper.toTimerItem
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository

class TimerRepositoryImpl(
    private val timerDao: TimerDao,
    private val selectedNumberDao: SelectedNumberDao
): TimerRepository{
    override suspend fun getAllTimerItems(): List<TimerItem> {
        val timerItemEntities = timerDao.getAll()
        val selectedNumberEntities = selectedNumberDao.getAll()

        val timerItems = ArrayList<TimerItem>()
        timerItemEntities.forEach{timerItemEntity ->
            val pairingSelectedNumberEntities = ArrayList<SelectedNumberEntity>()
            selectedNumberEntities.forEach{ selectedNumberEntity ->
                if(timerItemEntity.timerId == selectedNumberEntity.timerId){
                    pairingSelectedNumberEntities.add(selectedNumberEntity)
                }
            }
            val entitiesPair = Pair(timerItemEntity, pairingSelectedNumberEntities)
            timerItems.add(entitiesPair.toTimerItem())
        }
        return timerItems
    }

    override suspend fun insertTimerItem(timerItem: TimerItem) {
        val entities = timerItem.toEntities()
        timerDao.insert(entities.first)
        entities.second.forEach {
            selectedNumberDao.insert(it)
        }
    }

    override suspend fun deleteTimerItem(timerItem: TimerItem) {
        val entities = timerItem.toEntities()
        timerDao.delete(entities.first)
        entities.second.forEach {
            selectedNumberDao.delete(it)
        }
    }



}