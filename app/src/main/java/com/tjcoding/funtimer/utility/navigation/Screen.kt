package com.tjcoding.funtimer.utility.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
    data object TimerSetupScreen : Screen("timer_setup_screen", Icons.Outlined.Timer)
    data object ActiveTimersScreen : Screen("active_timers_screen", Icons.Outlined.History)
}