package com.tjcoding.funtimer.domain.model

import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView

data class UserPreferences(
    val customDurations: Map<DurationOption, Int>,
    val selectedLayoutView: LayoutView,
    val selectedExtraTime: Int
)
