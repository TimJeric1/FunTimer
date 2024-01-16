package com.tjcoding.funtimer.data.mapper

import com.tjcoding.funtimer.data.local.entity.AlarmTriggerTimeEntity
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.domain.model.TimerItem
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class TimerItemMapperKtTest {

    @Test
    fun `toEntitiesPair should convert TimerItem to Pair of entities`() {
        // Arrange
        val timerItem = TimerItem(
            selectedNumbers = listOf(1, 2, 3),
            alarmTime = LocalDateTime.now(),
            extraTime = 5,
            hasTriggered = false
        )

        // Act
        val entitiesPair = timerItem.toEntitiesPair()

        // Assert
        assertEquals(timerItem.hashCode(), entitiesPair.first.id)
        assertEquals(timerItem.alarmTime, entitiesPair.first.alarmTime)
        assertEquals(timerItem.extraTime, entitiesPair.first.extraTime)
        assertEquals(timerItem.hasTriggered, entitiesPair.first.hasTriggered)

        assertEquals(timerItem.selectedNumbers.size, entitiesPair.second.size)
        for (i in timerItem.selectedNumbers.indices) {
            assertEquals(timerItem.selectedNumbers[i], entitiesPair.second[i].selectedNumber)
            assertEquals(timerItem.hashCode(), entitiesPair.second[i].timeItemId)
        }
    }

    @Test
    fun `toTimerItem should convert Pair of entities to TimerItem`() {
        // Arrange
        val alarmTime = LocalDateTime.now()
        val entitiesPair = Pair(
            AlarmTriggerTimeEntity(1, alarmTime, 10, false),
            listOf(
                SelectedNumberEntity(selectedNumber = 1, timeItemId = 1),
                SelectedNumberEntity(selectedNumber = 2, timeItemId = 1),
                SelectedNumberEntity(selectedNumber = 3, timeItemId = 1)
            )
        )

        // Act
        val timerItem = entitiesPair.toTimerItem()

        // Assert
        assertEquals(entitiesPair.first.alarmTime, timerItem.alarmTime)
        assertEquals(entitiesPair.first.extraTime, timerItem.extraTime)
        assertEquals(entitiesPair.first.hasTriggered, timerItem.hasTriggered)

        assertEquals(entitiesPair.second.size, timerItem.selectedNumbers.size)
        for (i in entitiesPair.second.indices) {
            assertEquals(entitiesPair.second[i].selectedNumber, timerItem.selectedNumbers[i])
        }
    }
}