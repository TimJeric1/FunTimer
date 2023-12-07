package com.tjcoding.funtimer.presentation.timer_setup

sealed class TimerSetupEvent {
    object OnLeftFilledArrowClick: TimerSetupEvent()
    object OnRightFilledArrowClick: TimerSetupEvent()
    data class OnDurationRadioButtonClick(val duration: DurationOption): TimerSetupEvent()
    data class OnDurationRadioButtonLongClick(val index: Int): TimerSetupEvent()
    object OnAddButtonClick: TimerSetupEvent()
    object OnSaveButtonClick: TimerSetupEvent()

    data class OnSelectedNumberClick(val number: Int): TimerSetupEvent()

    data class OnCustomDurationPicked(val duration: Int): TimerSetupEvent()
}