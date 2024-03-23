package com.tjcoding.funtimer.utility

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.tjcoding.funtimer.presentation.active_timers.ActiveTimerItem
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.UUID

object Util {
    const val DEFAULT_SELECTED_EXTRA_TIME = 2
    val TIMER_SETUP_SCREEN_DEFAULT_DISPLAYED_DURATIONS =
        mapOf(DurationOption.FIRST to 30, DurationOption.SECOND to 60, DurationOption.THIRD to -1)
    val DEFAULT_SELECTED_LAYOUT_VIEW = LayoutView.STANDARD
    val DEFAULT_DURATION_OPTION = DurationOption.FIRST
    val DEFAULT_POSSIBLE_NUMBERS = (1..99).toList()
    val DEFAULT_SELECTED_NUMBERS = ArrayList<Int>(100)
    val DEFAULT_DISPLAYED_NUMBER = 1

    val EDIT_ACTIVE_TIMER_SCREEN_DEFAULT_DISPLAYED_DURATIONS = mapOf(
        DurationOption.FIRST to 0,
        DurationOption.SECOND to 5,
        DurationOption.THIRD to -5,
    )

    val DEFAULT_ACTIVE_TIMER_ITEM = ActiveTimerItem(
        id = UUID.randomUUID(),
        selectedNumbers = emptyList(),
        alarmTime = 0,
        extraTime = 0,
        triggerTime = LocalDateTime.now()
    )

    fun MutableList<Int>.addInOrder(newNumber: Int) {
        this.add(newNumber)
        this.sort()
    }

    fun LocalDateTime.formatTo24HourAndMinute(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return this.format(formatter)
    }


    fun LocalDateTime.formatToTimeRemaining(): String {
        val unixEndTime = this.atZone(ZoneId.systemDefault()).toEpochSecond()
        val duration = unixEndTime - (System.currentTimeMillis() / 1000)
        val localTime =
            if (duration >= 0) LocalTime.ofSecondOfDay(duration) else LocalTime.ofSecondOfDay(0)
        return localTime.format(DateTimeFormatter.ofPattern("mm:ss"))
    }

    fun Long.SecondsFormatTommss(): String {
        val duration = this
        val localTime =
            if (duration >= 0) LocalTime.ofSecondOfDay(duration) else LocalTime.ofSecondOfDay(0)
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

    suspend fun <T> retryOnIOError(block: suspend () -> T): T {
        var attempt = 0
        while (true) {
            try {
                return block() // Successful insertion, return from the function
            } catch (cause: Throwable) {
                attempt++
                if (shouldRetry(cause, attempt.toLong())) {
                    continue
                } else {
                    throw cause // Rethrow after exceeding retries
                }
            }
        }
    }

    @Composable
    fun <T> ObserveAsEvents(stream: Flow<T>, onEvent: (T) -> Unit) {
        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(stream, lifecycleOwner.lifecycle) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main.immediate) {
                    stream.collect(onEvent)
                }
            }
        }
    }



}
