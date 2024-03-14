package com.tjcoding.funtimer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.UUID

@Parcelize
data class TimerItem(
    val id: UUID,
    val selectedNumbers: List<Int>,
    val triggerTime: LocalDateTime,
    val alarmTime: Int,
    val extraTime: Int,
    val hasTriggered: Boolean
) : Parcelable {

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is TimerItem) return false
        val otherItem = other
        return id == otherItem.id &&
                selectedNumbers == otherItem.selectedNumbers &&
                triggerTime == otherItem.triggerTime &&
                alarmTime == otherItem.alarmTime &&
                extraTime == otherItem.extraTime &&
                hasTriggered == otherItem.hasTriggered
    }
}


fun TimerItem.toMessage(): String {
    var message = ""
    for (number in selectedNumbers) {
        message += "$number, "
    }
    if (message != "") message.dropLast(2)
    return message
}

