package com.tjcoding.funtimer.di

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tjcoding.funtimer.data.local.TimerDatabase
import com.tjcoding.funtimer.data.local.daos.TimerDao
import com.tjcoding.funtimer.data.repository.TimerRepositoryImpl
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.utility.alarm.alarm_service.AlarmScheduler
import com.tjcoding.funtimer.utility.alarm.alarm_service.AlarmSchedulerImpl
import com.tjcoding.funtimer.utility.alarm.notification_service.NotificationService
import com.tjcoding.funtimer.utility.alarm.notification_service.NotificationServiceImpl
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
        ).build()
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
        alarmScheduler: AlarmScheduler
    ): TimerRepository {
        return TimerRepositoryImpl(
            timerDao = db.timerDao(),
            alarmScheduler = alarmScheduler
        )
    }

    @Provides
    @Singleton
    fun provideNotificationService(app: Application): NotificationService {
        return NotificationServiceImpl(app.applicationContext)
    }
}