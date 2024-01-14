package com.tjcoding.funtimer.utility

import com.tjcoding.funtimer.presentation.timer_setup.DurationOption
import com.tjcoding.funtimer.presentation.timer_setup.LayoutView
import kotlinx.coroutines.delay
import java.io.IOException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.ArrayList

object Util {
    const val DEFAULT_SELECTED_EXTRA_TIME = 2
    val DEFAULT_DISPLAYED_DURATIONS = mapOf(DurationOption.THIRTY_MINUTES to 30, DurationOption.SIXTY_MINUTES to 60, DurationOption.CUSTOM to -1)
    val DEFAULT_SELECTED_LAYOUT_VIEW = LayoutView.STANDARD
    val DEFAULT_DURATION_OPTION = DurationOption.THIRTY_MINUTES
    val DEFAULT_POSSIBLE_NUMBERS = (1..99).toList()
    val DEFAULT_SELECTED_NUMBERS = ArrayList<Int>(100)
    val DEFAULT_DISPLAYED_NUMBER = 1

    fun MutableList<Int>.addInOrder(newNumber: Int){
        this.add(newNumber)
        this.sort()
    }
    fun getDurationString(time: LocalDateTime): String {
        val unixEndTime = time.atZone(ZoneId.systemDefault()).toEpochSecond()
        val duration =  unixEndTime - (System.currentTimeMillis() /1000)
        val localTime = if(duration >= 0) LocalTime.ofSecondOfDay(duration) else LocalTime.ofSecondOfDay(0)
        return localTime.format(DateTimeFormatter.ofPattern("mm:ss"))
    }

    fun getDuration(time: LocalDateTime): String {

        // Define the desired date-time format
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        // Format the LocalDateTime to a string
        return time.format(formatter)
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
