package com.tjcoding.funtimer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AlarmTriggerTimeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("timeItemId"),
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class SelectedNumberEntity(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val selectedNumber: Int,
    @ColumnInfo(index = true)
    val timeItemId: Int,
)