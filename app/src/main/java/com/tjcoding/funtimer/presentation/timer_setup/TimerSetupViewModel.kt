package com.tjcoding.funtimer.presentation.timer_setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tjcoding.funtimer.BuildConfig
import com.tjcoding.funtimer.domain.model.AppError
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.domain.repository.UserPreferencesRepository
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView
import com.tjcoding.funtimer.presentation.common.toIndex
import com.tjcoding.funtimer.service.alarm.AlarmScheduler
import com.tjcoding.funtimer.utility.Util.addInOrder
import com.tjcoding.funtimer.utility.Util.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine


import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class TimerSetupViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val alarmScheduler: AlarmScheduler,
) : ViewModel() {


    private var timerItemsStreamCounter = 0
    private val timerItemsStream = timerRepository.getAllActiveTimerItemsStream()
        .catch { cause: Throwable -> handleError(cause, "Couldn't retrieve data") }
        .onEach { updateState(it) }
        // it has to be .statein otherwise it won't replay the last value on back navigation
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())



    private val userPreferencesStream = userPreferencesRepository.timerSetupScreenUserPreferencesStream
        .catch { cause: Throwable -> handleError(cause, "Couldn't retrieve preferences data") }

    private val _state = MutableStateFlow(TimerSetupState())
    // it has to combine timerItemStream in order for the timerItemsStream to have a collector
    val state = combine(_state, timerItemsStream, userPreferencesStream) { state, _, userPreferences ->
        state.copy(
            displayedDurations = userPreferences.selectedCustomDurations,
            selectedLayoutView = userPreferences.selectedLayoutView,
            selectedExtraTime = userPreferences.selectedExtraTime
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TimerSetupState())

    private val shouldShowCustomTimePickerDialogChannel = Channel<Boolean>()
    val shouldShowCustomTimePickerDialogStream = shouldShowCustomTimePickerDialogChannel.receiveAsFlow()

    private val shouldShowExtraTimePickerDialogChannel = Channel<Boolean>()
    val shouldShowExtraTimePickerDialogStream = shouldShowExtraTimePickerDialogChannel.receiveAsFlow()

    private val shouldShowSnackbarWithTextChannel = Channel<String>()
    val shouldShowSnackbarWithTextStream = shouldShowSnackbarWithTextChannel.receiveAsFlow()


    fun onEvent(event: TimerSetupEvent) {
        when (event) {
            TimerSetupEvent.OnAddButtonClick -> {
                onAddButtonClick()
            }

            is TimerSetupEvent.OnDurationRadioButtonClick -> {
                onDurationRadioButtonClick(event.duration)
            }

            TimerSetupEvent.OnSaveButtonClick -> {
                onSaveButtonClick()
            }

            TimerSetupEvent.OnLeftFilledArrowClick -> {
                onLeftFilledArrowClick()
            }

            TimerSetupEvent.OnRightFilledArrowClick -> {
                onRightFilledArrowClick()
            }

            is TimerSetupEvent.OnSelectedNumberClick -> {
                onSelectedNumberClick(event.number)
            }

            is TimerSetupEvent.OnCustomDurationPicked -> {
                onCustomDurationPicked(event.duration)
            }
            is TimerSetupEvent.OnExtraTimePicked -> {
                onExtraTimePicked(event.extraTime)
            }
            TimerSetupEvent.OnDurationRadioButtonLongClick -> {
                onDurationRadioButtonLongClick()
            }

            TimerSetupEvent.OnLayoutViewIconClick -> {
                onLayoutViewButtonClick()
            }
            TimerSetupEvent.OnExtraTimeIconClick -> {
                onExtraTimeButtonClick()
            }

            TimerSetupEvent.OnRestartIconClick -> {
                onRestartIconClick()
            }
            TimerSetupEvent.OnBackspaceIconClick -> {
                onBackspaceIconClick()
            }
        }


    }

    private fun onRestartIconClick() {
        val selectedNumbers = state.value.selectedNumbers.toMutableList()
        val newPossibleNumbers = state.value.possibleNumbers.toMutableList()
        for(number in selectedNumbers) newPossibleNumbers.addInOrder(number)
        _state.update {
            it.copy(
                displayedNumber = newPossibleNumbers.first(),
                selectedNumbers = emptyList(),
                possibleNumbers = newPossibleNumbers.toList(),
                selectedDurationOption = DurationOption.indexToDurationOption(0)
            )
        }
    }
    private fun onBackspaceIconClick() {
        val newSelectedNumbers = state.value.selectedNumbers.toMutableList()
        val removedNumber = newSelectedNumbers.removeLast()
        val newPossibleNumbers = state.value.possibleNumbers.toMutableList()
        newPossibleNumbers.addInOrder(removedNumber)
        _state.update {
            it.copy(
                selectedNumbers = newSelectedNumbers.toList(),
                possibleNumbers = newPossibleNumbers.toList()
            )
        }
    }

    private fun onExtraTimeButtonClick() {
        viewModelScope.launch {
            showExtraTimePickerDialog()
        }
    }

    private fun onLayoutViewButtonClick() {
        try {
            viewModelScope.launch {
                userPreferencesRepository.updateTimerSetupScreenSelectedLayoutView(if (state.value.selectedLayoutView ==
                    LayoutView.STANDARD) LayoutView.ALTERNATIVE else LayoutView.STANDARD)
            }
        } catch (cause: AppError) {
            handleError(cause, "Couldn't save layout change")
        }
    }

    private fun onDurationRadioButtonLongClick() {
        viewModelScope.launch { showCustomTimePickerDialog() }
    }

    private fun onCustomDurationPicked(duration: Int) {
        try {
            viewModelScope.launch {
                userPreferencesRepository.updateTimerSetupScreenSelectedCustomDurations(
                    selectedCustomDuration = duration,
                    index = state.value.selectedDurationOption.toIndex()
                )
            }
        } catch (cause: AppError) {
            handleError(cause, "Couldn't save layout change")
        }
    }

    private fun onExtraTimePicked(extraTime: Int) {
        try {
            viewModelScope.launch { userPreferencesRepository.updateTimerSetupScreenSelectedExtraTime(extraTime) }
        } catch (cause: AppError) {
            handleError(cause, "Couldn't save extra time change")
        }
    }

    private fun onSelectedNumberClick(number: Int) {
        val newSelectedNumbers = state.value.selectedNumbers.toMutableList()
        newSelectedNumbers.remove(number)
        val newPossibleNumbers = state.value.possibleNumbers.toMutableList()
        newPossibleNumbers.addInOrder(number)
        _state.update {
            it.copy(
                selectedNumbers = newSelectedNumbers.toList(),
                possibleNumbers = newPossibleNumbers.toList()
            )
        }
    }

    private fun onSaveButtonClick() {
        if (state.value.selectedNumbers.isEmpty()) return

        val isInDebugMode = BuildConfig.DEBUG
        val timerDuration = state.value.getDuration()
        val timerItem = TimerItem(
            id = UUID.randomUUID(),
            selectedNumbers = state.value.selectedNumbers,
            triggerTime = if (isInDebugMode) LocalDateTime.now()
                .plusSeconds(timerDuration.toLong()).plusSeconds(state.value.selectedExtraTime.toLong()) else LocalDateTime.now()
                .plusMinutes(timerDuration.toLong()).plusMinutes(state.value.selectedExtraTime.toLong()),
            alarmTime = state.value.displayedDurations[state.value.selectedDurationOption]!!,
            extraTime = state.value.selectedExtraTime,
            hasTriggered = false
        )
        try {
            viewModelScope.launch {
                timerRepository.insertTimerItem(timerItem)
                alarmScheduler.scheduleOrUpdateAlarm(timerItem)
                _state.update { it.copy(selectedNumbers = emptyList()) }
            }
        } catch (cause: AppError) {
            handleError(cause, "Couldn't insert item")
        }

    }

    private fun onDurationRadioButtonClick(newDuration: DurationOption) {
        if (state.value.selectedDurationOption == newDuration) return
        _state.update {
            it.copy(selectedDurationOption = newDuration)
        }
        if (newDuration == DurationOption.THIRD) onCustomDurationSelected()
    }

    private fun onCustomDurationSelected() {
        if (state.value.displayedDurations[DurationOption.THIRD] == -1)
            viewModelScope.launch { showCustomTimePickerDialog() }
    }

    private suspend fun showCustomTimePickerDialog() {
        shouldShowCustomTimePickerDialogChannel.send(true)
    }
    private suspend fun showExtraTimePickerDialog() {
        shouldShowExtraTimePickerDialogChannel.send(true)
    }


    private fun onAddButtonClick() {
        val numberToAdd = state.value.displayedNumber
        onRightFilledArrowClick()
        val newSelectedNumbers = state.value.selectedNumbers.toMutableList()
        newSelectedNumbers.addInOrder(numberToAdd)
        val newPossibleNumbers = state.value.possibleNumbers.toMutableList()
        newPossibleNumbers.remove(numberToAdd)
        _state.update {
            it.copy(
                selectedNumbers = newSelectedNumbers.toList(),
                possibleNumbers = newPossibleNumbers.toList(),
            )
        }

    }

    private fun onLeftFilledArrowClick() {
        val possibleNumbers = state.value.possibleNumbers
        val currentNumberIndex = possibleNumbers.indexOf(state.value.displayedNumber)
        if (currentNumberIndex == 0) return
        val newDisplayedNumber = possibleNumbers[currentNumberIndex - 1]
        _state.update {
            it.copy(displayedNumber = newDisplayedNumber)
        }
    }

    private fun onRightFilledArrowClick() {
        val possibleNumbers = state.value.possibleNumbers
        val currentNumberIndex = possibleNumbers.indexOf(state.value.displayedNumber)
        if (currentNumberIndex == possibleNumbers.size - 1) return
        val newDisplayedNumber = possibleNumbers[currentNumberIndex + 1]
        _state.update {
            it.copy(displayedNumber = newDisplayedNumber)
        }
    }


    private fun updateState(timerItems: List<TimerItem>) {
        timerItemsStreamCounter++

        updatePossibleNumbers(timerItems)

        val shouldUpdateDisplayedNumber = timerItemsStreamCounter <= 1

        // updates the displayed number only when user navigates to the screen or opens the app
        // this is used because otherwise if alarm fires when user is on this screen the displayed
        // number will update and user potentially doesn't want that
        if (shouldUpdateDisplayedNumber) {
            updateDisplayedNumber()
        }
    }

    private fun updateDisplayedNumber() {
        val newDisplayedNumber = state.value.possibleNumbers[0]
        _state.update {
            it.copy(displayedNumber = newDisplayedNumber)
        }
    }

    private fun updatePossibleNumbers(timerItems: List<TimerItem>) {
        val selectedNumbers = timerItems.map { it.selectedNumbers }.flatten()
        val newPossibleNumbers = (1..99).toList().toMutableList()
        newPossibleNumbers.removeAll(selectedNumbers)
        _state.update {
            it.copy(possibleNumbers = newPossibleNumbers)
        }
    }


    private fun handleError(cause: Throwable, extraContext: String) = viewModelScope.launch {
        val errorMsg = getErrorMessage(cause, extraContext)
        shouldShowSnackbarWithTextChannel.send(errorMsg)
    }

}