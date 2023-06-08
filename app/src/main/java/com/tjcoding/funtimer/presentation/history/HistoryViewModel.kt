package com.tjcoding.funtimer.presentation.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
class HistoryViewModel @Inject constructor(
    private val repository: TimerRepository
): ViewModel() {


    var timerItems: List<TimerItem> by mutableStateOf(emptyList())
    init {
        onEvent(HistoryEvent.loadTimerItems)
    }
    fun onEvent(event: HistoryEvent){
        when(event){
            HistoryEvent.loadTimerItems -> {
                viewModelScope.launch {
                    val newTimerItems = repository.getAllTimerItems()
                    timerItems = newTimerItems
                }
            }
            is HistoryEvent.onCardLongClick -> {
                viewModelScope.launch {
                    repository.deleteTimerItem(event.timerItem)
                }
                val newTimerItems = timerItems.toMutableList()
                newTimerItems.remove(event.timerItem)
                timerItems = newTimerItems.toList()
            }
        }
    }

}