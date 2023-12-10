package com.tjcoding.funtimer.domain.repository

import com.tjcoding.funtimer.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val userPreferencesStream: Flow<UserPreferences>

    suspend fun updateCustomDuration(customDuration: Int)

}