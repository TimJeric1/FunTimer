package com.tjcoding.funtimer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class TimerItemEntity (
    @PrimaryKey val unixEndTime: Long,
    val time: Int,
)

@Entity(foreignKeys = arrayOf(ForeignKey(entity = TimerItemEntity::class,
    parentColumns = arrayOf("unixEndTime"),
    childColumns = arrayOf("unixEndTime"),
    onDelete = ForeignKey.CASCADE)))
data class SelectedNumberEntity(
    @PrimaryKey val selectedNumber: Int,
    @ColumnInfo(index = true)
    val unixEndTime: Long,
        )