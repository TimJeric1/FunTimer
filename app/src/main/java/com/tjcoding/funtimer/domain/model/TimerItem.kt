package com.tjcoding.funtimer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class TimerItem(
    val selectedNumbers: List<Int>,
    val time: LocalDateTime,
    ) : Parcelable {
    override fun hashCode(): Int {
        return selectedNumbers[0]
    }
}