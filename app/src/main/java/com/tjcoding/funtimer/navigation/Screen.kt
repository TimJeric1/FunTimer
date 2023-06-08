package com.tjcoding.funtimer.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
    object Timer : Screen("timer", Icons.Outlined.Timer)
    object History : Screen("history", Icons.Outlined.History)
}