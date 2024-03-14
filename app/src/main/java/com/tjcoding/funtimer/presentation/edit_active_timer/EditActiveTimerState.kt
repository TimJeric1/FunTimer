package com.tjcoding.funtimer.presentation.edit_active_timer

import com.tjcoding.funtimer.presentation.active_timers.ActiveTimerItem
import com.tjcoding.funtimer.presentation.timer_setup.DurationOption
import com.tjcoding.funtimer.presentation.timer_setup.LayoutView
import com.tjcoding.funtimer.utility.Util.DEFAULT_DISPLAYED_NUMBER
import com.tjcoding.funtimer.utility.Util.DEFAULT_POSSIBLE_NUMBERS
import java.time.LocalDateTime


data class EditActiveTimerState(
    val displayedNumber: Int = DEFAULT_DISPLAYED_NUMBER,
    val possibleNumbers: List<Int> = DEFAULT_POSSIBLE_NUMBERS,
    val selectedDurationOption: DurationOption = DurationOption.FIRST,
    val displayedDurations: Map<DurationOption, Int> = mapOf(
        DurationOption.FIRST to 0,
        DurationOption.SECOND to 5, DurationOption.THIRD to -5
    ),
    val selectedLayoutView: LayoutView = LayoutView.STANDARD,
    val editedTimerItem: ActiveTimerItem = ActiveTimerItem(
        selectedNumbers = emptyList(),
        alarmTime = 0,
        extraTime = 0,
        triggerTime = LocalDateTime.now()
    ),
    val originalTimerItem: ActiveTimerItem = ActiveTimerItem(
        selectedNumbers = emptyList(),
        alarmTime = 0,
        extraTime = 0,
        triggerTime = LocalDateTime.now()
    )
) {
    fun getDuration(): Int {
        return selectedDurationOption.toDuration(displayedDurations)
    }

    fun getDurationInTimeFormat(): String {
        val duration = getDuration()
        return "$duration:00"
    }

    fun getExtraTimeInTimeFormat(): String {
        return "${editedTimerItem.extraTime}:00"
    }


}



fun DurationOption.toDuration(durations: Map<DurationOption, Int>): Int {
    val duration = durations[this]
    if (duration != null) return duration
    return durations.values.first()
}

fun DurationOption.toIndex(): Int {
    return when (this) {
        DurationOption.FIRST -> 0
        DurationOption.SECOND -> 1
        DurationOption.THIRD -> 2
    }
}


