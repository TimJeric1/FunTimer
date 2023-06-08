package com.tjcoding.funtimer.data.mapper

import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.local.entity.TimerItemEntity
import com.tjcoding.funtimer.domain.model.TimerItem

fun TimerItem.toEntities(): Pair<TimerItemEntity, List<SelectedNumberEntity>>{
    val timerItemEntity = TimerItemEntity(
        time = time,
        unixEndTime = unixEndTime,
    )
    val selectedNumberEntities: MutableList<SelectedNumberEntity> = ArrayList<SelectedNumberEntity>(100)
    selectedNumbers.forEach{ number ->
        selectedNumberEntities.add(SelectedNumberEntity(
            selectedNumber = number,
            unixEndTime = unixEndTime
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
        time = timerItemEntity.time,
        unixEndTime = timerItemEntity.unixEndTime
    )
}