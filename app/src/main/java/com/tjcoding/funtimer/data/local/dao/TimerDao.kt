package com.tjcoding.funtimer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.local.entity.TimeEntity
import kotlinx.coroutines.flow.Flow



@Dao
abstract class TimerDao {

    @Query(
        "SELECT * FROM SelectedNumberEntity t1" +
                " INNER JOIN TimeEntity t2" +
                " ON t1.timeItemId = t2.id"
    )
    abstract fun getAllTimerItemsAsMapsStream(): Flow<Map<TimeEntity, List<SelectedNumberEntity>>>

    @Insert(TimeEntity::class, onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertTimeEntity(timeEntity: TimeEntity)

    @Delete(TimeEntity::class)
    abstract suspend fun deleteTimeEntity(timeEntity: TimeEntity)

    @Insert(SelectedNumberEntity::class, onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertSelectedNumberEntities(selectedNumberEntities: List<SelectedNumberEntity>)

    @Delete(SelectedNumberEntity::class)
    protected abstract suspend fun deleteSelectedNumberEntities(selectedNumberEntities: List<SelectedNumberEntity>)

    @Transaction
    open suspend fun insertTimerItemAsPair(timerItemPair: Pair<TimeEntity, List<SelectedNumberEntity>>) {
        insertTimeEntity(timerItemPair.first)
        insertSelectedNumberEntities(timerItemPair.second)
    }

}

