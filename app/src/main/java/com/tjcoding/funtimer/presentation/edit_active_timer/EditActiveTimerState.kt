package com.tjcoding.funtimer.presentation.edit_active_timer

import com.tjcoding.funtimer.presentation.active_timers.ActiveTimerItem
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView
import com.tjcoding.funtimer.utility.Util.DEFAULT_DISPLAYED_NUMBER
import com.tjcoding.funtimer.utility.Util.DEFAULT_POSSIBLE_NUMBERS
import java.time.LocalDateTime
import java.util.UUID


data class EditActiveTimerState(
    val displayedNumber: Int = DEFAULT_DISPLAYED_NUMBER,
    val possibleNumbers: List<Int> = DEFAULT_POSSIBLE_NUMBERS,
    val selectedDurationOption: DurationOption = DurationOption.FIRST,
    val displayedDurations: Map<DurationOption, Int> = mapOf(
        DurationOption.FIRST to 0,
        DurationOption.SECOND to 5,
        DurationOption.THIRD to -5,
    ),
    val selectedLayoutView: LayoutView = LayoutView.STANDARD,
    val editedActiveTimerItem: ActiveTimerItem = ActiveTimerItem(
        id = UUID.randomUUID(),
        selectedNumbers = emptyList(),
        alarmTime = 0,
        extraTime = 0,
        triggerTime = LocalDateTime.now()
    ),
    val originalTimerItem: ActiveTimerItem = ActiveTimerItem(
        id = UUID.randomUUID(),
        selectedNumbers = emptyList(),
        alarmTime = 0,
        extraTime = 0,
        triggerTime = LocalDateTime.now()
    )
)



