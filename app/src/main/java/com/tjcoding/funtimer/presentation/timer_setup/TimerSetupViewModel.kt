package com.tjcoding.funtimer.presentation.timer_setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.utility.addInOrder
import com.tjcoding.funtimer.service.alarm.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class TimerSetupViewModel @Inject constructor(
    private val repository: TimerRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {


    private var timerItemsFlowCounter = 0
    private val timerItemsFlow = repository.getAllTimerItemsStream()
        .retryWhen {cause, attempt -> return@retryWhen shouldRetry(attempt, cause) }
        .onEach { timerItems -> updateState(timerItems) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(TimerSetupState())
    val state = combine(_state, timerItemsFlow) { state, timerItems -> state }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TimerSetupState())





    fun onEvent(event: TimerSetupEvent) {
        when (event) {
            TimerSetupEvent.onAddButtonClick -> {
                onAddButtonClick()
            }
            is TimerSetupEvent.onDurationRadioButtonClick -> {
                onDurationRadioButtonClick(newDuration = event.duration)
            }
            TimerSetupEvent.onSaveButtonClick -> {
                onSaveButtonClick()
            }
            TimerSetupEvent.onLeftFilledArrowClick -> {
                onLeftFilledArrowClick()
            }
            TimerSetupEvent.onRightFilledArrowClick -> {
                onRightFilledArrowClick()
            }
            is TimerSetupEvent.onSelectedNumberClick -> {
                onSelectedNumberClick(number = event.number)
            }
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
            time = if (timerDuration == 5) LocalDateTime.now()
                .plusSeconds(timerDuration.toLong()) else LocalDateTime.now()
                .plusMinutes(timerDuration.toLong())
        )
        viewModelScope.launch {
            repository.insertTimerItem(timerItem)
        }
        alarmScheduler.schedule(timerItem)
        _state.update {
            it.copy(selectedNumbers = emptyList())
        }
    }

    private fun onDurationRadioButtonClick(newDuration: DurationOption) {
        if (state.value.durationOption == newDuration) return
        _state.update {
            it.copy(durationOption = newDuration)
        }
        if (newDuration == DurationOption.CUSTOM) onCustomDurationSelected()
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

    private fun onCustomDurationSelected() {
        return
    }


    private fun updateState(timerItems: List<TimerItem>) {
        timerItemsFlowCounter++

        val shouldUpdateDisplayedNumber = timerItemsFlowCounter <= 1

        updateSelectedNumbers(timerItems)

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

    private suspend fun shouldRetry(attempt: Long, cause: Throwable): Boolean {
        if (attempt > 3) return false
        val currentDelay = 2000L * attempt
        if (cause is IOException) {
            delay(currentDelay)
            return true
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
    }
}