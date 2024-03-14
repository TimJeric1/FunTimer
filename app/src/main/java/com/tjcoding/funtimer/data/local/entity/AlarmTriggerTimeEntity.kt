package com.tjcoding.funtimer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class AlarmTriggerTimeEntity (
    @PrimaryKey val id: UUID,
    val triggerTime: LocalDateTime,
    val alarmTime: Int,
    val extraTime: Int,
    val hasTriggered: Boolean
)
