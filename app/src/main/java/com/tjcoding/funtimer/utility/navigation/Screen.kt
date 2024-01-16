package com.tjcoding.funtimer.utility.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAlarm
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val name: String) {
    data object TimerSetupScreen : Screen("timer_setup_screen", Icons.Outlined.AddAlarm, "Setup Timer")
    data object ActiveTimersScreen : Screen("active_timers_screen", Icons.Outlined.Timer, "Active Timers")

    data object PastTimersScreen : Screen("past_timers_screen", Icons.Outlined.History, "Past Timers")
}