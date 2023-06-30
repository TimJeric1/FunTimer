package com.tjcoding.funtimer.data.mapper

import android.util.Log
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.local.entity.TimerItemEntity
import com.tjcoding.funtimer.domain.model.TimerItem
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun TimerItem.toEntities(): Pair<TimerItemEntity, List<SelectedNumberEntity>>{
    val unixEndTime = time.atZone(ZoneId.systemDefault()).toEpochSecond()
    val timerItemEntity = TimerItemEntity(
        key = this.hashCode(),
        unixEndTime = unixEndTime,
    )
    val selectedNumberEntities: MutableList<SelectedNumberEntity> = ArrayList<SelectedNumberEntity>(100)
    selectedNumbers.forEach{ number ->
        selectedNumberEntities.add(SelectedNumberEntity(
            selectedNumber = number,
            timerItemKey = this.hashCode()
        ))
    }
    return Pair(timerItemEntity, selectedNumberEntities)
}

fun Pair<TimerItemEntity, List<SelectedNumberEntity>>.toTimerItem(): TimerItem {
    val timerItemEntity = this.first
    val selectedNumberEntities = this.second

    val selectedNumbers = selectedNumberEntities.map { it.selectedNumber }.toList()


    return TimerItem(
        selectedNumbers = selectedNumbers,
        time = Instant.ofEpochSecond(timerItemEntity.unixEndTime).atZone(ZoneId.systemDefault()).toLocalDateTime(),
    )
}

