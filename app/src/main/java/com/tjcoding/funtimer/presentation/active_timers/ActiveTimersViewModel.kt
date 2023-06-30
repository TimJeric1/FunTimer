package com.tjcoding.funtimer.presentation.active_timers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.service.alarm.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HistoryViewModel"
@HiltViewModel
class ActiveTimersViewModel @Inject constructor(
    private val repository: TimerRepository,
    private val alarmScheduler: AlarmScheduler
): ViewModel() {


    private val timerItemsFlow = repository.getAllTimerItemsStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _historyState = MutableStateFlow(ActiveTimersState())
    val historyState = combine(_historyState, timerItemsFlow) { historyState, timerItems -> historyState.copy(timerItems = timerItems) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ActiveTimersState())

    fun onEvent(event: ActiveTimersEvent){
        when(event){
            is ActiveTimersEvent.onCardLongClick -> {
                viewModelScope.launch {
                    repository.deleteTimerItem(event.timerItem)
                    alarmScheduler.cancel(event.timerItem)
                }
            }
        }
    }



}