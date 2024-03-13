package com.tjcoding.funtimer.data.repository

import com.tjcoding.funtimer.domain.model.UserPreferences
import com.tjcoding.funtimer.domain.repository.UserPreferencesRepository
import com.tjcoding.funtimer.presentation.timer_setup.DurationOption
import com.tjcoding.funtimer.presentation.timer_setup.LayoutView
import com.tjcoding.funtimer.utility.Util.DEFAULT_DISPLAYED_DURATIONS
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_EXTRA_TIME
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_LAYOUT_VIEW
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeUserPreferencesRepository : UserPreferencesRepository {

    private val _userPreferencesStream = MutableStateFlow(UserPreferences(
        customDurations = mapOf(
            DurationOption.FIRST to DEFAULT_DISPLAYED_DURATIONS[DurationOption.FIRST]!!,
            DurationOption.SECOND to DEFAULT_DISPLAYED_DURATIONS[DurationOption.SECOND]!!,
            DurationOption.THIRD to DEFAULT_DISPLAYED_DURATIONS[DurationOption.THIRD]!!,
        ),
        selectedLayoutView = DEFAULT_SELECTED_LAYOUT_VIEW,
        selectedExtraTime = DEFAULT_SELECTED_EXTRA_TIME,
    ))

    override val userPreferencesStream: Flow<UserPreferences>
        get() = _userPreferencesStream.asStateFlow()

    override suspend fun updateSelectedCustomDurations(selectedCustomDuration: Int, index: Int) {
        _userPreferencesStream.update { currentPreferences ->
            currentPreferences.copy(
                customDurations = currentPreferences.customDurations.toMutableMap().apply {
                    put(DurationOption.indexToDurationOption(index), selectedCustomDuration)
                }
            )
        }
    }

    override suspend fun updateSelectedLayoutView(selectedLayoutView: LayoutView) {
        _userPreferencesStream.update { currentPreferences ->
            currentPreferences.copy(selectedLayoutView = selectedLayoutView)
        }
    }

    override suspend fun updateSelectedExtraTime(selectedExtraTime: Int) {
        _userPreferencesStream.update { currentPreferences ->
            currentPreferences.copy(selectedExtraTime = selectedExtraTime)
        }
    }
}
