package com.tjcoding.funtimer.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.tjcoding.funtimer.domain.model.EditActiveTimerScreenUserPreferences
import com.tjcoding.funtimer.domain.model.TimerSetupScreenUserPreferences
import com.tjcoding.funtimer.domain.repository.UserPreferencesRepository
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView
import com.tjcoding.funtimer.presentation.common.toIndex
import com.tjcoding.funtimer.utility.Util.TIMER_SETUP_SCREEN_DEFAULT_DISPLAYED_DURATIONS
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_EXTRA_TIME
import com.tjcoding.funtimer.utility.Util.EDIT_ACTIVE_TIMER_SCREEN_DEFAULT_DISPLAYED_DURATIONS
import com.tjcoding.funtimer.utility.Util.shouldRetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val TIMER_SETUP_SCREEN_SELECTED_LAYOUT_VIEW = stringPreferencesKey("timer_setup_screen_selected_layout_view")
        val TIMER_SETUP_SCREEN_SELECTED_EXTRA_TIME = intPreferencesKey("timer_setup_screen_selected_extra_time")
        val TIMER_SETUP_SCREEN_SELECTED_CUSTOM_DURATIONS = stringSetPreferencesKey("selected_custom_durations")

        val EDIT_ACTIVE_TIMER_SCREEN_SELECTED_LAYOUT_VIEW = stringPreferencesKey("edit_active_timer_screen_selected_layout_view")
        val EDIT_ACTIVE_TIMER_SCREEN_SELECTED_CUSTOM_DURATIONS = stringSetPreferencesKey("edit_active_timer_screen_selected_custom_durations")
    }

    override val timerSetupScreenUserPreferencesStream: Flow<TimerSetupScreenUserPreferences> = dataStore.data
        .retryWhen { cause, attempt -> shouldRetry(cause, attempt)}
        .catch { exception ->
            if(exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val timerSetupScreenSelectedDurations = preferences[PreferencesKeys.TIMER_SETUP_SCREEN_SELECTED_CUSTOM_DURATIONS]?.map { it.toInt() } ?: TIMER_SETUP_SCREEN_DEFAULT_DISPLAYED_DURATIONS.values.toList()
            val timerSetupScreenSelectedCustomDurations = mapOf(
                DurationOption.FIRST to timerSetupScreenSelectedDurations[DurationOption.FIRST.toIndex()],
                DurationOption.SECOND to timerSetupScreenSelectedDurations[DurationOption.SECOND.toIndex()],
                DurationOption.THIRD to timerSetupScreenSelectedDurations[DurationOption.THIRD.toIndex()],
            )
            val timerSetupScreenSelectedLayoutView = LayoutView.fromString(preferences[PreferencesKeys.TIMER_SETUP_SCREEN_SELECTED_LAYOUT_VIEW] ?: "")
            val timerSetupScreenSelectedExtraTime = preferences[PreferencesKeys.TIMER_SETUP_SCREEN_SELECTED_EXTRA_TIME] ?: DEFAULT_SELECTED_EXTRA_TIME


            TimerSetupScreenUserPreferences(
                selectedCustomDurations = timerSetupScreenSelectedCustomDurations,
                selectedLayoutView = timerSetupScreenSelectedLayoutView,
                selectedExtraTime = timerSetupScreenSelectedExtraTime,
            )
        }

    override val editActiveTimerScreenUserPreferencesStream: Flow<EditActiveTimerScreenUserPreferences> = dataStore.data
        .retryWhen { cause, attempt -> shouldRetry(cause, attempt)}
        .catch { exception ->
            if(exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val editActiveTimerScreenSelectedDurations = preferences[PreferencesKeys.EDIT_ACTIVE_TIMER_SCREEN_SELECTED_CUSTOM_DURATIONS]?.map { it.toInt() } ?: EDIT_ACTIVE_TIMER_SCREEN_DEFAULT_DISPLAYED_DURATIONS.values.toList()
            val editActiveTimerScreenSelectedCustomDurations = mapOf(
                DurationOption.FIRST to editActiveTimerScreenSelectedDurations[DurationOption.FIRST.toIndex()],
                DurationOption.SECOND to editActiveTimerScreenSelectedDurations[DurationOption.SECOND.toIndex()],
                DurationOption.THIRD to editActiveTimerScreenSelectedDurations[DurationOption.THIRD.toIndex()],
            )
            val editActiveTimerScreenSelectedLayoutView = LayoutView.fromString(preferences[PreferencesKeys.TIMER_SETUP_SCREEN_SELECTED_LAYOUT_VIEW] ?: "")
            EditActiveTimerScreenUserPreferences(
                selectedCustomDurations = editActiveTimerScreenSelectedCustomDurations,
                selectedLayoutView = editActiveTimerScreenSelectedLayoutView,
            )
        }


    override suspend fun updateTimerSetupScreenSelectedCustomDurations(selectedCustomDuration: Int, index: Int) {
        dataStore.edit { preferences ->
            val selectedCustomDurations = preferences[PreferencesKeys.TIMER_SETUP_SCREEN_SELECTED_CUSTOM_DURATIONS]?.toList() ?: TIMER_SETUP_SCREEN_DEFAULT_DISPLAYED_DURATIONS.values.map { it.toString() }
            val newSelectedCustomDurations = selectedCustomDurations.toMutableList()
            newSelectedCustomDurations[index] = selectedCustomDuration.toString()
            preferences[PreferencesKeys.TIMER_SETUP_SCREEN_SELECTED_CUSTOM_DURATIONS] = newSelectedCustomDurations.toSet()
        }
    }

    override suspend fun updateTimerSetupScreenSelectedLayoutView(selectedLayoutView: LayoutView) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIMER_SETUP_SCREEN_SELECTED_LAYOUT_VIEW] = selectedLayoutView.toString()
        }
    }

    override suspend fun updateTimerSetupScreenSelectedExtraTime(selectedExtraTime: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIMER_SETUP_SCREEN_SELECTED_EXTRA_TIME] = selectedExtraTime
        }
    }

    override suspend fun updateEditActiveTimerScreenLayoutView(layoutView: LayoutView) {
        dataStore.edit { it[PreferencesKeys.EDIT_ACTIVE_TIMER_SCREEN_SELECTED_LAYOUT_VIEW] = layoutView.toString() }
    }

    override suspend fun updateEditActiveTimerScreenCustomDurations(selectedCustomDuration: Int, index: Int) {
        dataStore.edit { preferences ->
            val selectedCustomDurations = preferences[PreferencesKeys.EDIT_ACTIVE_TIMER_SCREEN_SELECTED_CUSTOM_DURATIONS]?.toList() ?: EDIT_ACTIVE_TIMER_SCREEN_DEFAULT_DISPLAYED_DURATIONS.values.map { it.toString() }
            val newSelectedCustomDurations = selectedCustomDurations.toMutableList()
            newSelectedCustomDurations[index] = selectedCustomDuration.toString()
            preferences[PreferencesKeys.EDIT_ACTIVE_TIMER_SCREEN_SELECTED_CUSTOM_DURATIONS] = newSelectedCustomDurations.toSet()
        }
    }
}