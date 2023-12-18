package com.tjcoding.funtimer.presentation.timer_setup

sealed interface TimerSetupEvent {
    data object OnLeftFilledArrowClick: TimerSetupEvent
    data object OnRightFilledArrowClick: TimerSetupEvent
    data class OnDurationRadioButtonClick(val duration: DurationOption): TimerSetupEvent
    data class OnDurationRadioButtonLongClick(val index: Int): TimerSetupEvent
    data object OnAddButtonClick: TimerSetupEvent
    data object OnSaveButtonClick: TimerSetupEvent

    data class OnSelectedNumberClick(val number: Int): TimerSetupEvent

    data class OnCustomDurationPicked(val duration: Int): TimerSetupEvent
}