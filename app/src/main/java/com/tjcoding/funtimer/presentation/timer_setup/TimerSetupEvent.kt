package com.tjcoding.funtimer.presentation.timer_setup

sealed class TimerSetupEvent {
    object onLeftFilledArrowClick: TimerSetupEvent()
    object onRightFilledArrowClick: TimerSetupEvent()
    data class onDurationRadioButtonClick(val duration: DurationOption): TimerSetupEvent()
    object onAddButtonClick: TimerSetupEvent()
    object onSaveButtonClick: TimerSetupEvent()

    data class onSelectedNumberClick(val number: Int): TimerSetupEvent()
}