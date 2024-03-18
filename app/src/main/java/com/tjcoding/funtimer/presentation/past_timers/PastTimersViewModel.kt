package com.tjcoding.funtimer.presentation.past_timers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.utility.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PastTimersViewModel @Inject constructor(
    repository: TimerRepository,
): ViewModel() {


    private val timerItemsStream = repository.getAllTriggeredTimerItemsStream()
        .catch { cause: Throwable -> handleError(cause, "Couldn't retrieve data") }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(PastTimersState())
    val state = combine(
        _state,
        timerItemsStream
    ) { state, timerItems -> state.copy(pastTimerItems = timerItems.map { it.toPastTimerItem() }.sortedByDescending {pastTimerItem ->  pastTimerItem.triggerTime}) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PastTimersState())

    private val shouldShowSnackbarWithTextChannel = Channel<String>()
    val shouldShowSnackbarWithTextStream =
        shouldShowSnackbarWithTextChannel.receiveAsFlow()

    private fun handleError(cause: Throwable, extraContext: String) = viewModelScope.launch {
        val errorMsg = Util.getErrorMessage(cause, extraContext)
        shouldShowSnackbarWithTextChannel.send(errorMsg)
    }

}
