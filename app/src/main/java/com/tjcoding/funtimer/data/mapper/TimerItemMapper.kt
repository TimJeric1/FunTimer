package com.tjcoding.funtimer.data.mapper

import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.data.local.entity.AlarmTriggerTimeEntity
import com.tjcoding.funtimer.domain.model.TimerItem


fun TimerItem.toEntitiesPair(): Pair<AlarmTriggerTimeEntity, List<SelectedNumberEntity>>{
    val timerItemEntity = AlarmTriggerTimeEntity(
        id = this.hashCode(),
        triggerTime = triggerTime,
        alarmTime = alarmTime,
        extraTime = extraTime,
        hasTriggered = hasTriggered
    )
    val selectedNumberEntities = selectedNumbers.map {
        SelectedNumberEntity(
            selectedNumber = it,
            timeItemId = this.hashCode()
        )
    }
    return Pair(timerItemEntity, selectedNumberEntities)
}

fun Pair<AlarmTriggerTimeEntity, List<SelectedNumberEntity>>.toTimerItem(): TimerItem {
    val timerItemEntity = this.first
    val selectedNumberEntities = this.second

    val selectedNumbers = selectedNumberEntities.map { it.selectedNumber }.toList()

    return TimerItem(
        selectedNumbers = selectedNumbers,
        triggerTime = timerItemEntity.triggerTime,
        alarmTime = timerItemEntity.alarmTime,
        extraTime = timerItemEntity.extraTime,
        hasTriggered = timerItemEntity.hasTriggered
    )
}

