package com.tjcoding.funtimer.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView
import com.tjcoding.funtimer.utility.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException


@RunWith(RobolectricTestRunner::class)
class UserPreferencesRepositoryImplTest {


    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var dataStore: FakeDataStorePreferences
    private lateinit var userPreferencesRepository: UserPreferencesRepositoryImpl

    @Before
    fun setUp() {
        dataStore = FakeDataStorePreferences(preferences = emptyPreferences())
        userPreferencesRepository = UserPreferencesRepositoryImpl(dataStore)
    }

    @Test
    fun `updating selected extra time in timer setup screen user preferences should reflect the change`() =
        runTest {

            val initalState =
                userPreferencesRepository.timerSetupScreenUserPreferencesStream.first()

            userPreferencesRepository.updateTimerSetupScreenSelectedExtraTime(12)

            val newState = userPreferencesRepository.timerSetupScreenUserPreferencesStream.first()

            assertNotEquals(initalState, newState)
            assertEquals(newState.selectedExtraTime, 12)

        }

    @Test
    fun `updating selected layout view in timer setup screen user preferences should reflect the change`() =
        runTest {

            val initialState =
                userPreferencesRepository.timerSetupScreenUserPreferencesStream.first()

            userPreferencesRepository.updateTimerSetupScreenSelectedLayoutView(LayoutView.ALTERNATIVE)

            val newState = userPreferencesRepository.timerSetupScreenUserPreferencesStream.first()

            assertNotEquals(initialState, newState)
            assertEquals(newState?.selectedLayoutView, LayoutView.ALTERNATIVE)
        }

    @Test
    fun `updating selected custom durations in edit active timer screen user preferences should reflect the change`() =
        runTest {

            val initialState =
                userPreferencesRepository.editActiveTimerScreenUserPreferencesStream.firstOrNull()

            userPreferencesRepository.updateEditActiveTimerScreenCustomDurations(10, 0)

            val newState = userPreferencesRepository.editActiveTimerScreenUserPreferencesStream.first()

            assertNotEquals(initialState, newState)
            assertEquals(newState?.selectedCustomDurations?.get(DurationOption.FIRST), 10)
        }


    @Test
    fun `updating selected custom durations in timer setup screen user preferences should reflect the change`() =
        runTest {
            // Initial state
            val initialState = userPreferencesRepository.timerSetupScreenUserPreferencesStream.first()

            // Update custom durations
            userPreferencesRepository.updateTimerSetupScreenSelectedCustomDurations(40, 0)

            // New state
            val newState = userPreferencesRepository.timerSetupScreenUserPreferencesStream.first()

            // Assert
            assertNotEquals(initialState, newState)
            assertEquals(newState.selectedCustomDurations[DurationOption.FIRST], 40)
        }

    @Test
    fun `updating selected layout view in edit active timer screen user preferences should reflect the change`() =
        runTest {
            // Initial state
            val initialState = userPreferencesRepository.editActiveTimerScreenUserPreferencesStream.first()

            // Update layout view
            userPreferencesRepository.updateEditActiveTimerScreenLayoutView(LayoutView.ALTERNATIVE)

            // New state
            val newState = userPreferencesRepository.editActiveTimerScreenUserPreferencesStream.first()

            // Assert
            assertNotEquals(initialState, newState)
            assertEquals(newState.selectedLayoutView, LayoutView.ALTERNATIVE)
        }



}

class FakeDataStorePreferences(private var preferences: Preferences) : DataStore<Preferences> {
    // Implement methods of DataStore interface with fake behavior
    // For example, provide hardcoded preferences data

    private val preferencesFlow = MutableStateFlow(preferences)

    override val data: Flow<Preferences> = preferencesFlow

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val newPreferences = transform(preferences)
        preferencesFlow.update { newPreferences }
        return newPreferences
    }




}
