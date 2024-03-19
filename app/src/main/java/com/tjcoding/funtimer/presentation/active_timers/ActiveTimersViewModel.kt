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
) : ViewModel() {


    private val timerItemsStream = repository.getAllActiveTimerItemsStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ActiveTimersState())
    val state = combine(
        _state,
        timerItemsStream
    ) { state, timerItems -> state.copy(activeTimerItems = timerItems.map { it.toActiveTimerItem() }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ActiveTimersState())

    private val shouldShowDeleteTimerItemDialogChannel = Channel<Boolean>()
    val shouldShowDeleteTimerItemDialogStream =
        shouldShowDeleteTimerItemDialogChannel.receiveAsFlow()

    private val selectedActiveTimerItemChannel = Channel<ActiveTimerItem>()
    val selectedActiveTimerItemStream = selectedActiveTimerItemChannel.receiveAsFlow()

    private val shouldNavigateToEditTimerItemScreenChannel = Channel<Boolean>()
    val shouldNavigateToEditTimerItemScreenStream =
        shouldNavigateToEditTimerItemScreenChannel.receiveAsFlow()


    fun onEvent(event: ActiveTimersEvent) {
        when (event) {
            is ActiveTimersEvent.OnXIconClick -> {
                onXIconClick(event.activeTimerItem)
            }

            is ActiveTimersEvent.OnEditIconClick -> {
                onEditIconClick(event.activeTimerItem)
            }

            is ActiveTimersEvent.OnAlertDialogDeleteClick -> {
                onAlertDialogDeleteClick(event.activeTimerItem)
            }
        }
    }

    private fun onXIconClick(activeTimerItem: ActiveTimerItem) {
        viewModelScope.launch {
            selectedActiveTimerItemChannel.send(activeTimerItem)
            shouldShowDeleteTimerItemDialogChannel.send(true)
        }
    }

    private fun onEditIconClick(activeTimerItem: ActiveTimerItem) {
        viewModelScope.launch {
            selectedActiveTimerItemChannel.send(activeTimerItem)
            shouldNavigateToEditTimerItemScreenChannel.send(true)
        }
    }

    private fun onAlertDialogDeleteClick(activeTimerItem: ActiveTimerItem) {
        viewModelScope.launch {
            repository.deleteTimerItem(activeTimerItem.toTimerItem())
            alarmScheduler.cancelAlarm(activeTimerItem.toTimerItem())
        }


    }


}