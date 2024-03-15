package com.tjcoding.funtimer.service.alarm


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.service.alarm.AlarmService.Companion.FIRE_ALARM_ACTION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var timerRepository: TimerRepository
    override fun onReceive(context: Context?, intent: Intent?) {


        val timerItemId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("TIMER_ITEM_ID", UUID::class.java) ?: return
        } else {
            @Suppress("DEPRECATION")
            intent?.extras?.get("TIMER_ITEM_ID") as UUID
        }

        goAsync {
            val timerItem = timerRepository.getTimerItemById(timerItemId)
            if (timerItem == null) return@goAsync

            timerRepository.updateTimerItem(originalTimerItem = timerItem, newTimerItem = timerItem.copy(hasTriggered = true))

            val serviceIntent = Intent(context, AlarmService::class.java)
            serviceIntent.action = FIRE_ALARM_ACTION
            serviceIntent.putExtra("TIMER_ITEM", timerItem)
            context?.startForegroundService(serviceIntent)
        }


    }


    @OptIn(DelicateCoroutinesApi::class)
    fun BroadcastReceiver.goAsync(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val pendingResult = goAsync()
        GlobalScope.launch(context) {
            try {
                retryIO {
                    block()
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun <T> retryIO(
        times: Int = 3,
        initialDelay: Long = 1000, // 1 second
        maxDelay: Long = 5000,    // 5 second
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                return block()
            } catch (e: IOException) {
                // you can log an error here and/or make a more finer-grained
                // analysis of the cause to see if retry is needed
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block() // last attempt
    }
}