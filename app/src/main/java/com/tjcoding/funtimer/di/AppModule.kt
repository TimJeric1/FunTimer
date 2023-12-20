package com.tjcoding.funtimer.di

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.tjcoding.funtimer.data.local.TimerDatabase
import com.tjcoding.funtimer.data.repository.TimerRepositoryImpl
import com.tjcoding.funtimer.data.repository.UserPreferencesRepositoryImpl
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.domain.repository.UserPreferencesRepository
import com.tjcoding.funtimer.service.alarm.AlarmNotifications
import com.tjcoding.funtimer.service.alarm.AlarmNotificationsImpl
import com.tjcoding.funtimer.service.alarm.AlarmScheduler
import com.tjcoding.funtimer.service.alarm.AlarmSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


private const val USER_PREFERENCES = "user_preferences"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    // for non static provide functions
    @Provides
    @Singleton
    fun provideTimerDatabase(app: Application): TimerDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            TimerDatabase::class.java, "timer_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(app: Application, isInDebugMode: Boolean): AlarmScheduler {
        return AlarmSchedulerImpl(app.applicationContext, isInDebugMode)
    }

    @Provides
    @Singleton
    fun provideTimerRepository(
        db: TimerDatabase,
    ): TimerRepository {
        return TimerRepositoryImpl(
            timerDao = db.timerDao(),
        )
    }

    @Provides
    @Singleton
    fun providerUserPreferencesRepository(app: Application): UserPreferencesRepository {
        val appContext = app.applicationContext
        return UserPreferencesRepositoryImpl(
            PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
                migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES)),
                produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
            )
        )
    }

    @Provides
    @Singleton
    fun provideIsInDebugMode(app: Application): Boolean {
        val context = app.applicationContext
        return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }


}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsAppModule {

    @Binds
    abstract fun bindAlarmNotifications(alarmNotificationsImpl: AlarmNotificationsImpl): AlarmNotifications
}


