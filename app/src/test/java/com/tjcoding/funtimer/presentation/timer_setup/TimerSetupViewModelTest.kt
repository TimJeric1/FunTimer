package com.tjcoding.funtimer.presentation.timer_setup

import com.tjcoding.funtimer.data.repository.FakeTimerRepository
import com.tjcoding.funtimer.data.repository.FakeUserPreferencesRepository
import com.tjcoding.funtimer.service.alarm.FakeAlarmScheduler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class TimerSetupViewModelTest {
    private lateinit var viewModel: TimerSetupViewModel

    @Before
    fun setup(){
        viewModel = TimerSetupViewModel(
            timerRepository = FakeTimerRepository(),
            alarmScheduler = FakeAlarmScheduler(),
            userPreferencesRepository = FakeUserPreferencesRepository(),
            isInDebugMode = true
        )
    }

    @Test
    fun `onEvent should update state when OnDurationRadioButtonClick event is triggered`() = runBlocking {
        // Given
        val initialDurationOption = viewModel.state.first().selectedDurationOption
        val newDurationOption = DurationOption.SIXTY_MINUTES

        // When
        viewModel.onEvent(TimerSetupEvent.OnDurationRadioButtonClick(newDurationOption))

        // Then
        val currentState = viewModel.state.first()
        assertEquals(newDurationOption, currentState.selectedDurationOption)
        assertNotEquals(initialDurationOption, currentState.selectedDurationOption)
    }

    @Test
    fun `onEvent should show custom time picker dialog when OnCustomDurationPicked event is triggered`() = runBlocking {
        // Given
        val shouldShowDialogBeforeEvent = viewModel.shouldShowCustomTimePickerDialogStream.first()

        // When
        viewModel.onEvent(TimerSetupEvent.OnCustomDurationPicked(30))

        // Then
        val shouldShowDialogAfterEvent = viewModel.shouldShowCustomTimePickerDialogStream.first()
        assertFalse(shouldShowDialogBeforeEvent)
        assertTrue(shouldShowDialogAfterEvent)
    }

    @Test
    fun `onEvent should add a new timer item when OnAddButtonClick event is triggered`() = runBlocking {
        // Given
        val initialTimerItemCount = viewModel.state.value.selectedNumbers.size

        // When
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)

        // Then
        val currentTimerItemCount = viewModel.state.value.selectedNumbers.size
        assertEquals(initialTimerItemCount + 1, currentTimerItemCount)
    }
}
