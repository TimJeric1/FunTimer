package com.tjcoding.funtimer.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.local.entity.TimerItemEntity


@Dao
interface TimerDao {
    @Query("SELECT * FROM TimerItemEntity")
    suspend fun getAllTimerEntities(): List<TimerItemEntity>

    @Insert(TimerItemEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTimerEntity(timerItemEntity: TimerItemEntity)

    @Delete(TimerItemEntity::class)
    suspend fun deleteTimerEntity(timerItemEntity: TimerItemEntity)

    @Query("SELECT * FROM SelectedNumberEntity")
    suspend fun getAllSelectedNumberEntities(): List<SelectedNumberEntity>

    @Insert(SelectedNumberEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSelectedNumberEntity(selectedNumberEntity: SelectedNumberEntity)

    @Delete(SelectedNumberEntity::class)
    suspend fun deleteSelectedNumberEntity(selectedNumberEntity: SelectedNumberEntity)
}
