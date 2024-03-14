package com.tjcoding.funtimer.data.mapper

import com.tjcoding.funtimer.data.local.entity.AlarmTriggerTimeEntity
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.domain.model.TimerItem
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID

class TimerItemMapperKtTest {

    @Test
    fun `toEntitiesPair should convert TimerItem to Pair of entities`() {
        // Arrange
        val timerItem = TimerItem(
            id = UUID.randomUUID(),
            selectedNumbers = listOf(1, 2, 3),
            triggerTime = LocalDateTime.now(),
            extraTime = 5,
            alarmTime = 30,
            hasTriggered = false
        )

        // Act
        val entitiesPair = timerItem.toEntitiesPair()

        // Assert
        assertEquals(timerItem.id, entitiesPair.first.id)
        assertEquals(timerItem.triggerTime, entitiesPair.first.triggerTime)
        assertEquals(timerItem.extraTime, entitiesPair.first.extraTime)
        assertEquals(timerItem.hasTriggered, entitiesPair.first.hasTriggered)

        assertEquals(timerItem.selectedNumbers.size, entitiesPair.second.size)
        for (i in timerItem.selectedNumbers.indices) {
            assertEquals(timerItem.selectedNumbers[i], entitiesPair.second[i].selectedNumber)
            assertEquals(timerItem.id, entitiesPair.second[i].alarmTriggerTimeEntityId)
        }
    }

    @Test
    fun `toTimerItem should convert Pair of entities to TimerItem`() {
        // Arrange
        val triggerTime = LocalDateTime.now()
        val id = UUID.randomUUID()
        val entitiesPair = Pair(
            AlarmTriggerTimeEntity(id, triggerTime,30, 10, false),
            listOf(
                SelectedNumberEntity(selectedNumber = 1, alarmTriggerTimeEntityId = id),
                SelectedNumberEntity(selectedNumber = 2, alarmTriggerTimeEntityId = id),
                SelectedNumberEntity(selectedNumber = 3, alarmTriggerTimeEntityId = id)
            )
        )

        // Act
        val timerItem = entitiesPair.toTimerItem()

        // Assert
        assertEquals(entitiesPair.first.id, timerItem.id)
        assertEquals(entitiesPair.first.triggerTime, timerItem.triggerTime)
        assertEquals(entitiesPair.first.extraTime, timerItem.extraTime)
        assertEquals(entitiesPair.first.hasTriggered, timerItem.hasTriggered)

        assertEquals(entitiesPair.second.size, timerItem.selectedNumbers.size)
        for (i in entitiesPair.second.indices) {
            assertEquals(entitiesPair.second[i].selectedNumber, timerItem.selectedNumbers[i])
        }
    }
}