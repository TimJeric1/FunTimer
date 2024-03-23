package com.tjcoding.funtimer.presentation.timer_setup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tjcoding.funtimer.data.repository.FakeTimerRepository
import com.tjcoding.funtimer.data.repository.FakeUserPreferencesRepository
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.service.alarm.FakeAlarmScheduler
import com.tjcoding.funtimer.utility.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)

class TimerSetupViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: TimerSetupViewModel

    private lateinit var fakeTimerRepository: FakeTimerRepository
    private lateinit var fakeAlarmScheduler: FakeAlarmScheduler
    private lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository

    @Before
    fun setup(){
        fakeTimerRepository = FakeTimerRepository()
        fakeAlarmScheduler = FakeAlarmScheduler()
        fakeUserPreferencesRepository = FakeUserPreferencesRepository()

        viewModel = TimerSetupViewModel(
            timerRepository = fakeTimerRepository,
            alarmScheduler = fakeAlarmScheduler,
            userPreferencesRepository = fakeUserPreferencesRepository,
        )
    }

    @Test
    fun `correct number displayed when fakeTimerRepository is prepopulated`() = runTest {

        val initalSelectedNumbers = listOf(1, 2, 4, 5)
        val timerItem = TimerItem(
            id = UUID.randomUUID(),
            selectedNumbers = initalSelectedNumbers,
            triggerTime = LocalDateTime.now(),
            alarmTime = 5,
            extraTime = 10,
            hasTriggered = false
        )

        fakeTimerRepository.insertTimerItem(timerItem)


        // creating a new viewmodel instance so it contains correct displayed number (because
        // the displayed number is decided on viewmodel creation)
        viewModel = TimerSetupViewModel(
            timerRepository = fakeTimerRepository,
            alarmScheduler = fakeAlarmScheduler,
            userPreferencesRepository = fakeUserPreferencesRepository
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        assertEquals(3, viewModel.state.value.displayedNumber)

    }

    @Test
    fun `onEvent should update state when OnDurationRadioButtonClick event is triggered`() = runTest {

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        // Given
        val initialDurationOption = viewModel.state.value.selectedDurationOption
        val newDurationOption = DurationOption.SECOND


        // When
        viewModel.onEvent(TimerSetupEvent.OnDurationRadioButtonClick(newDurationOption))

        delay(1000)


        // Then
        val currentState = viewModel.state.value
        assertEquals(newDurationOption, currentState.selectedDurationOption)
        assertNotEquals(initialDurationOption, currentState.selectedDurationOption)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEvent should update selected duration option to 30 when OnCustomDurationPicked event is triggered`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        delay(1000)

        // When
        viewModel.onEvent(TimerSetupEvent.OnCustomDurationPicked(30))

        delay(1000)

        val newCustomDuration = viewModel.state.value.displayedDurations.get(viewModel.state.value.selectedDurationOption)

        assertEquals(30, newCustomDuration)


    }

    @Test
    fun `onEvent should add a new timer item when OnAddButtonClick event is triggered`() = runTest {

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }


        // Given
        val initialTimerItemCount = fakeTimerRepository.timerItems.value.size

        // When
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)
        viewModel.onEvent(TimerSetupEvent.OnSaveButtonClick)

        delay(1000)


        // Then
        val currentTimerItemCount = fakeTimerRepository.timerItems.value.size
        assertEquals(initialTimerItemCount + 1, currentTimerItemCount)
    }

    // Test for onAddButtonClick method
    @Test
    fun `onAddButtonClick adds displayed number to selected numbers list`() = runTest {

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        val oldState = viewModel.state.value

        // Call onAddButtonClick
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        val newState = viewModel.state.value

        // Assert that the displayed number is added to the selected numbers list
        assertTrue(newState.selectedNumbers.contains(oldState.displayedNumber))
    }

    // Test for onDurationRadioButtonClick method
    @Test
    fun `onDurationRadioButtonClick updates selected duration option`() = runTest {
        val newDurationOption = DurationOption.SECOND

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        // Call onDurationRadioButtonClick with new duration option
        viewModel.onEvent(TimerSetupEvent.OnDurationRadioButtonClick(newDurationOption))

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        val newState = viewModel.state.value

        // Assert that the selected duration option is updated
        assertEquals(newDurationOption, newState.selectedDurationOption)
    }

    // Test for onCustomDurationPicked method
    @Test
    fun `onCustomDurationPicked updates selected custom duration`() = runTest {
        val newCustomDuration = 30

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        // Call onCustomDurationPicked with new custom duration
        viewModel.onEvent(TimerSetupEvent.OnCustomDurationPicked(newCustomDuration))

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        val newState = viewModel.state.value

        // Assert that the selected custom duration is updated
        assertEquals(newCustomDuration, newState.displayedDurations[DurationOption.indexToDurationOption(0)])
    }

    // Test for onExtraTimePicked method
    @Test
    fun `onExtraTimePicked updates selected extra time`() = runTest {
        val newExtraTime = 10

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        val initalState = viewModel.state.value


        // Call onExtraTimePicked with new extra time
        viewModel.onEvent(TimerSetupEvent.OnExtraTimePicked(newExtraTime))

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        val newState = viewModel.state.value

        assertNotEquals(initalState, newState)

        // Assert that the selected extra time is updated
        assertEquals(newExtraTime, newState.selectedExtraTime)
    }

    // Test for onLayoutViewButtonClick method
    @Test
    fun `onLayoutViewButtonClick toggles between standard and alternative layout views`() = runTest {

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        // Initial state
        val initialState = viewModel.state.value

        // Click on layout view button
        viewModel.onEvent(TimerSetupEvent.OnLayoutViewIconClick)
        var newState = viewModel.state.value

        // Assert that layout view is toggled
        assertNotEquals(initialState.selectedLayoutView, newState.selectedLayoutView)

        // Click again on layout view button
        viewModel.onEvent(TimerSetupEvent.OnLayoutViewIconClick)
        newState = viewModel.state.value

        // Assert that layout view is toggled back to initial state
        assertEquals(initialState.selectedLayoutView, newState.selectedLayoutView)
    }
    // Test for OnDurationRadioButtonLongClick method
    @Test
    fun `onDurationRadioButtonLongClick should trigger custom time picker dialog`() = runTest {

        var isDialogTriggered = false

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.shouldShowCustomTimePickerDialogStream.collect{
                isDialogTriggered = it
            }
        }

        assertFalse(isDialogTriggered)

        // Call onDurationRadioButtonLongClick
        viewModel.onEvent(TimerSetupEvent.OnDurationRadioButtonLongClick)

        // Delay to allow dialog to be shown
        delay(1000)

        // Assert that custom time picker dialog is triggered
        assertTrue(isDialogTriggered)
    }

    // Test for OnRestartIconClick method
    @Test
    fun `onRestartIconClick should clear selected numbers and reset displayed number`() = runTest {

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        val initalState = viewModel.state.value

        viewModel.onEvent(TimerSetupEvent.OnRestartIconClick)

        var newState = viewModel.state.value

        assertEquals(initalState, newState)

        // Set initial selected numbers and displayed number
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)

        // Delay to allow state update
        delay(1000)

        newState = viewModel.state.value
        assertNotEquals(initalState, newState)

        // Call onRestartIconClick
        viewModel.onEvent(TimerSetupEvent.OnRestartIconClick)

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        newState = viewModel.state.value

        // Assert that selected numbers are cleared and displayed number is reset
        assertEquals(emptyList<Int>(), newState.selectedNumbers)
        assertEquals(1, newState.displayedNumber)
    }

    // Test for OnBackspaceIconClick method
    @Test
    fun `onBackspaceIconClick should remove last selected number and add it back to possible numbers`() = runTest {

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        val initalState = viewModel.state.value

        viewModel.onEvent(TimerSetupEvent.OnBackspaceIconClick)

        var newState = viewModel.state.value

        assertEquals(initalState, newState)

        // Set initial selected numbers and displayed number
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)

        // Delay to allow state update
        delay(1000)

        newState = viewModel.state.value
        assertNotEquals(initalState, newState)

        // Call onBackspaceIconClick
        viewModel.onEvent(TimerSetupEvent.OnBackspaceIconClick)

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        newState = viewModel.state.value

        // Assert that last selected number is removed and added back to possible numbers
        assertEquals(listOf(1), newState.selectedNumbers)
        assertTrue(newState.possibleNumbers.contains(2))
    }

    // Test for onSaveButtonClick method
    @Test
    fun `onSaveButtonClick should insert timer item into repository and schedule alarm`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        // Set up initial state
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)
        viewModel.onEvent(TimerSetupEvent.OnDurationRadioButtonClick(DurationOption.FIRST))

        // Delay to allow state update
        delay(1000)


        assertEquals(0, fakeTimerRepository.timerItems.value.size)
        assertEquals(0, fakeAlarmScheduler.scheduledItems.size)

        // Call onSaveButtonClick
        viewModel.onEvent(TimerSetupEvent.OnSaveButtonClick)

        // Delay to allow repository operation and alarm scheduling
        delay(1000)

        // Retrieve the updated state
        val newState = viewModel.state.value

        // Assert that selected numbers are cleared
        assertEquals(emptyList<Int>(), newState.selectedNumbers)

        // Assert that a timer item is inserted into the repository
        assertTrue(newState.selectedNumbers.isEmpty())

        // Assert that an alarm is scheduled
        // You need to assert this based on the behavior of your fake dependencies
        assertEquals(1, fakeTimerRepository.timerItems.value.size)
        assertEquals(1, fakeAlarmScheduler.scheduledItems.size)
        assertEquals(fakeAlarmScheduler.scheduledItems.get(0), fakeTimerRepository.timerItems.value.get(0))
        assertEquals(listOf(1,2), fakeTimerRepository.timerItems.value.get(0).selectedNumbers)

    }

    // Test for OnLeftFilledArrowClick method
    @Test
    fun `onLeftFilledArrowClick should update displayed number to the left number in possible numbers`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        // Set initial possible numbers and displayed number
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)
        viewModel.onEvent(TimerSetupEvent.OnRightFilledArrowClick)
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)

        // Delay to allow state update
        delay(1000)

        // Call onLeftFilledArrowClick
        viewModel.onEvent(TimerSetupEvent.OnLeftFilledArrowClick)

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        val newState = viewModel.state.value

        // Assert that displayed number is updated to the left number in possible numbers
        assertEquals(3, newState.displayedNumber)
    }

    // Test for OnRightFilledArrowClick method
    @Test
    fun `onRightFilledArrowClick should update displayed number to the right number in possible numbers`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        // Set initial possible numbers and displayed number
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)
        viewModel.onEvent(TimerSetupEvent.OnAddButtonClick)

        // Delay to allow state update
        delay(1000)

        // Call onRightFilledArrowClick
        viewModel.onEvent(TimerSetupEvent.OnRightFilledArrowClick)

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        val newState = viewModel.state.value

        // Assert that displayed number is updated to the right number in possible numbers
        assertEquals(5, newState.displayedNumber)
    }

    // Test for OnSaveButtonClick method with empty selected numbers list
    @Test
    fun `onSaveButtonClick with empty selected numbers list should not insert timer item`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        assertEquals(0, fakeTimerRepository.timerItems.value.size)
        assertEquals(0, fakeAlarmScheduler.scheduledItems.size)

        // Call onSaveButtonClick with empty selected numbers list
        viewModel.onEvent(TimerSetupEvent.OnSaveButtonClick)

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        val newState = viewModel.state.value

        // Assert that selected numbers remain empty
        assertTrue(newState.selectedNumbers.isEmpty())

        // Assert that no timer item is inserted into the repository
        assertEquals(0, fakeTimerRepository.timerItems.value.size)
        assertEquals(0, fakeAlarmScheduler.scheduledItems.size)
        // You need to assert this based on the behavior of your fake dependencies
    }







}
