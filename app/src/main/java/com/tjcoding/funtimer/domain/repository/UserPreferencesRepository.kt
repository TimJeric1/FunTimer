package com.tjcoding.funtimer.domain.repository

import com.tjcoding.funtimer.domain.model.UserPreferences
import com.tjcoding.funtimer.presentation.timer_setup.LayoutView
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val userPreferencesStream: Flow<UserPreferences>

    suspend fun updateSelectedCustomDurations(selectedCustomDuration: Int, index: Int)

    suspend fun updateSelectedLayoutView(selectedLayoutView: LayoutView)

    suspend fun updateSelectedExtraTime(selectedExtraTime: Int)

}