package com.tjcoding.funtimer.data.repository

import android.util.Log
import com.tjcoding.funtimer.data.local.daos.TimerDao
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.mapper.toEntities
import com.tjcoding.funtimer.data.mapper.toTimerItem
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.utility.alarm.alarm_service.AlarmScheduler
import java.time.LocalDateTime

private const val TAG = "TimerRepositoryImpl"
class TimerRepositoryImpl(
    private val timerDao: TimerDao,
    private val alarmScheduler: AlarmScheduler
): TimerRepository{
    override suspend fun getAllTimerItems(): List<TimerItem> {
        val timerItemEntities = timerDao.getAllTimerEntities()
        val selectedNumberEntities = timerDao.getAllSelectedNumberEntities()

        val timerItems = ArrayList<TimerItem>()
        timerItemEntities.forEach{timerItemEntity ->
            Log.d(TAG, "getAllTimerItems: timerItemEntity ${timerItemEntity.toString()}")
            val pairingSelectedNumberEntities = ArrayList<SelectedNumberEntity>()
            selectedNumberEntities.forEach{ selectedNumberEntity ->
                if(timerItemEntity.key == selectedNumberEntity.timerItemKey){
                    pairingSelectedNumberEntities.add(selectedNumberEntity)
                }
            }
            val entitiesPair = Pair(timerItemEntity, pairingSelectedNumberEntities)
            val timerItem = entitiesPair.toTimerItem()
            if(timerItem.time.isAfter(LocalDateTime.now().minusSeconds(2))) timerItems.add(timerItem)
            else deleteTimerItem(timerItem)
        }

        return timerItems
    }

    override suspend fun insertTimerItem(timerItem: TimerItem) {
        Log.d(TAG, "insertTimerItem: ${timerItem.toString()}")
        alarmScheduler.schedule(timerItem)
        val entities = timerItem.toEntities()
        val timerEntity = entities.first
        val selectedNumberEntities = entities.second
        timerDao.insertTimerEntity(timerEntity)
        Log.d(TAG, "insertTimerItem: timerEntity ${timerEntity.toString()}")
        selectedNumberEntities.forEach {
            timerDao.insertSelectedNumberEntity(it)
        }
    }

    override suspend fun deleteTimerItem(timerItem: TimerItem) {
        Log.d(TAG, "deleteTimerItem: ${timerItem.toString()}")
        alarmScheduler.cancel(timerItem)
        val entities = timerItem.toEntities()
        val timerEntity = entities.first
        val selectedNumberEntities = entities.second

        Log.d(TAG, "deleteTimerItem: timerEntity: ${timerEntity.toString()}")
        timerDao.deleteTimerEntity(timerEntity)
        selectedNumberEntities.forEach {
            timerDao.deleteSelectedNumberEntity(it)
        }
    }



}