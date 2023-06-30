package com.tjcoding.funtimer.presentation.timer

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.utility.addInOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

private const val TAG = "TimerViewModel"
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: TimerRepository
): ViewModel() {

    var state by mutableStateOf(SetupState())
        private set


    init {
        updatePossibleNumbers()
    }

    private fun updatePossibleNumbers() {
        viewModelScope.launch {
            val timerItems = repository.getAllTimerItems()
            val selectedNumbers = ArrayList<Int>()
            timerItems.forEach { timerItem ->
                selectedNumbers.addAll(timerItem.selectedNumbers)
            }
            val newPossibleNumbers = state.possibleNumbers.toMutableList()
            newPossibleNumbers.removeAll(selectedNumbers)
            state = state.copy(
                possibleNumbers = newPossibleNumbers,
                displayedNumber = newPossibleNumbers.get(0)
            )
        }
    }

    fun onEvent(event: TimerEvent){
        when(event){
            TimerEvent.onAddButtonClick -> {
                if (state.selectedNumbers.size == 11) return
                val numberToAdd = state.displayedNumber
                onRightFilledArrowClick()
                val newSelectedNumbers = state.selectedNumbers.toMutableList()
                newSelectedNumbers.add(numberToAdd)
                val newPossibleNumbers = state.possibleNumbers.toMutableList()
                newPossibleNumbers.remove(numberToAdd)
                state = state.copy(selectedNumbers = newSelectedNumbers.toList(), possibleNumbers = newPossibleNumbers.toList())

            }
            is TimerEvent.onDurationRadioButtonClick -> {
                if(state.durationOption == event.duration) return
                state = state.copy(durationOption = event.duration)
                if(event.duration == DurationOption.CUSTOM) onCustomDurationSelected()
            }
            TimerEvent.onSaveButtonClick -> {
                Log.d(TAG, "onSaveButtonClick: ")
                if(state.selectedNumbers.isEmpty()) return
                val timerDuration = state.getDuration()
                val timerItem = TimerItem(
                    selectedNumbers = state.selectedNumbers,
                    time = if(timerDuration == 5) LocalDateTime.now().plusSeconds(timerDuration.toLong()) else LocalDateTime.now().plusMinutes(timerDuration.toLong())
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

            is TimerEvent.onSelectedNumberClick -> {
                val newSelectedNumbers = state.selectedNumbers.toMutableList()
                newSelectedNumbers.remove(event.number)
                val newPossibleNumbers = state.possibleNumbers.toMutableList()
                newPossibleNumbers.addInOrder(event.number)
                state = state.copy(selectedNumbers = newSelectedNumbers.toList(), possibleNumbers = newPossibleNumbers.toList())
            }
        }


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
        return
    }
}