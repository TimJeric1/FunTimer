package com.tjcoding.funtimer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.ZoneId

@Parcelize
data class TimerItem(
    val selectedNumbers: List<Int>,
    val triggerTime: LocalDateTime,
    val alarmTime: Int,
    val extraTime: Int,
    val hasTriggered: Boolean
    ) : Parcelable {
    override fun hashCode(): Int {
        return triggerTime.atZone(ZoneId.systemDefault()).toEpochSecond().toInt() + extraTime
    }

    override fun equals(other: Any?): Boolean {
        return this.hashCode() == other.hashCode()
    }
}

fun TimerItem.toMessage(): String {
    var message = ""
    for(number in selectedNumbers) {
        message += "$number, "
    }
    if(message != "") message.dropLast(2)
    return message
}

