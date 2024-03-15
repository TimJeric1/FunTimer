package com.tjcoding.funtimer.presentation.timer_setup

import com.tjcoding.funtimer.presentation.common.DurationOption

sealed interface TimerSetupEvent {
    data object OnLeftFilledArrowClick: TimerSetupEvent
    data object OnRightFilledArrowClick: TimerSetupEvent
    data class OnDurationRadioButtonClick(val duration: DurationOption): TimerSetupEvent
    data object OnDurationRadioButtonLongClick: TimerSetupEvent
    data object OnAddButtonClick: TimerSetupEvent
    data object OnSaveButtonClick: TimerSetupEvent

    data class OnSelectedNumberClick(val number: Int): TimerSetupEvent

    data class OnCustomDurationPicked(val duration: Int): TimerSetupEvent

    data class OnExtraTimePicked(val extraTime: Int): TimerSetupEvent

    data object OnLayoutViewIconClick: TimerSetupEvent

    data object OnExtraTimeIconClick: TimerSetupEvent

    data object OnBackspaceIconClick: TimerSetupEvent

    data object OnRestartIconClick: TimerSetupEvent
}