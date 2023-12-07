package com.tjcoding.funtimer.service.alarm


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.service.alarm.AlarmService.Companion.FIRE_ALARM_ACTION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var timerRepository: TimerRepository
    override fun onReceive(context: Context?, intent: Intent?) {


        val timerItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("TIMER_ITEM", TimerItem::class.java) ?: return
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra("TIMER_ITEM") ?: return
        }
        goAsync {
            timerRepository.deleteTimerItem(timerItem)
        }
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.action = FIRE_ALARM_ACTION
        serviceIntent.putExtra("TIMER_ITEM", timerItem)
        context?.startForegroundService(serviceIntent)


    }


    @OptIn(DelicateCoroutinesApi::class)
    fun BroadcastReceiver.goAsync(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val pendingResult = goAsync()
        GlobalScope.launch(context) {
            try {
                block()
            } finally {
                pendingResult.finish()
            }
        }
    }
}