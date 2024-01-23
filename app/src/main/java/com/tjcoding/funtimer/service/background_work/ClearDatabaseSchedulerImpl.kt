package com.tjcoding.funtimer.service.background_work

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import javax.inject.Inject

class ClearDatabaseSchedulerImpl @Inject constructor(
    private val context: Context
) : ClearDatabaseScheduler {
    private val CLEAR_DATABASE_ALARM_CODE = 521

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    override fun scheduleRegularClearing() {
        val intent = Intent(context, ClearDatabaseReceiver::class.java)
        val alarmAlreadySet = PendingIntent.getBroadcast(
            context, CLEAR_DATABASE_ALARM_CODE, intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) != null

        if (alarmAlreadySet) return

        val shouldScheduleToday = LocalTime.now().hour < 3
        val oneAM = LocalTime
            .parse("01:00:00")
            // if current time is less than 3am then schedule the alarm to 3am same day
            // if current time is more than 3am then schedule the alarm to 3am next day
            .atDate(LocalDate.now().plusDays(if (shouldScheduleToday) 0 else 1))
            .atOffset(ZoneOffset.systemDefault().rules.getOffset(Instant.now()))
            .toEpochSecond() * 1000

        alarmManager.setWindow(
            AlarmManager.RTC,
            oneAM,
            AlarmManager.INTERVAL_HOUR * 5, // From 1AM to 6AM
            PendingIntent.getBroadcast(
                context,
                CLEAR_DATABASE_ALARM_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }



}