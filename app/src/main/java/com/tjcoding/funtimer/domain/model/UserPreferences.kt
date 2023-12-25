package com.tjcoding.funtimer.domain.model

import com.tjcoding.funtimer.presentation.timer_setup.DurationOption
import com.tjcoding.funtimer.presentation.timer_setup.LayoutView

data class UserPreferences(
    val customDurations: Map<DurationOption, Int>,
    val selectedLayoutView: LayoutView,
    val selectedExtraTime: Int
)
