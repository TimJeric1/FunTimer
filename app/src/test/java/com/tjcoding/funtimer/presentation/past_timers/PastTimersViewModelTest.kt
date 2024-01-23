import com.tjcoding.funtimer.data.repository.FakeTimerRepository
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.presentation.past_timers.PastTimersState
import com.tjcoding.funtimer.presentation.past_timers.PastTimersViewModel
import com.tjcoding.funtimer.presentation.past_timers.toPastTimerItem
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class PastTimersViewModelTest {

    private lateinit var testCoroutineExtension: TestScope

    private lateinit var viewModel: PastTimersViewModel
    private lateinit var fakeTimerRepository: FakeTimerRepository
    @Before
    fun setup(){
        testCoroutineExtension = TestScope()
        fakeTimerRepository = FakeTimerRepository()
        viewModel = PastTimersViewModel(repository = fakeTimerRepository)
    }
    @Test
    fun `verify initial state`() {
        assertEquals(PastTimersState(), viewModel.state.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    @Test
    fun `verify state after loading triggered timer items`() = testCoroutineExtension.runTest {
        // Assuming you have some triggered timer items in your fake repository
        val timerItems = listOf(
            TimerItem(
                selectedNumbers = listOf(1, 2, 3),
                triggerTime = LocalDateTime.now(),// You should replace this with an actual LocalDateTime
                alarmTime = 30,
                extraTime = 5,
                hasTriggered = true
            ),
            TimerItem(
                selectedNumbers = listOf(4, 5, 6),
                triggerTime = LocalDateTime.now(), // You should replace this with an actual LocalDateTime
                alarmTime = 30,
                extraTime = 10,
                hasTriggered = true
            ),
            TimerItem(
                selectedNumbers = listOf(7, 8, 9),
                triggerTime = LocalDateTime.now(), // You should replace this with an actual LocalDateTime
                alarmTime = 30,
                extraTime = 15,
                hasTriggered = true
            )
        )

        timerItems.forEach {
            fakeTimerRepository.insertTimerItem(it)

        }

        // Trigger loading
        GlobalScope.launch {
            viewModel.state.collect()
        }

        // State should be updated based on the loaded timer items
        assertEquals(PastTimersState(pastTimerItems = timerItems.map { it.toPastTimerItem() }), viewModel.state.value.pastTimerItems)
    }

    // Add more test cases as needed based on your application logic
}
