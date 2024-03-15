package com.tjcoding.funtimer.domain.model

import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView

data class EditActiveTimerScreenUserPreferences(
    val selectedCustomDurations: Map<DurationOption, Int>,
    val selectedLayoutView: LayoutView,
)
