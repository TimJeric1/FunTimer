package com.tjcoding.funtimer.domain.repository

import com.tjcoding.funtimer.domain.model.EditActiveTimerScreenUserPreferences
import com.tjcoding.funtimer.domain.model.TimerSetupScreenUserPreferences
import com.tjcoding.funtimer.presentation.common.LayoutView
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val timerSetupScreenUserPreferencesStream: Flow<TimerSetupScreenUserPreferences>

    val editActiveTimerScreenUserPreferencesStream: Flow<EditActiveTimerScreenUserPreferences>

    suspend fun updateTimerSetupScreenSelectedCustomDurations(selectedCustomDuration: Int, index: Int)

    suspend fun updateTimerSetupScreenSelectedLayoutView(selectedLayoutView: LayoutView)

    suspend fun updateTimerSetupScreenSelectedExtraTime(selectedExtraTime: Int)

    suspend fun updateEditActiveTimerScreenLayoutView(layoutView: LayoutView)
    suspend fun updateEditActiveTimerScreenCustomDurations(selectedCustomDuration: Int, index: Int)
}