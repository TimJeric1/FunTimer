package com.tjcoding.funtimer.presentation.timer_setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.domain.repository.UserPreferencesRepository
import com.tjcoding.funtimer.service.alarm.AlarmScheduler
import com.tjcoding.funtimer.utility.Util.addInOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine


import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class TimerSetupViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val alarmScheduler: AlarmScheduler,
    private val isInDebugMode: Boolean
) : ViewModel() {


    private var timerItemsStreamCounter = 0
    private val timerItemsStream = timerRepository.getAllTimerItemsStream()
        .onEach { updateState(it) }
        // it has to be .statein otherwise it won't replay the last value on back navigation
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val userPreferencesStream = userPreferencesRepository.userPreferencesStream

    private val _state = MutableStateFlow(TimerSetupState())
    // it has to combine timerItemStream in order for the timerItemsStream to have a collector
    val state = combine(_state, timerItemsStream, userPreferencesStream) { state, _, userPreferences ->
        state.copy(
            displayedDurations = state.displayedDurations + (DurationOption.CUSTOM to userPreferences.customDuration),
            selectedLayoutView = userPreferences.selectedLayoutView,
            selectedExtraTime = userPreferences.selectedExtraTime
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TimerSetupState())

    private val shouldShowCustomTimePickerDialogChannel = Channel<Boolean>()
    val shouldShowCustomTimePickerDialogStream = shouldShowCustomTimePickerDialogChannel.receiveAsFlow()

    private val shouldShowExtraTimePickerDialogChannel = Channel<Boolean>()
    val shouldShowExtraTimePickerDialogStream = shouldShowExtraTimePickerDialogChannel.receiveAsFlow()


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
            is TimerSetupEvent.OnDurationRadioButtonLongClick -> {
                onDurationRadioButtonLongClick(event.index)
            }

            TimerSetupEvent.OnLayoutViewIconClick -> {
                onLayoutViewButtonClick()
            }
            TimerSetupEvent.OnExtraTimeIconClick -> {
                onExtraTimeButtonClick()
            }
        }


    }

    private fun onExtraTimeButtonClick() {
        viewModelScope.launch {
            showExtraTimePickerDialog()
        }
    }

    private fun onLayoutViewButtonClick() {
        viewModelScope.launch {
            userPreferencesRepository.updateSelectedLayoutView(if (state.value.selectedLayoutView == LayoutView.STANDARD) LayoutView.ALTERNATIVE else LayoutView.STANDARD)
        }
    }

    private fun onDurationRadioButtonLongClick(index: Int) {
        val customDurationRadioButtonIsClicked = index == 2
        if (customDurationRadioButtonIsClicked) viewModelScope.launch { showCustomTimePickerDialog() }
    }

    private fun onCustomDurationPicked(duration: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateSelectedCustomDuration(duration)
        }
    }

    private fun onExtraTimePicked(extraTime: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateSelectedExtraTime(extraTime)
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
        val timerDuration = state.value.getDuration()
        val timerItem = TimerItem(
            selectedNumbers = state.value.selectedNumbers,
            alarmTime = if (isInDebugMode) LocalDateTime.now()
                .plusSeconds(timerDuration.toLong()) else LocalDateTime.now()
                .plusMinutes(timerDuration.toLong()),
            extraTime = state.value.selectedExtraTime
        )
        viewModelScope.launch {
            timerRepository.insertTimerItem(timerItem)
        }
        alarmScheduler.schedule(timerItem)
        _state.update {
            it.copy(selectedNumbers = emptyList())
        }
    }

    private fun onDurationRadioButtonClick(newDuration: DurationOption) {
        if (state.value.selectedDurationOption == newDuration) return
        _state.update {
            it.copy(selectedDurationOption = newDuration)
        }
        if (newDuration == DurationOption.CUSTOM) onCustomDurationSelected()
    }

    private fun onCustomDurationSelected() {
        if (state.value.displayedDurations[DurationOption.CUSTOM] == -1)
            viewModelScope.launch { showCustomTimePickerDialog() }
    }

    private suspend fun showCustomTimePickerDialog() {
        shouldShowCustomTimePickerDialogChannel.send(true)
    }
    private suspend fun showExtraTimePickerDialog() {
        shouldShowExtraTimePickerDialogChannel.send(true)
    }


    private fun onAddButtonClick() {
        if (state.value.selectedNumbers.size >= 11) return
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

        updateSelectedNumbers(timerItems)

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

    private fun updateSelectedNumbers(timerItems: List<TimerItem>) {
        val selectedNumbers = timerItems.map { it.selectedNumbers }.flatten()
        val newPossibleNumbers = (1..99).toList().toMutableList()
        newPossibleNumbers.removeAll(selectedNumbers)
        _state.update {
            it.copy(possibleNumbers = newPossibleNumbers)
        }
    }


}