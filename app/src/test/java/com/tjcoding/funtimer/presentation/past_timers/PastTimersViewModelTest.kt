import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tjcoding.funtimer.data.repository.FakeTimerRepository
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.presentation.past_timers.PastTimersState
import com.tjcoding.funtimer.presentation.past_timers.PastTimersViewModel
import com.tjcoding.funtimer.presentation.past_timers.toPastTimerItem
import com.tjcoding.funtimer.utility.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID

class PastTimersViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testScope: TestScope

    private lateinit var viewModel: PastTimersViewModel
    private lateinit var fakeTimerRepository: FakeTimerRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        testScope = TestScope()
        fakeTimerRepository = FakeTimerRepository()
        viewModel = PastTimersViewModel(repository = fakeTimerRepository)
    }

    @Test
    fun `verify initial state`() {
        assertEquals(PastTimersState(), viewModel.state.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `verify state after loading triggered timer items`() = testScope.runTest {

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        // Assuming you have some triggered timer items in your fake repository
        val timerItems = listOf(
            TimerItem(
                id = UUID.randomUUID(),
                selectedNumbers = listOf(1, 2, 3),
                triggerTime = LocalDateTime.now(),// You should replace this with an actual LocalDateTime
                alarmTime = 30,
                extraTime = 5,
                hasTriggered = true
            ),
            TimerItem(
                id = UUID.randomUUID(),
                selectedNumbers = listOf(4, 5, 6),
                triggerTime = LocalDateTime.now(), // You should replace this with an actual LocalDateTime
                alarmTime = 30,
                extraTime = 10,
                hasTriggered = true
            ),
            TimerItem(
                id = UUID.randomUUID(),
                selectedNumbers = listOf(7, 8, 9),
                triggerTime = LocalDateTime.now(), // You should replace this with an actual LocalDateTime
                alarmTime = 30,
                extraTime = 15,
                hasTriggered = false
            )
        )



        timerItems.forEach {
            fakeTimerRepository.insertTimerItem(it)
            delay(1000)
        }

        delay(1000)

        // State should be updated based on the loaded timer items
        assertEquals(
            PastTimersState(pastTimerItems = timerItems.filter { it.hasTriggered }
                .map { it.toPastTimerItem() }.sortedByDescending { it.triggerTime }),
            viewModel.state.value
        )
    }

    @After
    fun cleanup() {
        testScope.cancel()
        TestScope().cancel()
    }

    // Add more test cases as needed based on your application logic
}
