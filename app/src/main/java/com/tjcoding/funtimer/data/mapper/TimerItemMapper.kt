package com.tjcoding.funtimer.data.mapper

import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.local.entity.TriggerTimeEntity
import com.tjcoding.funtimer.domain.model.TimerItem


fun TimerItem.toEntitiesPair(): Pair<TriggerTimeEntity, List<SelectedNumberEntity>>{
    val timerItemEntity = TriggerTimeEntity(
        id = this.hashCode(),
        triggerTime = alarmTriggerTime,
    )
    val selectedNumberEntities = selectedNumbers.map {
        SelectedNumberEntity(
            selectedNumber = it,
            timeItemId = this.hashCode()
        )
    }
    return Pair(timerItemEntity, selectedNumberEntities)
}

fun Pair<TriggerTimeEntity, List<SelectedNumberEntity>>.toTimerItem(): TimerItem {
    val timerItemEntity = this.first
    val selectedNumberEntities = this.second

    val selectedNumbers = selectedNumberEntities.map { it.selectedNumber }.toList()

    return TimerItem(
        selectedNumbers = selectedNumbers,
        alarmTriggerTime = timerItemEntity.triggerTime
    )
}

