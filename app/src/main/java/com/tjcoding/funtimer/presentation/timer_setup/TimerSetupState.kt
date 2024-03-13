package com.tjcoding.funtimer.presentation.timer_setup

import android.util.Log
import com.tjcoding.funtimer.utility.Util.DEFAULT_DISPLAYED_DURATIONS
import com.tjcoding.funtimer.utility.Util.DEFAULT_DISPLAYED_NUMBER
import com.tjcoding.funtimer.utility.Util.DEFAULT_DURATION_OPTION
import com.tjcoding.funtimer.utility.Util.DEFAULT_POSSIBLE_NUMBERS
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_EXTRA_TIME
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_LAYOUT_VIEW
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_NUMBERS

private const val TAG = "TimerSetupState"

data class TimerSetupState(
    val displayedNumber: Int = DEFAULT_DISPLAYED_NUMBER,
    val selectedNumbers: List<Int> = DEFAULT_SELECTED_NUMBERS,
    val possibleNumbers: List<Int> = DEFAULT_POSSIBLE_NUMBERS,
    val selectedDurationOption: DurationOption = DEFAULT_DURATION_OPTION,
    val displayedDurations: Map<DurationOption, Int> = DEFAULT_DISPLAYED_DURATIONS,
    val selectedLayoutView: LayoutView = DEFAULT_SELECTED_LAYOUT_VIEW,
    val selectedExtraTime: Int = DEFAULT_SELECTED_EXTRA_TIME
){
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
    FIRST, SECOND, THIRD;

    companion object {
        fun indexToDurationOption(index: Int): DurationOption {
            return when (index) {
                0 -> FIRST
                1 -> SECOND
                2 -> THIRD
                else -> FIRST
            }
        }
    }
}

fun DurationOption.toDuration(durations: Map<DurationOption, Int>): Int{
    val duration = durations[this]
    if(duration != null) return duration
    return durations.values.first()
}
fun DurationOption. toIndex(): Int{
    return when(this){
        DurationOption.FIRST -> 0
        DurationOption.SECOND -> 1
        DurationOption.THIRD -> 2
    }
}
enum class LayoutView {
    STANDARD, ALTERNATIVE;


    companion object {
        fun fromString(value: String) : LayoutView {
            return when(value) {
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

