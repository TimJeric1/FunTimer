package com.tjcoding.funtimer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tjcoding.funtimer.data.local.dao.TimerDao
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.local.entity.AlarmTriggerTimeEntity
import com.tjcoding.funtimer.data.local.type_converter.Converters

@Database(entities = [AlarmTriggerTimeEntity::class, SelectedNumberEntity::class], version = 18, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TimerDatabase: RoomDatabase() {
    abstract fun timerDao(): TimerDao
}
