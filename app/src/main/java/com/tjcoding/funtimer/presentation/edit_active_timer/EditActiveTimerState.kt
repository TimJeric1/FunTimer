package com.tjcoding.funtimer.presentation.edit_active_timer

import com.tjcoding.funtimer.presentation.active_timers.ActiveTimerItem
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView
import com.tjcoding.funtimer.utility.Util.DEFAULT_ACTIVE_TIMER_ITEM
import com.tjcoding.funtimer.utility.Util.DEFAULT_DISPLAYED_NUMBER
import com.tjcoding.funtimer.utility.Util.DEFAULT_DURATION_OPTION
import com.tjcoding.funtimer.utility.Util.DEFAULT_POSSIBLE_NUMBERS
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_LAYOUT_VIEW
import com.tjcoding.funtimer.utility.Util.EDIT_ACTIVE_TIMER_SCREEN_DEFAULT_DISPLAYED_DURATIONS


data class EditActiveTimerState(
    val displayedNumber: Int = DEFAULT_DISPLAYED_NUMBER,
    val possibleNumbers: List<Int> = DEFAULT_POSSIBLE_NUMBERS,
    val selectedDurationOption: DurationOption = DEFAULT_DURATION_OPTION,
    val displayedDurations: Map<DurationOption, Int> = EDIT_ACTIVE_TIMER_SCREEN_DEFAULT_DISPLAYED_DURATIONS,
    val selectedLayoutView: LayoutView = DEFAULT_SELECTED_LAYOUT_VIEW,
    val editedActiveTimerItem: ActiveTimerItem = DEFAULT_ACTIVE_TIMER_ITEM,
    val originalTimerItem: ActiveTimerItem = DEFAULT_ACTIVE_TIMER_ITEM
)



