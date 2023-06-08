package com.tjcoding.funtimer.presentation.timer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: TimerRepository
): ViewModel() {

    var state by mutableStateOf(SetupState())
        private set

    fun onEvent(event: TimerEvent){
        when(event){
            TimerEvent.onAddButtonClick -> {
                val numberToAdd = state.displayedNumber
                onRightFilledArrowClick()
                val newSelectedNumbers = state.selectedNumbers.toMutableList()
                newSelectedNumbers.add(numberToAdd)
                val newPossibleNumbers = state.possibleNumbers.toMutableList()
                newPossibleNumbers.remove(numberToAdd)
                state = state.copy(selectedNumbers = newSelectedNumbers.toList(), possibleNumbers = newPossibleNumbers)

            }
            is TimerEvent.onDurationRadioButtonClick -> {
                if(state.durationOption == event.duration) return
                state = state.copy(durationOption = event.duration)
                if(event.duration == DurationOption.CUSTOM) onCustomDurationSelected()
            }
            TimerEvent.onSaveButtonClick -> {
                val timerDuration = DurationOption.durationOptionToDuration(state.durationOption, state.durations)
                val timerItem = TimerItem(
                    selectedNumbers = state.selectedNumbers,
                    time = timerDuration,
                    unixEndTime = calculateUnixEndTime(duration = timerDuration)
                )
                viewModelScope.launch {
                    repository.insertTimerItem(timerItem)
                }
                state = state.copy(selectedNumbers = emptyList())
            }

            TimerEvent.onLeftFilledArrowClick -> {
                onLeftFilledArrowClick()
            }
            TimerEvent.onRightFilledArrowClick -> {
                onRightFilledArrowClick()
            }
        }


    }

    private fun calculateUnixEndTime(duration: Int): Long{
        val currentTime = System.currentTimeMillis()
        val durationInMillis = (duration * 1000).toLong()
        val unixEndTime = currentTime + durationInMillis
        return  unixEndTime
    }

    private fun onLeftFilledArrowClick() {
        val possibleNumbers = state.possibleNumbers
        val currentNumberIndex = possibleNumbers.indexOf(state.displayedNumber)
        if (currentNumberIndex == 0) return
        val newDisplayedNumber = possibleNumbers.get(currentNumberIndex - 1)
        state = state.copy(displayedNumber = newDisplayedNumber)
    }

    private fun onRightFilledArrowClick() {
        val possibleNumbers = state.possibleNumbers
        val currentNumberIndex = possibleNumbers.indexOf(state.displayedNumber)
        if (currentNumberIndex == possibleNumbers.size - 1) return
        val newDisplayedNumber = possibleNumbers.get(currentNumberIndex + 1)
        state = state.copy(displayedNumber = newDisplayedNumber)
    }

    private fun onCustomDurationSelected() {
        TODO()
    }
}