package com.tjcoding.funtimer.domain.model

import com.tjcoding.funtimer.presentation.timer_setup.LayoutView

data class UserPreferences(
    val customDuration: Int,
    val selectedLayoutView: LayoutView,
    val selectedExtraTime: Int
)
