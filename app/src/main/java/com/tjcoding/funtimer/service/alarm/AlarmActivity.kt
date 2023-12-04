package com.tjcoding.funtimer.service.alarm

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.service.alarm.AlarmService.Companion.ALARM_DONE_ACTION
import com.tjcoding.funtimer.service.alarm.AlarmService.Companion.DISMISS_ALARM_ACTION
import com.tjcoding.funtimer.service.alarm.presentation.AlarmScreen
import com.tjcoding.funtimer.ui.theme.FunTimerTheme


class AlarmActivity : ComponentActivity() {


    private lateinit var timerItem: TimerItem
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ALARM_DONE_ACTION -> finish()
                else -> return
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this /* lifecycle owner */) {
            // don't let the user finish the activity with back button
        }



        val filter = IntentFilter(ALARM_DONE_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)
        else
            registerReceiver(receiver, filter)

        setShowWhenLocked(true)
        setTurnScreenOn(true)
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        keyguardManager.requestDismissKeyguard(this, null)
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )

        timerItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("TIMER_ITEM", TimerItem::class.java) ?: return
        } else {
            intent?.getParcelableExtra("TIMER_ITEM") ?: return
        }


        setContent {
            FunTimerTheme {
                AlarmScreen(
                    numbers = timerItem.selectedNumbers, onDismiss = ::onDismiss
                )
            }
        }

    }


    private fun onDismiss() {
        val dismissIntent = Intent(this, AlarmService::class.java)
        dismissIntent.action = DISMISS_ALARM_ACTION
        startForegroundService(dismissIntent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }




}