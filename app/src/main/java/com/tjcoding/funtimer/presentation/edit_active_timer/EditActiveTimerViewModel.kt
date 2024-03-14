package com.tjcoding.funtimer.presentation.edit_active_timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tjcoding.funtimer.BuildConfig
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.domain.repository.UserPreferencesRepository
import com.tjcoding.funtimer.presentation.active_timers.toActiveTimerItem
import com.tjcoding.funtimer.presentation.timer_setup.DurationOption
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
import javax.inject.Inject


@HiltViewModel
class EditActiveTimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val alarmScheduler: AlarmScheduler,
) : ViewModel() {


    private var timerItemsStreamCounter = 0
    private val timerItemsStream = timerRepository.getAllActiveTimerItemsStream()
        .onEach { updateState(it) }
        // it has to be .statein otherwise it won't replay the last value on back navigation
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val userPreferencesStream = userPreferencesRepository.userPreferencesStream

    private val _state = MutableStateFlow(EditActiveTimerState())

    // it has to combine timerItemStream in order for the timerItemsStream to have a collector
    val state =
        combine(_state, timerItemsStream, userPreferencesStream) { state, _, userPreferences ->
            state.copy()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EditActiveTimerState())

    private val shouldShowCustomTimePickerDialogChannel = Channel<Boolean>()
    val shouldShowCustomTimePickerDialogStream =
        shouldShowCustomTimePickerDialogChannel.receiveAsFlow()


    fun onEvent(event: EditActiveTimerEvent) {
        when (event) {
            EditActiveTimerEvent.OnAddButtonClick -> {
                onAddButtonClick()
            }

            is EditActiveTimerEvent.OnDurationRadioButtonClick -> {
                onDurationRadioButtonClick(event.duration)
            }

            EditActiveTimerEvent.OnSaveButtonClick -> {
                onSaveButtonClick()
            }

            EditActiveTimerEvent.OnLeftFilledArrowClick -> {
                onLeftFilledArrowClick()
            }

            EditActiveTimerEvent.OnRightFilledArrowClick -> {
                onRightFilledArrowClick()
            }

            is EditActiveTimerEvent.OnSelectedNumberClick -> {
                onSelectedNumberClick(event.number)
            }

            is EditActiveTimerEvent.OnCustomDurationPicked -> {
                onCustomDurationPicked(event.duration)
            }

            EditActiveTimerEvent.OnDurationRadioButtonLongClick -> {
                onDurationRadioButtonLongClick()
            }

            EditActiveTimerEvent.OnLayoutViewIconClick -> {
                onLayoutViewButtonClick()
            }

            EditActiveTimerEvent.OnRestartIconClick -> {
                onRestartIconClick()
            }

            EditActiveTimerEvent.OnBackspaceIconClick -> {
                onBackspaceIconClick()
            }

            is EditActiveTimerEvent.OnScreenLaunch -> {
                onScreenLaunch(event.timerItemId)
            }
        }


    }

    private fun onScreenLaunch(timerItemId: Int) {
        viewModelScope.launch {
            val timerItem = timerRepository.getTimerItemById(timerItemId)
            _state.update {
                it.copy(
                    originalTimerItem = timerItem.toActiveTimerItem(),
                    editedTimerItem = timerItem.toActiveTimerItem(),
                )
            }
        }
    }


    private fun onRestartIconClick() {
        val editedTimerItem = state.value.editedTimerItem
        val originalTimerItem = state.value.originalTimerItem
        val newPossibleNumbers = state.value.possibleNumbers.toMutableList()
        for (number in editedTimerItem.selectedNumbers) newPossibleNumbers.addInOrder(number)
        newPossibleNumbers.removeAll(originalTimerItem.selectedNumbers)
        _state.update {
            it.copy(
                displayedNumber = newPossibleNumbers.first(),
                editedTimerItem = it.originalTimerItem,
                possibleNumbers = newPossibleNumbers.toList(),
                selectedDurationOption = DurationOption.indexToDurationOption(0)
            )
        }
    }

    private fun onBackspaceIconClick() {
        val editedTimerItem = state.value.editedTimerItem
        val newSelectedNumbers = editedTimerItem.selectedNumbers.toMutableList()
        val removedNumber = newSelectedNumbers.removeLast()
        val newPossibleNumbers = state.value.possibleNumbers.toMutableList()
        newPossibleNumbers.addInOrder(removedNumber)
        _state.update {
            it.copy(
                editedTimerItem = editedTimerItem.copy(selectedNumbers = newSelectedNumbers.toList()),
                possibleNumbers = newPossibleNumbers.toList()
            )
        }
    }


    private fun onLayoutViewButtonClick() {
        viewModelScope.launch {
        }
    }

    private fun onDurationRadioButtonLongClick() {
        viewModelScope.launch { showCustomTimePickerDialog() }
    }

    private fun onCustomDurationPicked(duration: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateSelectedCustomDurations(
                selectedCustomDuration = duration,
                index = state.value.selectedDurationOption.toIndex()
            )
        }
    }

    private fun onSelectedNumberClick(number: Int) {
        val editedTimerItem = state.value.editedTimerItem
        val newSelectedNumbers = editedTimerItem.selectedNumbers.toMutableList()
        newSelectedNumbers.remove(number)
        val newPossibleNumbers = state.value.possibleNumbers.toMutableList()
        newPossibleNumbers.addInOrder(number)
        _state.update {
            it.copy(
                editedTimerItem = editedTimerItem.copy(selectedNumbers = newSelectedNumbers),
                possibleNumbers = newPossibleNumbers.toList()
            )
        }
    }

    private fun onSaveButtonClick() {
        val editedTimerItem = state.value.editedTimerItem
        if (editedTimerItem.selectedNumbers.isEmpty()) return

        val isInDebugMode = BuildConfig.DEBUG
        val timerDuration = state.value.getDuration()
        // TODO: update originalTimerItem to editedTimerItem
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


    private fun onAddButtonClick() {
        val editedTimerItem = state.value.editedTimerItem
        if (editedTimerItem.selectedNumbers.size >= 11) return
        val numberToAdd = state.value.displayedNumber
        onRightFilledArrowClick()
        val newSelectedNumbers = editedTimerItem.selectedNumbers.toMutableList()
        newSelectedNumbers.addInOrder(numberToAdd)
        val newPossibleNumbers = state.value.possibleNumbers.toMutableList()
        newPossibleNumbers.remove(numberToAdd)
        _state.update {
            it.copy(
                editedTimerItem = editedTimerItem.copy(selectedNumbers = newSelectedNumbers.toList()),
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
        val newDisplayedNumber = _state.value.possibleNumbers[0]
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


}