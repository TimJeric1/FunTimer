package com.tjcoding.funtimer.presentation.components

import android.os.CountDownTimer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tjcoding.funtimer.BuildConfig
import java.time.LocalDateTime
import java.time.ZoneId


@Composable
fun AlarmAndExtraTimeCountdown(
    alarmTime: Int,
    triggerTime: LocalDateTime,
    content: @Composable (countDownAlarmTime: Long, countDownExtraTime: Long) -> Unit
) {
    val isInDebugMode = BuildConfig.DEBUG

    val millisInFutureTriggerTime = remember(triggerTime) {
        (triggerTime.atZone(ZoneId.systemDefault())
            .toEpochSecond() * 1000 - System.currentTimeMillis())
    }
    val millisInFutureAlarmTime =
        remember(alarmTime) { if (isInDebugMode) alarmTime * 1000L else alarmTime * 60 * 1000L }


    val millisInFutureExtraTime =
        remember(millisInFutureTriggerTime, millisInFutureAlarmTime) { millisInFutureTriggerTime - millisInFutureAlarmTime }
    var countDownAlarmTime by remember(millisInFutureTriggerTime, millisInFutureAlarmTime) { mutableLongStateOf(if (millisInFutureTriggerTime > millisInFutureAlarmTime) millisInFutureAlarmTime / 1000 else millisInFutureTriggerTime / 1000) }
    var countDownExtraTime by remember(millisInFutureExtraTime) { mutableLongStateOf(millisInFutureExtraTime / 1000) }

    val timeCountDown = remember(millisInFutureTriggerTime,millisInFutureAlarmTime) {
        object : CountDownTimer(
            if (millisInFutureTriggerTime > millisInFutureAlarmTime) millisInFutureAlarmTime else millisInFutureTriggerTime,
            1000
        ) {

            override fun onTick(millisUntilFinished: Long) {
                countDownAlarmTime = (millisUntilFinished / 1000)
            }

            override fun onFinish() {}
        }
    }

    remember(millisInFutureExtraTime) {
        object : CountDownTimer(millisInFutureExtraTime, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                countDownExtraTime = millisUntilFinished / 1000
            }

            override fun onFinish() {
                timeCountDown.start()
            }
        }.start()
    }

    content(countDownAlarmTime, countDownExtraTime)

}