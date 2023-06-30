package com.tjcoding.funtimer.presentation.history

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

private const val TAG = "HistoryViewModel"
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

    fun getDuration(time: LocalDateTime): String {
        val unixEndTime = time.atZone(ZoneId.systemDefault()).toEpochSecond()
        val duration =  unixEndTime - (System.currentTimeMillis() /1000)
        val localTime = if(duration >= 0) LocalTime.ofSecondOfDay(duration) else LocalTime.ofSecondOfDay(0)
        return localTime.format(DateTimeFormatter.ofPattern("mm:ss"))
    }

}