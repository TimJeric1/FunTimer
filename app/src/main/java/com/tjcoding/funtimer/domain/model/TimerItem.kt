package com.tjcoding.funtimer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class TimerItem(
    val selectedNumbers: List<Int>,
    val alarmTime: LocalDateTime,
    val extraTime: Int

    ) : Parcelable {
    override fun hashCode(): Int {
        return selectedNumbers[0]
    }

    override fun equals(other: Any?): Boolean {
        val otherTimerItem = (other as TimerItem)
        return (this.alarmTime == otherTimerItem.alarmTime && this.selectedNumbers == otherTimerItem.selectedNumbers)
    }


    fun getAlarmTriggerTime(): LocalDateTime {
       return alarmTime.plusMinutes(extraTime.toLong())
    }

    fun getAlarmTriggerTimeDebug(): LocalDateTime {
        return alarmTime.plusSeconds(extraTime.toLong())
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

