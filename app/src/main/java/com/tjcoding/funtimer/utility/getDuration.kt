package com.tjcoding.funtimer.utility

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getDuration(time: LocalDateTime): String {
    val unixEndTime = time.atZone(ZoneId.systemDefault()).toEpochSecond()
    val duration =  unixEndTime - (System.currentTimeMillis() /1000)
    val localTime = if(duration >= 0) LocalTime.ofSecondOfDay(duration) else LocalTime.ofSecondOfDay(0)
    return localTime.format(DateTimeFormatter.ofPattern("mm:ss"))
}