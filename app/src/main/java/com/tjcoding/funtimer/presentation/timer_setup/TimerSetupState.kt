package com.tjcoding.funtimer.presentation.timer_setup

import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView
import com.tjcoding.funtimer.presentation.common.toDuration
import com.tjcoding.funtimer.utility.Util.TIMER_SETUP_SCREEN_DEFAULT_DISPLAYED_DURATIONS
import com.tjcoding.funtimer.utility.Util.DEFAULT_DISPLAYED_NUMBER
import com.tjcoding.funtimer.utility.Util.DEFAULT_DURATION_OPTION
import com.tjcoding.funtimer.utility.Util.DEFAULT_POSSIBLE_NUMBERS
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_EXTRA_TIME
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_LAYOUT_VIEW
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_NUMBERS


data class TimerSetupState(
    val displayedNumber: Int = DEFAULT_DISPLAYED_NUMBER,
    val selectedNumbers: List<Int> = DEFAULT_SELECTED_NUMBERS,
    val possibleNumbers: List<Int> = DEFAULT_POSSIBLE_NUMBERS,
    val selectedDurationOption: DurationOption = DEFAULT_DURATION_OPTION,
    val displayedDurations: Map<DurationOption, Int> = TIMER_SETUP_SCREEN_DEFAULT_DISPLAYED_DURATIONS,
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


