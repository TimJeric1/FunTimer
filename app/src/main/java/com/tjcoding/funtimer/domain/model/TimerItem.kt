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

    override fun equals(other: Any?): Boolean {
        val otherTimerItem = (other as TimerItem)
        return (this.time == otherTimerItem.time && this.selectedNumbers == otherTimerItem.selectedNumbers)
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