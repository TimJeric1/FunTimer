package com.tjcoding.funtimer.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.tjcoding.funtimer.domain.model.UserPreferences
import com.tjcoding.funtimer.domain.repository.UserPreferencesRepository
import com.tjcoding.funtimer.utility.Util.shouldRetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): UserPreferencesRepository {

    private object PreferencesKeys {
        val CUSTOM_DURATION = intPreferencesKey("custom_duration")
    }

    override val userPreferencesStream: Flow<UserPreferences> = dataStore.data
        .retryWhen { cause, attempt -> shouldRetry(cause, attempt)}
        .catch { exception ->
            if(exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val customDuration = preferences[PreferencesKeys.CUSTOM_DURATION] ?: -1
            UserPreferences(customDuration = customDuration)
        }
    

    override suspend fun updateCustomDuration(customDuration: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CUSTOM_DURATION] = customDuration
        }
    }
}