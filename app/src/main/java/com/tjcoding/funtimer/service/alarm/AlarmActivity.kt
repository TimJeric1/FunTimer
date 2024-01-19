package com.tjcoding.funtimer.service.alarm

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.service.alarm.AlarmService.Companion.ALARM_DONE_ACTION
import com.tjcoding.funtimer.service.alarm.AlarmService.Companion.DISMISS_ALARM_ACTION
import com.tjcoding.funtimer.service.alarm.presentation.AlarmScreen
import com.tjcoding.funtimer.ui.theme.FunTimerTheme
import java.time.Instant
import kotlin.properties.Delegates

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
    private var activityCreationTime by Delegates.notNull<Long>()

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
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
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra("TIMER_ITEM") ?: return
        }




        setContent {
            FunTimerTheme {
                Surface(
                    tonalElevation = 2.dp
                ) {

                    NavigationBarColor(color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                    StatusBarColor(color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))


                    val openDialog = remember { mutableStateOf(false) }
                    AlarmScreen(
                        numbers = timerItem.selectedNumbers,
                        onDismiss = {
                            muteSound()
                            val lessThan5SecondsPassed =
                                Instant.now().epochSecond < activityCreationTime + 5
                            if (lessThan5SecondsPassed) {
                                openDialog.value = true
                            } else {
                                onDismiss()
                            }
                        },
                        onMute = ::muteSound
                    )
                    if (openDialog.value) {
                        AlertDialog(
                            onDismissRequest = { openDialog.value = false },
                            title = {
                                Text(text = "Dismiss the alarm?")
                            },
                            text = {
                                Text("Are you sure you want to dismiss the alarm this quickly?")
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = ::onDismiss
                                ) {
                                    Text("Yes")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        openDialog.value = false
                                    }
                                ) {
                                    Text("No")
                                }
                            }
                        )
                    }
                }
            }
        }
        activityCreationTime = Instant.now().epochSecond

    }

    @Composable
    fun NavigationBarColor(color: Color) {
        val view = LocalView.current
        val darkTheme = isSystemInDarkTheme()
        val currentWindow = (view.context as? Activity)?.window
            ?: throw Exception("Not in an activity - unable to get Window reference")

        if (!view.isInEditMode) {
            SideEffect {
                (view.context as Activity).window.navigationBarColor = color.toArgb()
                WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    @Composable
    fun StatusBarColor(color: Color) {
        val view = LocalView.current
        val darkTheme = isSystemInDarkTheme()
        val currentWindow = (view.context as? Activity)?.window
            ?: throw Exception("Not in an activity - unable to get Window reference")

        if (!view.isInEditMode) {
            SideEffect {
                (view.context as Activity).window.statusBarColor = color.toArgb()
                WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    private fun muteSound() {
        AlarmHorn.stop(this)
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



