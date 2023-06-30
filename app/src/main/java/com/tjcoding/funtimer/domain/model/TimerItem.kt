package com.tjcoding.funtimer.domain.model

import java.time.LocalDateTime

data class TimerItem(
    val selectedNumbers: List<Int>,
    val time: LocalDateTime,
    ) {
    override fun hashCode(): Int {
        return selectedNumbers[0]
    }
}