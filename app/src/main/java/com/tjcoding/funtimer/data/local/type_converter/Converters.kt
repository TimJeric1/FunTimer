package com.tjcoding.funtimer.data.local.type_converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {
    @TypeConverter
    fun fromLocalDateTime(time: LocalDateTime): Long {
        return time.atZone(ZoneId.systemDefault()).toEpochSecond()
    }

    @TypeConverter
    fun toLocalDateTime(unixTime: Long): LocalDateTime {
        return Instant.ofEpochSecond(unixTime).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }
}