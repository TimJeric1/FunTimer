package com.tjcoding.funtimer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class AlarmTriggerTimeEntity (
    @PrimaryKey val id: Int,
    val triggerTime: LocalDateTime,
    val alarmTime: Int,
    val extraTime: Int,
    val hasTriggered: Boolean
)
