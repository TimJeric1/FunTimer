package com.tjcoding.funtimer.presentation.timer

sealed class TimerEvent {
    object onLeftFilledArrowClick: TimerEvent()
    object onRightFilledArrowClick: TimerEvent()
    data class onDurationRadioButtonClick(val duration: DurationOption): TimerEvent()
    object onAddButtonClick: TimerEvent()
    object onSaveButtonClick: TimerEvent()

    data class onSelectedNumberClick(val number: Int): TimerEvent()
}