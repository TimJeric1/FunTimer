package com.tjcoding.funtimer.data.repository

import android.util.Log
import com.tjcoding.funtimer.data.local.daos.TimerDao
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.mapper.toEntities
import com.tjcoding.funtimer.data.mapper.toTimerItem
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository

private const val TAG = "TimerRepositoryImpl"
class TimerRepositoryImpl(
    private val timerDao: TimerDao,
): TimerRepository{
    override suspend fun getAllTimerItems(): List<TimerItem> {
        val timerItemEntities = timerDao.getAllTimerEntities()
        val selectedNumberEntities = timerDao.getAllSelectedNumberEntities()

        val timerItems = ArrayList<TimerItem>()
        timerItemEntities.forEach{timerItemEntity ->
            val pairingSelectedNumberEntities = ArrayList<SelectedNumberEntity>()
            Log.d(TAG, "getAllTimerItems: timerItemEntityId = ${timerItemEntity.unixEndTime}")
            selectedNumberEntities.forEach{ selectedNumberEntity ->
                Log.d(TAG, "getAllTimerItems: selectedNumberEntityTimerId = ${selectedNumberEntity.unixEndTime}")
                if(timerItemEntity.unixEndTime == selectedNumberEntity.unixEndTime){
                    pairingSelectedNumberEntities.add(selectedNumberEntity)
                }
            }
            val entitiesPair = Pair(timerItemEntity, pairingSelectedNumberEntities)
            timerItems.add(entitiesPair.toTimerItem())
        }
        Log.d(TAG, "getAllTimerItems filteredTimerItems.size: ${timerItems.size}")

        for(timerItem in timerItems){
            if (timerItem.unixEndTime < System.currentTimeMillis()){
                Log.d(TAG, "getAllTimerItems: deleting")
                deleteTimerItem(timerItem)
            }
        }
        val filteredTimerItems = timerItems.filter {timerItem->
            Log.d(TAG, "getAllTimerItems: filtering")
            timerItem.unixEndTime > System.currentTimeMillis()
        }

        Log.d(TAG, "getAllTimerItems filteredTimerItems.size: ${filteredTimerItems.size}")


        return filteredTimerItems
    }

    override suspend fun insertTimerItem(timerItem: TimerItem) {
        val entities = timerItem.toEntities()
        val timerEntity = entities.first
        val selectedNumberEntities = entities.second
        timerDao.insertTimerEntity(timerEntity)
        selectedNumberEntities.forEach {
            timerDao.insertSelectedNumberEntity(it)
        }
    }

    override suspend fun deleteTimerItem(timerItem: TimerItem) {
        val entities = timerItem.toEntities()
        val timerEntity = entities.first
        val selectedNumberEntities = entities.second
        timerDao.deleteTimerEntity(timerEntity)
        selectedNumberEntities.forEach {
            timerDao.deleteSelectedNumberEntity(it)
        }
    }



}