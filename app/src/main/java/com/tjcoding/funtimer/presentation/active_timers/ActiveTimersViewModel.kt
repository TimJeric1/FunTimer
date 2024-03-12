package com.tjcoding.funtimer.presentation.active_timers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.service.alarm.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveTimersViewModel @Inject constructor(
    private val repository: TimerRepository,
    private val alarmScheduler: AlarmScheduler
): ViewModel() {


    private val timerItemsStream = repository.getAllActiveTimerItemsStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ActiveTimersState())
    val state = combine(_state, timerItemsStream) { state, timerItems -> state.copy(activeTimerItems = timerItems.map { it.toActiveTimerItem() }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ActiveTimersState())

    private val shouldShowDeleteTimerItemDialogChannel = Channel<Boolean>()
    val shouldShowDeleteTimerItemDialogStream = shouldShowDeleteTimerItemDialogChannel.receiveAsFlow()

    private val selectedTimerItemChannel = Channel<ActiveTimerItem>()
    val selectedTimerItemStream = selectedTimerItemChannel.receiveAsFlow()

    private val shouldNavigateToEditTimerItemScreenChannel = Channel<Int>()
    val shouldNavigateToEditTimerItemScreenStream = shouldNavigateToEditTimerItemScreenChannel.receiveAsFlow()

    fun onEvent(event: ActiveTimersEvent){
        when(event){
            is ActiveTimersEvent.OnXClick -> {
                viewModelScope.launch {
                    selectedTimerItemChannel.send(event.activeTimerItem)
                    shouldShowDeleteTimerItemDialogChannel.send(true)
                }
            }

            is ActiveTimersEvent.OnEditClick -> {
                viewModelScope.launch {
                    shouldNavigateToEditTimerItemScreenChannel.send(event.activeTimerItem.toTimerItem().hashCode())
                }
            }

            is ActiveTimersEvent.OnAlertDialogDeleteClick -> {
                viewModelScope.launch {
                    repository.deleteTimerItem(event.activeTimerItem.toTimerItem())
                }
                alarmScheduler.cancel(event.activeTimerItem.toTimerItem())
            }
        }
    }



}