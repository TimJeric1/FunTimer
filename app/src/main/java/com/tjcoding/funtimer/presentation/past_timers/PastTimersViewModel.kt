package com.tjcoding.funtimer.presentation.past_timers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tjcoding.funtimer.domain.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PastTimersViewModel @Inject constructor(
    repository: TimerRepository,
) : ViewModel() {


    private val timerItemsStream = repository.getAllTriggeredTimerItemsStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(PastTimersState())
    val state = combine(
        _state,
        timerItemsStream
    ) { state, timerItems ->
        state.copy(pastTimerItems = timerItems.map { it.toPastTimerItem() }
            .sortedByDescending { pastTimerItem -> pastTimerItem.triggerTime })
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PastTimersState())


}
