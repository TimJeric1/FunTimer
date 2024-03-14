package com.tjcoding.funtimer.presentation.edit_active_timer

import com.tjcoding.funtimer.presentation.timer_setup.DurationOption
import java.util.UUID


sealed interface EditActiveTimerEvent {

    data class OnScreenLaunch(val timerItemId: UUID): EditActiveTimerEvent
    data object OnLeftFilledArrowClick: EditActiveTimerEvent
    data object OnRightFilledArrowClick: EditActiveTimerEvent
    data class OnDurationRadioButtonClick(val duration: DurationOption): EditActiveTimerEvent
    data object OnDurationRadioButtonLongClick: EditActiveTimerEvent
    data object OnAddButtonClick: EditActiveTimerEvent
    data object OnSaveButtonClick: EditActiveTimerEvent

    data class OnSelectedNumberClick(val number: Int): EditActiveTimerEvent

    data class OnCustomDurationPicked(val duration: Int): EditActiveTimerEvent

    data object OnLayoutViewIconClick: EditActiveTimerEvent

    data object OnBackspaceIconClick: EditActiveTimerEvent

    data object OnRestartIconClick: EditActiveTimerEvent
}