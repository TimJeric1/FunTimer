import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tjcoding.funtimer.data.repository.FakeTimerRepository
import com.tjcoding.funtimer.presentation.active_timers.ActiveTimersEvent
import com.tjcoding.funtimer.presentation.active_timers.ActiveTimersState
import com.tjcoding.funtimer.presentation.active_timers.ActiveTimersViewModel
import com.tjcoding.funtimer.presentation.active_timers.toActiveTimerItem
import com.tjcoding.funtimer.presentation.active_timers.toTimerItem
import com.tjcoding.funtimer.service.alarm.FakeAlarmScheduler
import com.tjcoding.funtimer.utility.MainDispatcherRule
import com.tjcoding.funtimer.utility.Util
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

@ExperimentalCoroutinesApi
class ActiveTimersViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testScope: TestScope

    private lateinit var viewModel: ActiveTimersViewModel
    private lateinit var fakeTimerRepository: FakeTimerRepository
    private lateinit var fakeAlarmScheduler: FakeAlarmScheduler

    @Before
    fun setup() {
        fakeTimerRepository = FakeTimerRepository()
        fakeAlarmScheduler = FakeAlarmScheduler()
        viewModel = ActiveTimersViewModel(fakeTimerRepository, fakeAlarmScheduler)
        testScope = TestScope()
    }

    @Test
    fun `verify initial state`() {
        assertEquals(ActiveTimersState(), viewModel.state.value)
    }

    @Test
    fun `onAlertDialogDeleteClick deletes timer item and cancels alarm`() = runTest {

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        // Create a sample TimerItem
        val activeTimerItem = Util.DEFAULT_ACTIVE_TIMER_ITEM

        // Insert the TimerItem into the fake repository
        fakeTimerRepository.insertTimerItem(activeTimerItem.toTimerItem())

        delay(1000)

        // Check if the TimerItem was inserted from the repository
        val itemsBeforeDelete = viewModel.state.value.activeTimerItems
        assertEquals(1, itemsBeforeDelete.size)

        // Call the function to be tested
        viewModel.onEvent(ActiveTimersEvent.OnAlertDialogDeleteClick(activeTimerItem))

        delay(1000)

        // Check if the TimerItem was deleted from the repository
        val itemsAfterDelete = viewModel.state.value.activeTimerItems
        assertEquals(0, itemsAfterDelete.size)


        // Check if the alarm associated with the TimerItem was canceled
        val alarmCancelled = fakeAlarmScheduler.canceledItems.contains(activeTimerItem.toTimerItem())
        assertEquals(true, alarmCancelled)
    }

    @Test
    fun `onXIconClick sends correct event`() = runTest {
        val activeTimerItem = Util.DEFAULT_ACTIVE_TIMER_ITEM

        testScope.launch {
            viewModel.selectedActiveTimerItemStream.collect {
                assertEquals(activeTimerItem, it)
            }
        }

        viewModel.onEvent(ActiveTimersEvent.OnXIconClick(activeTimerItem))

        delay(100)


    }

    @Test
    fun `onEditIconClick sends correct event`() = runTest {
        val activeTimerItem = Util.DEFAULT_ACTIVE_TIMER_ITEM

        testScope.launch {
            viewModel.selectedActiveTimerItemStream.collect {
                assertEquals(activeTimerItem, it)
            }
        }

        viewModel.onEvent(ActiveTimersEvent.OnEditIconClick(activeTimerItem))

        delay(100)
    }

    @Test
    fun `inserting and deleting timer item updates state and cancels alarm`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }


        val activeTimerItem = Util.DEFAULT_ACTIVE_TIMER_ITEM

        fakeTimerRepository.insertTimerItem(activeTimerItem.toTimerItem())

        delay(1000)

        val itemsAfterInsert = viewModel.state.value.activeTimerItems
        assertEquals(1, itemsAfterInsert.size)
        assertEquals(activeTimerItem, itemsAfterInsert[0])

        viewModel.onEvent(ActiveTimersEvent.OnXIconClick(activeTimerItem))
        viewModel.onEvent(ActiveTimersEvent.OnAlertDialogDeleteClick(activeTimerItem))

        delay(1000)

        val itemsAfterDelete = viewModel.state.value.activeTimerItems
        assertEquals(0, itemsAfterDelete.size)

        val alarmCancelled = fakeAlarmScheduler.canceledItems.contains(activeTimerItem.toTimerItem())
        assertEquals(true, alarmCancelled)
    }




    // Clean up after each test
    @After
    fun cleanup() {
        testScope.cancel()
        TestScope().cancel()
    }
}
