package com.tjcoding.funtimer.data.mapper

import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.local.entity.TimeEntity
import com.tjcoding.funtimer.domain.model.TimerItem


fun TimerItem.toEntitiesPair(): Pair<TimeEntity, List<SelectedNumberEntity>>{
    val timerItemEntity = TimeEntity(
        id = this.hashCode(),
        time = time,
    )
    val selectedNumberEntities = selectedNumbers.map {
        SelectedNumberEntity(
            selectedNumber = it,
            timeItemId = this.hashCode()
        )
    }
    return Pair(timerItemEntity, selectedNumberEntities)
}

fun Pair<TimeEntity, List<SelectedNumberEntity>>.toTimerItem(): TimerItem {
    val timerItemEntity = this.first
    val selectedNumberEntities = this.second

    val selectedNumbers = selectedNumberEntities.map { it.selectedNumber }.toList()

    return TimerItem(
        selectedNumbers = selectedNumbers,
        time = timerItemEntity.time
    )
}

