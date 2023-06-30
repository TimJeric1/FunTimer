package com.tjcoding.funtimer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Objects

@Entity
data class TimerItemEntity (
    @PrimaryKey val key: Int,
    val unixEndTime: Long,
){
    override fun hashCode(): Int {
        return Objects.hash(key)
    }
}

@Entity(foreignKeys = arrayOf(ForeignKey(entity = TimerItemEntity::class,
    parentColumns = arrayOf("key"),
    childColumns = arrayOf("timerItemKey"),
    onDelete = ForeignKey.CASCADE)))
data class SelectedNumberEntity(
    @PrimaryKey val selectedNumber: Int,
    @ColumnInfo(index = true)
    val timerItemKey: Int,
        ) {
}