package com.tjcoding.funtimer.service.alarm

import android.app.KeyguardManager
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.service.alarm.AlarmService.Companion.DISMISS_ALARM_ACTION
import com.tjcoding.funtimer.service.alarm.presentation.AlarmScreen
import com.tjcoding.funtimer.ui.theme.FunTimerTheme


class AlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setShowWhenLocked(true)
        setTurnScreenOn(true)
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        keyguardManager.requestDismissKeyguard(this, null)
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )

        val timerItem = intent?.getParcelableExtra("TIMER_ITEM", TimerItem::class.java) ?: return


        setContent {
            FunTimerTheme {
                AlarmScreen(numbers = timerItem.selectedNumbers, onDismiss = ::onDismiss

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

}