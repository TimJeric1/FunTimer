package com.tjcoding.funtimer.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.tjcoding.funtimer.domain.model.UserPreferences
import com.tjcoding.funtimer.domain.repository.UserPreferencesRepository
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView
import com.tjcoding.funtimer.presentation.common.toIndex
import com.tjcoding.funtimer.utility.Util.DEFAULT_DISPLAYED_DURATIONS
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_EXTRA_TIME
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
        val SELECTED_LAYOUT_VIEW = stringPreferencesKey("selected_layout_view")
        val SELECTED_EXTRA_TIME = intPreferencesKey("selected_extra_time")
        val SELECTED_CUSTOM_DURATIONS = stringSetPreferencesKey("selected_custom_durations")
    }

    override val userPreferencesStream: Flow<UserPreferences> = dataStore.data
        .retryWhen { cause, attempt -> shouldRetry(cause, attempt)}
        .catch { exception ->
            if(exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val customDurationsList = preferences[PreferencesKeys.SELECTED_CUSTOM_DURATIONS]?.map { it.toInt() } ?: DEFAULT_DISPLAYED_DURATIONS.values.toList()
            val customDurations = mapOf(
                DurationOption.FIRST to customDurationsList[DurationOption.FIRST.toIndex()],
                DurationOption.SECOND to customDurationsList[DurationOption.SECOND.toIndex()],
                DurationOption.THIRD to customDurationsList[DurationOption.THIRD.toIndex()],
            )
            val selectedLayoutView = LayoutView.fromString(preferences[PreferencesKeys.SELECTED_LAYOUT_VIEW] ?: "")
            val selectedExtraTime = preferences[PreferencesKeys.SELECTED_EXTRA_TIME] ?: DEFAULT_SELECTED_EXTRA_TIME
            UserPreferences(customDurations = customDurations, selectedLayoutView = selectedLayoutView, selectedExtraTime = selectedExtraTime)
        }
    

    override suspend fun updateSelectedCustomDurations(selectedCustomDuration: Int, index: Int) {
        dataStore.edit { preferences ->
            val selectedCustomDurations = preferences[PreferencesKeys.SELECTED_CUSTOM_DURATIONS]?.toList() ?: DEFAULT_DISPLAYED_DURATIONS.values.map { it.toString() }
            val newSelectedCustomDurations = selectedCustomDurations.toMutableList()
            newSelectedCustomDurations[index] = selectedCustomDuration.toString()
            preferences[PreferencesKeys.SELECTED_CUSTOM_DURATIONS] = newSelectedCustomDurations.toSet()
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