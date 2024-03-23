package com.tjcoding.funtimer.presentation.edit_active_timer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tjcoding.funtimer.data.repository.FakeTimerRepository
import com.tjcoding.funtimer.data.repository.FakeUserPreferencesRepository
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.presentation.active_timers.toActiveTimerItem
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.service.alarm.FakeAlarmScheduler
import com.tjcoding.funtimer.utility.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)

class EditActiveTimerViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: EditActiveTimerViewModel

    private lateinit var fakeTimerRepository: FakeTimerRepository
    private lateinit var fakeAlarmScheduler: FakeAlarmScheduler
    private lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository


    @Before
    fun setup() {
        fakeTimerRepository = FakeTimerRepository()
        fakeAlarmScheduler = FakeAlarmScheduler()
        fakeUserPreferencesRepository = FakeUserPreferencesRepository()
        viewModel = EditActiveTimerViewModel(
            timerRepository = fakeTimerRepository,
            alarmScheduler = fakeAlarmScheduler,
            userPreferencesRepository = fakeUserPreferencesRepository
        )
    }

    @Test
    fun `verify initial state`() {
        assertEquals(EditActiveTimerState(), viewModel.state.value)
    }

    @Test
    fun `verify onScreenLaunch updates state correctly`() = runTest {

        val timerItemId = UUID.randomUUID()
        val timerItem = TimerItem(
            id = timerItemId,
            selectedNumbers = listOf(1, 2, 3),
            triggerTime = LocalDateTime.now(),
            alarmTime = 5,
            extraTime = 10,
            hasTriggered = false
        )
        fakeTimerRepository.insertTimerItem(timerItem)

        viewModel = EditActiveTimerViewModel(
            timerRepository = fakeTimerRepository,
            alarmScheduler = fakeAlarmScheduler,
            userPreferencesRepository = fakeUserPreferencesRepository
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }


        // Mock data


        // Trigger onScreenLaunch
        viewModel.onEvent(EditActiveTimerEvent.OnScreenLaunch(timerItemId))

        delay(1000)

        // Verify that state is updated correctly
        assertEquals(timerItem.toActiveTimerItem(), viewModel.state.value.editedActiveTimerItem)
        assertEquals(timerItem.toActiveTimerItem(), viewModel.state.value.originalTimerItem)
        assertEquals(4, viewModel.state.value.displayedNumber)
        timerItem.selectedNumbers.forEach {
            assertFalse(
                viewModel.state.value.possibleNumbers.contains(
                    it
                )
            )
        }
    }


    @Test
    fun `verify onLayoutViewButtonClick updates user preferences repository`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        val oldSelectedLayoutView = viewModel.state.value.selectedLayoutView

        // Trigger onLayoutViewButtonClick
        viewModel.onEvent(EditActiveTimerEvent.OnLayoutViewIconClick)

        val newSelectedLayoutView = viewModel.state.value.selectedLayoutView

        // Verify that user preferences repository was called with correct parameters
        assertNotEquals(oldSelectedLayoutView, newSelectedLayoutView)
    }

    @Test
    fun `verify onRestartIconClick resets selected numbers`() = runTest {

        val initalSelectedNumbers = listOf(1, 2, 3, 4, 5)
        val originalTimerItem = TimerItem(
            id = UUID.randomUUID(),
            selectedNumbers = initalSelectedNumbers,
            triggerTime = LocalDateTime.now(),
            alarmTime = 5,
            extraTime = 10,
            hasTriggered = false
        )

        fakeTimerRepository.insertTimerItem(originalTimerItem)

        // creating a new viewmodel instance so it contains correct displayed number (because
        // the displayed number is decided on viewmodel creation)
        viewModel = EditActiveTimerViewModel(
            timerRepository = fakeTimerRepository,
            alarmScheduler = fakeAlarmScheduler,
            userPreferencesRepository = fakeUserPreferencesRepository
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        // Set original timer item in ViewModel state
        viewModel.onEvent(EditActiveTimerEvent.OnScreenLaunch(originalTimerItem.id))

        viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)

        var newSelectedNumbers = viewModel.state.value.editedActiveTimerItem.selectedNumbers

        assertNotEquals(initalSelectedNumbers, newSelectedNumbers)
        assertEquals(listOf(1, 2, 3, 4, 5, 6), newSelectedNumbers)

        // Trigger onRestartIconClick
        viewModel.onEvent(EditActiveTimerEvent.OnRestartIconClick)

        newSelectedNumbers = viewModel.state.value.editedActiveTimerItem.selectedNumbers

        // Verify that selected numbers are reset
        assertEquals(newSelectedNumbers, initalSelectedNumbers)
    }


    @Test
    fun `onEvent should update state when OnDurationRadioButtonClick event is triggered`() =
        runTest {

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect()
            }
            // Given
            val initialDurationOption = viewModel.state.value.selectedDurationOption
            val newDurationOption = DurationOption.SECOND


            // When
            viewModel.onEvent(EditActiveTimerEvent.OnDurationRadioButtonClick(newDurationOption))

            delay(1000)


            // Then
            val currentState = viewModel.state.value
            assertEquals(newDurationOption, currentState.selectedDurationOption)
            assertNotEquals(initialDurationOption, currentState.selectedDurationOption)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEvent should update selected duration option to 30 when OnCustomDurationPicked event is triggered`() =
        runTest {
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect()
            }

            delay(1000)

            // When
            viewModel.onEvent(EditActiveTimerEvent.OnCustomDurationPicked(30))

            delay(1000)

            val newCustomDuration =
                viewModel.state.value.displayedDurations.get(viewModel.state.value.selectedDurationOption)

            assertEquals(30, newCustomDuration)


        }

    @Test
    fun `onEvent should edit a timer item when OnAddButtonClick event is triggered`() = runTest {

        val timerItemId = UUID.randomUUID()
        val timerItem = TimerItem(
            id = timerItemId,
            selectedNumbers = listOf(1, 2, 3),
            triggerTime = LocalDateTime.now(),
            alarmTime = 5,
            extraTime = 10,
            hasTriggered = false
        )
        fakeTimerRepository.insertTimerItem(timerItem)

        viewModel = EditActiveTimerViewModel(
            timerRepository = fakeTimerRepository,
            alarmScheduler = fakeAlarmScheduler,
            userPreferencesRepository = fakeUserPreferencesRepository
        )


        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        viewModel.onEvent(EditActiveTimerEvent.OnScreenLaunch(timerItemId))


        // Given
        val initialTimerItem = fakeTimerRepository.timerItems.value.get(0)

        // When
        viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)
        viewModel.onEvent(EditActiveTimerEvent.OnSaveButtonClick)

        delay(1000)


        // Then
        val currentTimerItem = fakeTimerRepository.timerItems.value.get(0)
        assertNotEquals(initialTimerItem, currentTimerItem)
        assertEquals(
            initialTimerItem.selectedNumbers.size + 1,
            currentTimerItem.selectedNumbers.size
        )
    }

    // Test for onAddButtonClick method
    @Test
    fun `onAddButtonClick adds displayed number to selected numbers list`() = runTest {

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        val oldState = viewModel.state.value

        // Call onAddButtonClick
        viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        val newState = viewModel.state.value

        // Assert that the displayed number is added to the selected numbers list
        assertTrue(newState.editedActiveTimerItem.selectedNumbers.contains(oldState.displayedNumber))
    }

    // Test for onDurationRadioButtonClick method
    @Test
    fun `onDurationRadioButtonClick updates selected duration option`() = runTest {
        val newDurationOption = DurationOption.SECOND

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        // Call onDurationRadioButtonClick with new duration option
        viewModel.onEvent(EditActiveTimerEvent.OnDurationRadioButtonClick(newDurationOption))

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
        viewModel.onEvent(EditActiveTimerEvent.OnCustomDurationPicked(newCustomDuration))

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        val newState = viewModel.state.value

        // Assert that the selected custom duration is updated
        assertEquals(
            newCustomDuration,
            newState.displayedDurations[DurationOption.indexToDurationOption(0)]
        )
    }

    // Test for onLayoutViewButtonClick method
    @Test
    fun `onLayoutViewButtonClick toggles between standard and alternative layout views`() =
        runTest {

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect()
            }

            // Initial state
            val initialState = viewModel.state.value

            // Click on layout view button
            viewModel.onEvent(EditActiveTimerEvent.OnLayoutViewIconClick)
            var newState = viewModel.state.value

            // Assert that layout view is toggled
            assertNotEquals(initialState.selectedLayoutView, newState.selectedLayoutView)

            // Click again on layout view button
            viewModel.onEvent(EditActiveTimerEvent.OnLayoutViewIconClick)
            newState = viewModel.state.value

            // Assert that layout view is toggled back to initial state
            assertEquals(initialState.selectedLayoutView, newState.selectedLayoutView)
        }

    // Test for OnDurationRadioButtonLongClick method
    @Test
    fun `onDurationRadioButtonLongClick should trigger custom time picker dialog`() = runTest {

        var isDialogTriggered = false

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.shouldShowCustomTimePickerDialogStream.collect {
                isDialogTriggered = it
            }
        }

        assertFalse(isDialogTriggered)

        // Call onDurationRadioButtonLongClick
        viewModel.onEvent(EditActiveTimerEvent.OnDurationRadioButtonLongClick)

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

        viewModel.onEvent(EditActiveTimerEvent.OnRestartIconClick)

        var newState = viewModel.state.value

        assertEquals(initalState, newState)

        // Set initial selected numbers and displayed number
        viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)
        viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)

        // Delay to allow state update
        delay(1000)

        newState = viewModel.state.value
        assertNotEquals(initalState, newState)

        // Call onRestartIconClick
        viewModel.onEvent(EditActiveTimerEvent.OnRestartIconClick)

        // Delay to allow state update
        delay(1000)

        // Retrieve the updated state
        newState = viewModel.state.value

        // Assert that selected numbers are cleared and displayed number is reset
        assertEquals(emptyList<Int>(), newState.editedActiveTimerItem.selectedNumbers)
        assertEquals(1, newState.displayedNumber)
    }

    // Test for OnBackspaceIconClick method
    @Test
    fun `onBackspaceIconClick should remove last selected number and add it back to possible numbers`() =
        runTest {

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect()
            }

            val initalState = viewModel.state.value

            viewModel.onEvent(EditActiveTimerEvent.OnBackspaceIconClick)

            var newState = viewModel.state.value

            assertEquals(initalState, newState)

            // Set initial selected numbers and displayed number
            viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)
            viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)

            // Delay to allow state update
            delay(1000)

            newState = viewModel.state.value
            assertNotEquals(initalState, newState)

            // Call onBackspaceIconClick
            viewModel.onEvent(EditActiveTimerEvent.OnBackspaceIconClick)

            // Delay to allow state update
            delay(1000)

            // Retrieve the updated state
            newState = viewModel.state.value

            // Assert that last selected number is removed and added back to possible numbers
            assertEquals(listOf(1), newState.editedActiveTimerItem.selectedNumbers)
            assertTrue(newState.possibleNumbers.contains(2))
        }


    // Test for OnLeftFilledArrowClick method
    @Test
    fun `onLeftFilledArrowClick should update displayed number to the left number in possible numbers`() =
        runTest {
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect()
            }

            // Set initial possible numbers and displayed number
            viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)
            viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)
            viewModel.onEvent(EditActiveTimerEvent.OnRightFilledArrowClick)
            viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)

            // Delay to allow state update
            delay(1000)

            // Call onLeftFilledArrowClick
            viewModel.onEvent(EditActiveTimerEvent.OnLeftFilledArrowClick)

            // Delay to allow state update
            delay(1000)

            // Retrieve the updated state
            val newState = viewModel.state.value

            // Assert that displayed number is updated to the left number in possible numbers
            assertEquals(3, newState.displayedNumber)
        }

    // Test for OnRightFilledArrowClick method
    @Test
    fun `onRightFilledArrowClick should update displayed number to the right number in possible numbers`() =
        runTest {
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect()
            }

            // Set initial possible numbers and displayed number
            viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)
            viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)
            viewModel.onEvent(EditActiveTimerEvent.OnAddButtonClick)

            // Delay to allow state update
            delay(1000)

            // Call onRightFilledArrowClick
            viewModel.onEvent(EditActiveTimerEvent.OnRightFilledArrowClick)

            // Delay to allow state update
            delay(1000)

            // Retrieve the updated state
            val newState = viewModel.state.value

            // Assert that displayed number is updated to the right number in possible numbers
            assertEquals(5, newState.displayedNumber)
        }

    // Test for OnSaveButtonClick method with empty selected numbers list
    @Test
    fun `onSaveButtonClick with empty selected numbers list should not update timer item`() =
        runTest {

            val timerItemId = UUID.randomUUID()
            val timerItem = TimerItem(
                id = timerItemId,
                selectedNumbers = listOf(1, 2, 3),
                triggerTime = LocalDateTime.now(),
                alarmTime = 5,
                extraTime = 10,
                hasTriggered = false
            )
            fakeTimerRepository.insertTimerItem(timerItem)
            fakeAlarmScheduler.scheduleOrUpdateAlarm(timerItem)

            viewModel = EditActiveTimerViewModel(
                timerRepository = fakeTimerRepository,
                alarmScheduler = fakeAlarmScheduler,
                userPreferencesRepository = fakeUserPreferencesRepository
            )

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.state.collect()
            }

            viewModel.onEvent(EditActiveTimerEvent.OnScreenLaunch(timerItemId))

            assertEquals(1, fakeTimerRepository.timerItems.value.size)
            assertEquals(1, fakeAlarmScheduler.scheduledItems.size)

            viewModel.onEvent(EditActiveTimerEvent.OnBackspaceIconClick)
            viewModel.onEvent(EditActiveTimerEvent.OnBackspaceIconClick)
            viewModel.onEvent(EditActiveTimerEvent.OnBackspaceIconClick)

            assertTrue(viewModel.state.value.editedActiveTimerItem.selectedNumbers.isEmpty())

            // Call onSaveButtonClick with empty selected numbers list
            viewModel.onEvent(EditActiveTimerEvent.OnSaveButtonClick)

            // Delay to allow state update
            delay(1000)


            // Assert that no timer item is updated into the repository
            assertEquals(timerItem, fakeTimerRepository.timerItems.value.get(0))
            assertEquals(timerItem, fakeAlarmScheduler.scheduledItems.get(0))
        }

    @Test
    fun `onSaveButtonClick should update timer item`() = runTest {

        val timerItemId = UUID.randomUUID()
        val timerItem = TimerItem(
            id = timerItemId,
            selectedNumbers = listOf(1, 2, 3),
            triggerTime = LocalDateTime.now(),
            alarmTime = 5,
            extraTime = 10,
            hasTriggered = false
        )
        fakeTimerRepository.insertTimerItem(timerItem)
        fakeAlarmScheduler.scheduleOrUpdateAlarm(timerItem)

        viewModel = EditActiveTimerViewModel(
            timerRepository = fakeTimerRepository,
            alarmScheduler = fakeAlarmScheduler,
            userPreferencesRepository = fakeUserPreferencesRepository
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        viewModel.onEvent(EditActiveTimerEvent.OnScreenLaunch(timerItemId))

        assertEquals(1, fakeTimerRepository.timerItems.value.size)
        assertEquals(1, fakeAlarmScheduler.scheduledItems.size)

        viewModel.onEvent(EditActiveTimerEvent.OnBackspaceIconClick)
        viewModel.onEvent(EditActiveTimerEvent.OnDurationRadioButtonClick(DurationOption.THIRD))
        viewModel.onEvent(EditActiveTimerEvent.OnSaveButtonClick)

        // Delay to allow state update
        delay(1000)


        // Assert that no timer item is updated into the repository
        assertNotEquals(timerItem, fakeTimerRepository.timerItems.value.get(0))
        assertNotEquals(timerItem, fakeAlarmScheduler.scheduledItems.get(0))
        assertEquals(
            fakeTimerRepository.timerItems.value.get(0),
            fakeAlarmScheduler.scheduledItems.get(0)
        )
    }


}