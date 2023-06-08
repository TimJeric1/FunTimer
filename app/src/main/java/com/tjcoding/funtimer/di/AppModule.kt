package com.tjcoding.funtimer.di

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tjcoding.funtimer.data.local.TimerDatabase
import com.tjcoding.funtimer.data.local.daos.TimerDao
import com.tjcoding.funtimer.data.repository.TimerRepositoryImpl
import com.tjcoding.funtimer.domain.repository.TimerRepository
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
    fun provideTimerRepository(
        db: TimerDatabase
    ): TimerRepository {
        return TimerRepositoryImpl(
            timerDao = db.timerDao(),
        )
    }
}