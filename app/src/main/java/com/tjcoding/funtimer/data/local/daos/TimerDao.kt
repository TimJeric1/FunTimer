package com.tjcoding.funtimer.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tjcoding.funtimer.data.local.entity.TimerItemEntity


@Dao
interface TimerDao {
    @Query("SELECT * FROM TimerItemEntity")
    fun getAll(): List<TimerItemEntity>

    @Insert
    fun insert(timerItemEntity: TimerItemEntity)

    @Delete
    fun delete(timerItemEntity: TimerItemEntity)
}
