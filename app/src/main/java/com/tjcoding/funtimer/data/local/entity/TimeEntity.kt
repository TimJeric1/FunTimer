package com.tjcoding.funtimer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class TimeEntity (
    @PrimaryKey val id: Int,
    val time: LocalDateTime,
)
