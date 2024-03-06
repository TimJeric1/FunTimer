package com.tjcoding.funtimer.presentation.active_timers

import com.tjcoding.funtimer.data.repository.FakeTimerRepository
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.service.alarm.FakeAlarmScheduler
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class ActiveTimersViewModelTest {

    private lateinit var testCoroutineExtension: TestScope

    private lateinit var viewModel: ActiveTimersViewModel
    private lateinit var fakeTimerRepository: FakeTimerRepository
    private lateinit var fakeAlarmScheduler: FakeAlarmScheduler
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        Dispatchers.setMain(StandardTestDispatcher())
        testCoroutineExtension = TestScope()
        fakeTimerRepository = FakeTimerRepository()
        fakeAlarmScheduler = FakeAlarmScheduler()
        viewModel = ActiveTimersViewModel(repository = fakeTimerRepository, alarmScheduler = fakeAlarmScheduler)
    }
    @Test
    fun `verify initial state`() {
        assertEquals(ActiveTimersState(), viewModel.state.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    @Test
    fun `verify state after loading triggered timer items`() = runTest {
        // Assuming you have some triggered timer items in your fake repository
        val timerItems = listOf(
            TimerItem(
                selectedNumbers = listOf(1, 2, 3),
                triggerTime = LocalDateTime.now(), // You should replace this with an actual LocalDateTime
                alarmTime = 30,
                extraTime = 5,
                hasTriggered = false
            ),
            TimerItem(
                selectedNumbers = listOf(4, 5, 6),
                triggerTime = LocalDateTime.now(), // You should replace this with an actual LocalDateTime
                alarmTime = 30,
                extraTime = 10,
                hasTriggered = false
            ),
            TimerItem(
                selectedNumbers = listOf(7, 8, 9),
                triggerTime = LocalDateTime.now(), // You should replace this with an actual LocalDateTime
                alarmTime = 30,
                extraTime = 15,
                hasTriggered = true
            )
        )

        // Insert timer items into the repository
        timerItems.forEach {
            fakeTimerRepository.insertTimerItem(it)
        }

        // Wait for the ViewModel's state to update
        viewModel.state.first()

        // Assert the state
        assertEquals(
            timerItems.filter { !it.hasTriggered }.map { it.toActiveTimerItem() },
            viewModel.state.value.activeTimerItems
        )
    }

    // Add more test cases as needed based on your application logic
}
