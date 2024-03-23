package com.tjcoding.funtimer.data.dao

import com.tjcoding.funtimer.data.local.dao.TimerDao
import com.tjcoding.funtimer.data.local.entity.AlarmTriggerTimeEntity
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID

class FakeTimerDao : TimerDao() {

    val fakeDatabase: MutableStateFlow<List<Pair<AlarmTriggerTimeEntity, List<SelectedNumberEntity>>>> =
        MutableStateFlow(emptyList())

    override fun getAllNotTriggeredTimerItemsAsMapsStream(): Flow<Map<AlarmTriggerTimeEntity, List<SelectedNumberEntity>>> {
        return fakeDatabase.map { db ->
            db.filter { !it.first.hasTriggered }.associate { it.first to it.second }
        }
    }

    override fun getAllTriggeredTimerItemsAsMapsStream(): Flow<Map<AlarmTriggerTimeEntity, List<SelectedNumberEntity>>> {
        return fakeDatabase.map { db ->
            db.filter { it.first.hasTriggered }.associate { it.first to it.second }
        }
    }

    override suspend fun deleteAll() {
        fakeDatabase.value = emptyList()
    }

    override suspend fun deleteTimeEntity(timeEntity: AlarmTriggerTimeEntity) {
        fakeDatabase.value = fakeDatabase.value.filter { it.first.id != timeEntity.id }
    }

    override suspend fun getTimeEntityById(id: UUID): AlarmTriggerTimeEntity? {
        return fakeDatabase.value.find { it.first.id == id }?.first
    }

    override suspend fun getSelectedNumberEntitiesByTimeItemId(id: UUID): List<SelectedNumberEntity> {
        return fakeDatabase.value.find { it.first.id == id }?.second ?: emptyList()
    }

    override suspend fun updateTimeEntity(timeEntity: AlarmTriggerTimeEntity) {
        fakeDatabase.value = fakeDatabase.value.map {
            if (it.first.id == timeEntity.id) timeEntity to it.second else it
        }
    }
    override suspend fun insertTimeEntity(timeEntity: AlarmTriggerTimeEntity) {
        fakeDatabase.value += timeEntity to emptyList()
    }

    override suspend fun updateSelectedNumberEntities(selectedNumberEntities: List<SelectedNumberEntity>) {
        fakeDatabase.value = fakeDatabase.value.map { pair ->
            val updatedNumbers = selectedNumberEntities.filter { it.alarmTriggerTimeEntityId == pair.first.id }
            pair.first to updatedNumbers
        }
    }

    override suspend fun insertSelectedNumberEntities(selectedNumberEntities: List<SelectedNumberEntity>) {
        fakeDatabase.value = fakeDatabase.value.map { pair ->
            val newNumbers = selectedNumberEntities.filter { it.alarmTriggerTimeEntityId == pair.first.id }
            pair.first to (pair.second + newNumbers)
        }
    }

    override suspend fun deleteSelectedNumberEntities(selectedNumberEntities: List<SelectedNumberEntity>) {
        fakeDatabase.value = fakeDatabase.value.map { pair ->
            val remainingNumbers = pair.second.filterNot { selectedNumberEntities.contains(it) }
            pair.first to remainingNumbers
        }
    }
}
