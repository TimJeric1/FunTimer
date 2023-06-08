package com.tjcoding.funtimer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tjcoding.funtimer.data.local.daos.TimerDao
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.local.entity.TimerItemEntity

@Database(entities = [TimerItemEntity::class, SelectedNumberEntity::class], version = 1)
abstract class TimerDatabase: RoomDatabase() {
    abstract fun timerDao(): TimerDao
}
