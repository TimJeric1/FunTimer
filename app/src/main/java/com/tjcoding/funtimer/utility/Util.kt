package com.tjcoding.funtimer.utility

import kotlinx.coroutines.delay
import java.io.IOException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Util {

    fun MutableList<Int>.addInOrder(newNumber: Int){
        this.add(newNumber)
        this.sort()
    }
    fun getDuration(time: LocalDateTime): String {
        val unixEndTime = time.atZone(ZoneId.systemDefault()).toEpochSecond()
        val duration =  unixEndTime - (System.currentTimeMillis() /1000)
        val localTime = if(duration >= 0) LocalTime.ofSecondOfDay(duration) else LocalTime.ofSecondOfDay(0)
        return localTime.format(DateTimeFormatter.ofPattern("mm:ss"))
    }
    suspend fun shouldRetry(cause: Throwable, attempt: Long): Boolean {
        if (attempt > 3) return false
        val currentDelay = 1000L * attempt
        if (cause is IOException) {
            delay(currentDelay)
            return true
        }
        return false
    }
}