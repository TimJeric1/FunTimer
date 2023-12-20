package com.tjcoding.funtimer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.local.entity.TriggerTimeEntity
import kotlinx.coroutines.flow.Flow



@Dao
abstract class TimerDao {

    @Query(
        "SELECT * FROM SelectedNumberEntity t1" +
                " INNER JOIN TriggerTimeEntity t2" +
                " ON t1.timeItemId = t2.id"
    )
    abstract fun getAllTimerItemsAsMapsStream(): Flow<Map<TriggerTimeEntity, List<SelectedNumberEntity>>>

    @Insert(TriggerTimeEntity::class, onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertTimeEntity(timeEntity: TriggerTimeEntity)

    @Delete(TriggerTimeEntity::class)
    abstract suspend fun deleteTimeEntity(timeEntity: TriggerTimeEntity)

    @Insert(SelectedNumberEntity::class, onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertSelectedNumberEntities(selectedNumberEntities: List<SelectedNumberEntity>)

    @Delete(SelectedNumberEntity::class)
    protected abstract suspend fun deleteSelectedNumberEntities(selectedNumberEntities: List<SelectedNumberEntity>)

    @Transaction
    open suspend fun insertTimerItemAsPair(timerItemPair: Pair<TriggerTimeEntity, List<SelectedNumberEntity>>) {
        insertTimeEntity(timerItemPair.first)
        insertSelectedNumberEntities(timerItemPair.second)
    }

}

