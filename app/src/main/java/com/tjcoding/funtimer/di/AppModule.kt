package com.tjcoding.funtimer.di

import android.app.Application
import androidx.room.Room
import com.tjcoding.funtimer.data.local.TimerDatabase
import com.tjcoding.funtimer.data.repository.TimerRepositoryImpl
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.service.alarm.AlarmScheduler
import com.tjcoding.funtimer.service.alarm.AlarmSchedulerImpl
import com.tjcoding.funtimer.service.notifications.NotificationsService
import com.tjcoding.funtimer.service.notifications.NotificationsServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


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
    fun provideTimerRepository(
        db: TimerDatabase,
    ): TimerRepository {
        return TimerRepositoryImpl(
            timerDao = db.timerDao(),
        )
    }

    @Provides
    @Singleton
    fun provideNotificationService(app: Application): NotificationsService {
        return NotificationsServiceImpl(app.applicationContext)
    }
}