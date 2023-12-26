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

@HiltViewModel
class ActiveTimersViewModel @Inject constructor(
    private val repository: TimerRepository,
    private val alarmScheduler: AlarmScheduler
): ViewModel() {


    private val timerItemsStream = repository.getAllNotTriggeredTimerItemsStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ActiveTimersState())
    val state = combine(_state, timerItemsStream) { state, timerItems -> state.copy(activeTimerItemsUi = timerItems.map { it.toActiveTimerItemUi() }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ActiveTimersState())

    fun onEvent(event: ActiveTimersEvent){
        when(event){
            is ActiveTimersEvent.OnCardLongClick -> {
                viewModelScope.launch {
                    repository.deleteTimerItem(event.activeTimerItemUi.toTimerItem())
                }
                alarmScheduler.cancel(event.activeTimerItemUi.toTimerItem())
            }
        }
    }



}