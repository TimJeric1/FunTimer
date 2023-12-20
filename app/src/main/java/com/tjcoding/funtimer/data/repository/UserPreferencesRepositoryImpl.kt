package com.tjcoding.funtimer.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tjcoding.funtimer.domain.model.UserPreferences
import com.tjcoding.funtimer.domain.repository.UserPreferencesRepository
import com.tjcoding.funtimer.presentation.timer_setup.LayoutView
import com.tjcoding.funtimer.utility.Util.shouldRetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): UserPreferencesRepository {

    private object PreferencesKeys {
        val SELECTED_CUSTOM_DURATION = intPreferencesKey("selected_custom_duration")
        val SELECTED_LAYOUT_VIEW = stringPreferencesKey("selected_layout_view")
        val SELECTED_EXTRA_TIME = intPreferencesKey("selected_extra_time")
    }

    override val userPreferencesStream: Flow<UserPreferences> = dataStore.data
        .retryWhen { cause, attempt -> shouldRetry(cause, attempt)}
        .catch { exception ->
            if(exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val customDuration = preferences[PreferencesKeys.SELECTED_CUSTOM_DURATION] ?: -1
            val selectedLayoutView = LayoutView.fromString(preferences[PreferencesKeys.SELECTED_LAYOUT_VIEW] ?: "")
            val selectedExtraTime = preferences[PreferencesKeys.SELECTED_EXTRA_TIME] ?: 2
            UserPreferences(customDuration = customDuration, selectedLayoutView = selectedLayoutView, selectedExtraTime = selectedExtraTime)
        }
    

    override suspend fun updateSelectedCustomDuration(selectedCustomDuration: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_CUSTOM_DURATION] = selectedCustomDuration
        }
    }

    override suspend fun updateSelectedLayoutView(selectedLayoutView: LayoutView) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_LAYOUT_VIEW] = selectedLayoutView.toString()
        }
    }

    override suspend fun updateSelectedExtraTime(selectedExtraTime: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_EXTRA_TIME] = selectedExtraTime
        }
    }
}