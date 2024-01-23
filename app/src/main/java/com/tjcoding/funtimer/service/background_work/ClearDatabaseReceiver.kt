package com.tjcoding.funtimer.service.background_work
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tjcoding.funtimer.domain.repository.TimerRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


@AndroidEntryPoint
class ClearDatabaseReceiver : BroadcastReceiver() {

    @Inject
    lateinit var timerRepository: TimerRepository
    override fun onReceive(context: Context?, intent: Intent?) {
        goAsync {
            timerRepository.deleteAll()
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
            }
            finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun <T> retryIO(
        times: Int = 3,
        initialDelay: Long = 1000, // 1 second
        maxDelay: Long = 5000,    // 5 second
        factor: Double = 2.0,
        block: suspend () -> T): T
    {
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



