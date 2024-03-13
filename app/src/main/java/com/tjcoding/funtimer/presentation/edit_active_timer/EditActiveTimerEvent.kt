package com.tjcoding.funtimer.presentation.edit_active_timer

sealed interface EditActiveTimerEvent {
    data object OnLeftFilledArrowClick: EditActiveTimerEvent
    data object OnRightFilledArrowClick: EditActiveTimerEvent
    data class OnDurationRadioButtonClick(val duration: DurationOption): EditActiveTimerEvent
    data object OnDurationRadioButtonLongClick: EditActiveTimerEvent
    data object OnAddButtonClick: EditActiveTimerEvent
    data object OnSaveButtonClick: EditActiveTimerEvent

    data class OnSelectedNumberClick(val number: Int): EditActiveTimerEvent

    data class OnCustomDurationPicked(val duration: Int): EditActiveTimerEvent

    data class OnExtraTimePicked(val extraTime: Int): EditActiveTimerEvent

    data object OnLayoutViewIconClick: EditActiveTimerEvent

    data object OnExtraTimeIconClick: EditActiveTimerEvent
}