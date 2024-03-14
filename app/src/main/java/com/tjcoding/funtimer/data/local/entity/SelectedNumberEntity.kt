package com.tjcoding.funtimer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.UUID

@Entity(
    primaryKeys = ["alarmTriggerTimeEntityId", "alarmTriggerTimeEntitySelectedNumberId"],
    foreignKeys = [
        ForeignKey(
            entity = AlarmTriggerTimeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("alarmTriggerTimeEntityId"),
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class SelectedNumberEntity(
    val alarmTriggerTimeEntityId: UUID,
    val alarmTriggerTimeEntitySelectedNumberId: Int,
    val selectedNumber: Int,
)