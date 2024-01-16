package com.tjcoding.funtimer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.ZoneId

@Parcelize
data class TimerItem(
    val selectedNumbers: List<Int>,
    val alarmTime: LocalDateTime,
    val extraTime: Int,
    val hasTriggered: Boolean
    ) : Parcelable {
    override fun hashCode(): Int {
        return alarmTime.atZone(ZoneId.systemDefault()).toEpochSecond().toInt() + extraTime
    }

    override fun equals(other: Any?): Boolean {
        return this.hashCode() == other.hashCode()
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

