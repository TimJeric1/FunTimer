package com.tjcoding.funtimer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.local.entity.AlarmTriggerTimeEntity
import kotlinx.coroutines.flow.Flow



@Dao
abstract class TimerDao {

    @Query(
        "SELECT * FROM SelectedNumberEntity t1" +
                " INNER JOIN AlarmTriggerTimeEntity t2" +
                " ON t1.timeItemId = t2.id" +
                " WHERE t2.hasTriggered = 0"
    )
    abstract fun getAllNotTriggeredTimerItemsAsMapsStream(): Flow<Map<AlarmTriggerTimeEntity, List<SelectedNumberEntity>>>

    @Query(
        "SELECT * FROM SelectedNumberEntity t1" +
                " INNER JOIN AlarmTriggerTimeEntity t2" +
                " ON t1.timeItemId = t2.id" +
                " WHERE t2.hasTriggered = 1"
    )
    abstract fun getAllTriggeredTimerItemsAsMapsStream(): Flow<Map<AlarmTriggerTimeEntity, List<SelectedNumberEntity>>>


    // Because of foreign key in other table it will also delete all from SelectedNumberEntity
    @Query("DELETE FROM AlarmTriggerTimeEntity")
    abstract suspend fun deleteAll()


    @Delete
    abstract suspend fun deleteTimeEntity(timeEntity: AlarmTriggerTimeEntity)

    @Transaction
    open suspend fun insertTimerItemAsPair(timerItemPair: Pair<AlarmTriggerTimeEntity, List<SelectedNumberEntity>>) {
        insertTimeEntity(timerItemPair.first)
        insertSelectedNumberEntities(timerItemPair.second)
    }
    @Transaction
    open suspend fun updateTimerItemAsPair(timerItemPair: Pair<AlarmTriggerTimeEntity, List<SelectedNumberEntity>>) {
        updateTimeEntity(timerItemPair.first)
        updateSelectedNumberEntities(timerItemPair.second)
    }


    @Update(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun updateSelectedNumberEntities(selectedNumberEntities: List<SelectedNumberEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertSelectedNumberEntities(selectedNumberEntities: List<SelectedNumberEntity>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun updateTimeEntity(timeEntity: AlarmTriggerTimeEntity)


    @Delete(SelectedNumberEntity::class)
    protected abstract suspend fun deleteSelectedNumberEntities(selectedNumberEntities: List<SelectedNumberEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertTimeEntity(timeEntity: AlarmTriggerTimeEntity)



}

