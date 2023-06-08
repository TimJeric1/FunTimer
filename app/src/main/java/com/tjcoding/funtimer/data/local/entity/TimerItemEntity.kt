package com.tjcoding.funtimer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class TimerItemEntity (
    @PrimaryKey(autoGenerate = true) val timerId: Int? = null,
    val time: Int,
    val unixEndTime: Long,
)

@Entity(foreignKeys = arrayOf(ForeignKey(entity = TimerItemEntity::class,
    parentColumns = arrayOf("timerId"),
    childColumns = arrayOf("timer_id"),
    onDelete = ForeignKey.CASCADE)))
data class SelectedNumberEntity(
    @PrimaryKey(autoGenerate = true) val selectedNumberId: Int? = null,
    val selectedNumber: Int,
    @ColumnInfo(name = "timer_id", index = true)
    val timerId : Int? = null,
        )