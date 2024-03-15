package com.tjcoding.funtimer.data.repository

import com.tjcoding.funtimer.domain.model.EditActiveTimerScreenUserPreferences
import com.tjcoding.funtimer.domain.model.TimerSetupScreenUserPreferences
import com.tjcoding.funtimer.domain.repository.UserPreferencesRepository
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView
import com.tjcoding.funtimer.utility.Util.TIMER_SETUP_SCREEN_DEFAULT_DISPLAYED_DURATIONS
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_EXTRA_TIME
import com.tjcoding.funtimer.utility.Util.DEFAULT_SELECTED_LAYOUT_VIEW
import com.tjcoding.funtimer.utility.Util.EDIT_ACTIVE_TIMER_SCREEN_DEFAULT_DISPLAYED_DURATIONS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeUserPreferencesRepository : UserPreferencesRepository {

    private val fakeTimerSetupScreenUserPreferencesStream = MutableStateFlow(
        TimerSetupScreenUserPreferences(
            selectedCustomDurations = mapOf(
                DurationOption.FIRST to TIMER_SETUP_SCREEN_DEFAULT_DISPLAYED_DURATIONS[DurationOption.FIRST]!!,
                DurationOption.SECOND to TIMER_SETUP_SCREEN_DEFAULT_DISPLAYED_DURATIONS[DurationOption.SECOND]!!,
                DurationOption.THIRD to TIMER_SETUP_SCREEN_DEFAULT_DISPLAYED_DURATIONS[DurationOption.THIRD]!!,
            ),
            selectedLayoutView = DEFAULT_SELECTED_LAYOUT_VIEW,
            selectedExtraTime = DEFAULT_SELECTED_EXTRA_TIME,
        )
    )

    private val fakeEditActiveTimerScreenUserPreferencesStream = MutableStateFlow(
        EditActiveTimerScreenUserPreferences(
            selectedCustomDurations = mapOf(
                DurationOption.FIRST to EDIT_ACTIVE_TIMER_SCREEN_DEFAULT_DISPLAYED_DURATIONS[DurationOption.FIRST]!!,
                DurationOption.SECOND to EDIT_ACTIVE_TIMER_SCREEN_DEFAULT_DISPLAYED_DURATIONS[DurationOption.SECOND]!!,
                DurationOption.THIRD to EDIT_ACTIVE_TIMER_SCREEN_DEFAULT_DISPLAYED_DURATIONS[DurationOption.THIRD]!!,
            ),
            selectedLayoutView = DEFAULT_SELECTED_LAYOUT_VIEW,
        )
    )
    override val timerSetupScreenUserPreferencesStream: Flow<TimerSetupScreenUserPreferences>
        get() = fakeTimerSetupScreenUserPreferencesStream
    override val editActiveTimerScreenUserPreferencesStream: Flow<EditActiveTimerScreenUserPreferences>
        get() = fakeEditActiveTimerScreenUserPreferencesStream

    override suspend fun updateTimerSetupScreenSelectedCustomDurations(
        selectedCustomDuration: Int,
        index: Int
    ) {
        fakeTimerSetupScreenUserPreferencesStream.update { currentPreferences ->
            currentPreferences.copy(
                selectedCustomDurations = currentPreferences.selectedCustomDurations.toMutableMap()
                    .apply {
                        put(DurationOption.indexToDurationOption(index), selectedCustomDuration)
                    }
            )
        }
    }

    override suspend fun updateTimerSetupScreenSelectedLayoutView(selectedLayoutView: LayoutView) {
        fakeTimerSetupScreenUserPreferencesStream.update { currentPreferences ->
            currentPreferences.copy(selectedLayoutView = selectedLayoutView)
        }
    }

    override suspend fun updateTimerSetupScreenSelectedExtraTime(selectedExtraTime: Int) {
        fakeTimerSetupScreenUserPreferencesStream.update { currentPreferences ->
            currentPreferences.copy(selectedExtraTime = selectedExtraTime)
        }
    }

    override suspend fun updateEditActiveTimerScreenLayoutView(layoutView: LayoutView) {
        fakeEditActiveTimerScreenUserPreferencesStream.update {
            it.copy(
                selectedLayoutView = layoutView
            )
        }
    }

    override suspend fun updateEditActiveTimerScreenCustomDurations(
        selectedCustomDuration: Int,
        index: Int
    ) {
        fakeEditActiveTimerScreenUserPreferencesStream.update {
            it.copy(
                selectedCustomDurations = it.selectedCustomDurations.toMutableMap().apply {
                    put(DurationOption.indexToDurationOption(index), selectedCustomDuration)
                }
            )
        }
    }
}
