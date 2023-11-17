package com.tjcoding.funtimer.service.alarm
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.content.ContextCompat.startActivity
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.domain.repository.TimerRepository
import com.tjcoding.funtimer.service.notifications.NotificationsService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


@AndroidEntryPoint
class AlarmReceiver(
): BroadcastReceiver() {

    @Inject lateinit var notificationService: NotificationsService
    @Inject lateinit var timerRepository: TimerRepository
    override fun onReceive(context: Context?, intent: Intent?) {
        val timerItem: TimerItem = if(VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("TIMER_ITEM", TimerItem::class.java) ?: return
        } else {
            intent?.getParcelableExtra("TIMER_ITEM") ?: return
        }
        notificationService.showNotification(timerItem.selectedNumbers.toString())
        goAsync {
            timerRepository.deleteTimerItem(timerItem)
        }
        val intent = Intent(context, AlarmActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (context != null) {
            startActivity(context, intent, null)
        }
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
            block()
        } finally {
            pendingResult.finish()
        }
    }
}