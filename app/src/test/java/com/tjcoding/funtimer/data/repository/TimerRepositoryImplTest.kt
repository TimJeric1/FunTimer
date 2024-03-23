package com.tjcoding.funtimer.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tjcoding.funtimer.data.dao.FakeTimerDao
import com.tjcoding.funtimer.data.local.entity.AlarmTriggerTimeEntity
import com.tjcoding.funtimer.data.local.entity.SelectedNumberEntity
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.utility.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class TimerRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var timerRepository: TimerRepositoryImpl

    private val alarmTriggerTimeEntity = AlarmTriggerTimeEntity(
        id = UUID.randomUUID(),
        triggerTime = LocalDateTime.now(),
        alarmTime = 10,
        extraTime = 5,
        hasTriggered = false
    )

    private val selectedNumberEntity = SelectedNumberEntity(
        alarmTriggerTimeEntityId = alarmTriggerTimeEntity.id,
        alarmTriggerTimeEntitySelectedNumberId = 1,
        selectedNumber = 5
    )

    private val timerItem = TimerItem(
        id = alarmTriggerTimeEntity.id,
        triggerTime = alarmTriggerTimeEntity.triggerTime,
        alarmTime = alarmTriggerTimeEntity.alarmTime,
        extraTime = alarmTriggerTimeEntity.extraTime,
        hasTriggered = alarmTriggerTimeEntity.hasTriggered,
        selectedNumbers = listOf(selectedNumberEntity.selectedNumber)
    )

    private val triggeredTimerItem = TimerItem(
        id = alarmTriggerTimeEntity.id,
        triggerTime = alarmTriggerTimeEntity.triggerTime,
        alarmTime = alarmTriggerTimeEntity.alarmTime,
        extraTime = alarmTriggerTimeEntity.extraTime,
        hasTriggered = !alarmTriggerTimeEntity.hasTriggered,
        selectedNumbers = listOf(selectedNumberEntity.selectedNumber)
    )

    @Before
    fun setUp() {
        val fakeTimerDao = FakeTimerDao()
        timerRepository = TimerRepositoryImpl(
            timerDao = fakeTimerDao
        )
    }

    @Test
    fun `test getAllTriggeredTimerItemsStream`() = runTest {
        val expected = listOf(triggeredTimerItem)
        timerRepository.insertTimerItem(triggeredTimerItem)
        val actual = timerRepository.getAllTriggeredTimerItemsStream().first()
        assertEquals(expected, actual)
    }

    @Test
    fun `test getAllActiveTimerItemsStream`() = runTest {
        val expected = listOf(timerItem)
        timerRepository.insertTimerItem(timerItem)
        val actual = timerRepository.getAllActiveTimerItemsStream().first()
        assertEquals(expected, actual)
    }

    @Test
    fun `test insertTimerItem`() = runTest {
        timerRepository.insertTimerItem(timerItem)
        val actual = timerRepository.getTimerItemById(timerItem.id)
        assertEquals(timerItem, actual)
    }

    @Test
    fun `test updateTimerItem`() = runTest {
        timerRepository.insertTimerItem(timerItem)
        val updatedTimerItem = timerItem.copy(alarmTime = 15)
        timerRepository.updateTimerItem(timerItem, updatedTimerItem)
        val actual = timerRepository.getTimerItemById(timerItem.id)
        assertEquals(updatedTimerItem, actual)
    }

    @Test
    fun `test deleteTimerItem`() = runTest {
        timerRepository.insertTimerItem(timerItem)
        timerRepository.deleteTimerItem(timerItem)
        val actual = timerRepository.getTimerItemById(timerItem.id)
        assertEquals(null, actual)
    }

    @Test
    fun `test deleteAll`() = runTest {
        timerRepository.insertTimerItem(timerItem)
        timerRepository.deleteAll()
        val actual = timerRepository.getAllTriggeredTimerItemsStream().first()
        assertEquals(emptyList<TimerItem>(), actual)
    }

    @Test
    fun `test getTimerItemById`() = runTest {
        timerRepository.insertTimerItem(timerItem)
        val actual = timerRepository.getTimerItemById(timerItem.id)
        assertEquals(timerItem, actual)
    }
}
