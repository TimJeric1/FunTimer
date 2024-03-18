package com.tjcoding.funtimer.di

import android.app.Application
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.tjcoding.funtimer.data.error_handler.ErrorHandlerImpl
import com.tjcoding.funtimer.data.local.TimerDatabase
import com.tjcoding.funtimer.data.repository.TimerRepositoryImpl
import com.tjcoding.funtimer.data.repository.UserPreferencesRepositoryImpl
import com.tjcoding.funtimer.domain.error_handler.ErrorHandler
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.domain.repository.UserPreferencesRepository
import com.tjcoding.funtimer.service.alarm.AlarmNotificationsManager
import com.tjcoding.funtimer.service.alarm.AlarmNotificationsManagerImpl
import com.tjcoding.funtimer.service.alarm.AlarmScheduler
import com.tjcoding.funtimer.service.alarm.AlarmSchedulerImpl
import com.tjcoding.funtimer.service.error_notification.ErrorNotificationsManager
import com.tjcoding.funtimer.service.error_notification.ErrorNotificationsManagerImpl
import com.tjcoding.funtimer.service.scheduled_work.ClearDatabaseScheduler
import com.tjcoding.funtimer.service.scheduled_work.ClearDatabaseSchedulerImpl
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
    fun provideAlarmScheduler(app: Application): AlarmScheduler {
        return AlarmSchedulerImpl(app.applicationContext)
    }

    @Provides
    @Singleton
    fun provideClearDatabaseScheduler(app: Application): ClearDatabaseScheduler {
        return ClearDatabaseSchedulerImpl(app.applicationContext)
    }

    @Provides
    @Singleton
    fun provideTimerRepository(
        db: TimerDatabase,
        errorHandler: ErrorHandler
    ): TimerRepository {
        return TimerRepositoryImpl(
            timerDao = db.timerDao(),
            errorHandler = errorHandler,
        )
    }



    @Provides
    @Singleton
    fun providerUserPreferencesRepository(app: Application, errorHandler: ErrorHandler): UserPreferencesRepository {
        val appContext = app.applicationContext
        return UserPreferencesRepositoryImpl(
            PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
                migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES)),
                produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
            ),
            errorHandler = errorHandler
        )
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsAppModule {

    @Binds
    @Singleton
    abstract fun bindAlarmNotificationsManager(alarmNotificationsManagerImpl: AlarmNotificationsManagerImpl): AlarmNotificationsManager

    @Binds
    @Singleton
    abstract fun bindErrorHandler(errorHandlerImpl: ErrorHandlerImpl): ErrorHandler

    @Binds
    @Singleton
    abstract fun bindErrorNotificationsManager(errorNotificationsManagerImpl: ErrorNotificationsManagerImpl): ErrorNotificationsManager

}


