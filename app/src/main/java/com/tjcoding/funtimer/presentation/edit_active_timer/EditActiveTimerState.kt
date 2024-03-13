package com.tjcoding.funtimer.presentation.edit_active_timer

import android.util.Log
import com.tjcoding.funtimer.utility.Util.DEFAULT_DISPLAYED_NUMBER
import com.tjcoding.funtimer.utility.Util.DEFAULT_POSSIBLE_NUMBERS
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_EXTRA_TIME
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_NUMBERS

private const val TAG = "TimerSetupState"

data class EditActiveTimerState(
    val displayedNumber: Int = DEFAULT_DISPLAYED_NUMBER,
    val selectedNumbers: List<Int> = DEFAULT_SELECTED_NUMBERS,
    val possibleNumbers: List<Int> = DEFAULT_POSSIBLE_NUMBERS,
    val selectedDurationOption: DurationOption = DurationOption.THIRTY_MINUTES,
    val displayedDurations: Map<DurationOption, Int> = mapOf(
        DurationOption.THIRTY_MINUTES to 30,
        DurationOption.SIXTY_MINUTES to 60, DurationOption.CUSTOM to -1
    ),
    val selectedLayoutView: LayoutView = LayoutView.STANDARD,
    val selectedExtraTime: Int = DEFAULT_SELECTED_EXTRA_TIME
) {
    fun getDuration(): Int {
        return selectedDurationOption.toDuration(displayedDurations)
    }

    fun getDurationInTimeFormat(): String {
        val duration = getDuration()
        return "$duration:00"
    }

    fun getExtraTimeInTimeFormat(): String {
        return "$selectedExtraTime:00"
    }
}


enum class DurationOption {
    THIRTY_MINUTES, SIXTY_MINUTES, CUSTOM;

    companion object {
        fun indexToDurationOption(index: Int): DurationOption {
            return when (index) {
                0 -> THIRTY_MINUTES
                1 -> SIXTY_MINUTES
                2 -> CUSTOM
                else -> THIRTY_MINUTES
            }
        }
    }
}

fun DurationOption.toDuration(durations: Map<DurationOption, Int>): Int {
    val duration = durations[this]
    if (duration != null) return duration
    return durations.values.first()
}

fun DurationOption.toIndex(): Int {
    return when (this) {
        DurationOption.THIRTY_MINUTES -> 0
        DurationOption.SIXTY_MINUTES -> 1
        DurationOption.CUSTOM -> 2
    }
}

enum class LayoutView {
    STANDARD, ALTERNATIVE;


    companion object {
        fun fromString(value: String): LayoutView {
            return when (value) {
                "STANDARD" -> STANDARD
                "ALTERNATIVE" -> ALTERNATIVE
                else -> {
                    Log.w(TAG, "Supplied invalid string for LayoutView.fromString()")
                    STANDARD
                }
            }
        }
    }
}

