package com.tjcoding.funtimer.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity

@Dao
interface SelectedNumberDao {
    @Query("SELECT * FROM SelectedNumberEntity")
    fun getAll(): List<SelectedNumberEntity>

    @Insert
    fun insert(selectedNumberEntity: SelectedNumberEntity)

    @Delete
    fun delete(selectedNumberEntity: SelectedNumberEntity)
}
