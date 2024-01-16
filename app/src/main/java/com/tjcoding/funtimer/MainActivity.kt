package com.tjcoding.funtimer

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tjcoding.funtimer.presentation.active_timers.ActiveTimersScreenRoot
import com.tjcoding.funtimer.presentation.past_timers.PastTimersScreenRoot
import com.tjcoding.funtimer.utility.navigation.Screen
import com.tjcoding.funtimer.presentation.timer_setup.TimerSetupScreenRoot
import com.tjcoding.funtimer.ui.theme.FunTimerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            AskNotificationsPermission(context = LocalContext.current)
            AskFullscreenIntentPermission(context = LocalContext.current)


            FunTimerTheme {
                Surface(
                    tonalElevation = 5.dp
                ) {

                    SystemBarsColor(color = MaterialTheme.colorScheme.surfaceColorAtElevation(NavigationBarDefaults.Elevation + 5.dp))


                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = { BottomNavigationBar(navController) }
                    ) { contentPadding ->
                        NavHost(navController = navController, startDestination = Screen.TimerSetupScreen.route) {
                            composable(Screen.TimerSetupScreen.route) {
                                TimerSetupScreenRoot(
                                    modifier = Modifier.padding(
                                        contentPadding
                                    )
                                )
                            }
                            composable(Screen.ActiveTimersScreen.route) {
                                ActiveTimersScreenRoot(
                                    modifier = Modifier.padding(
                                        contentPadding
                                    )
                                )
                            }
                            composable(Screen.PastTimersScreen.route) {
                                PastTimersScreenRoot(
                                    modifier = Modifier.padding(
                                        contentPadding
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun BottomNavigationBar(navController: NavController) {
        val screens = listOf(
            Screen.TimerSetupScreen,
            Screen.ActiveTimersScreen,
            Screen.PastTimersScreen
        )
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            screens.forEach { screen ->
                NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = screen.route) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                        }
                    },
                    label = { Text(text = screen.name)},
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )


                )
            }
        }
    }

    @Composable
    fun SystemBarsColor(color: Color) {
        val view = LocalView.current
        val darkTheme = isSystemInDarkTheme()
        val currentWindow = (view.context as? Activity)?.window
            ?: throw Exception("Not in an activity - unable to get Window reference")

        if (!view.isInEditMode) {
            SideEffect {
                (view.context as Activity).window.statusBarColor = color.toArgb()
                (view.context as Activity).window.navigationBarColor = color.toArgb()
                WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    @Composable
    @PreviewLightDark
    fun BottomNavigationBarPreview() {
        FunTimerTheme {
            BottomNavigationBar(navController = rememberNavController())
        }
    }


    @Composable
    fun AskNotificationsPermission(context: Context) {
        var hasNotificationPermission by remember {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                mutableStateOf(
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                )
            } else mutableStateOf(true)
        }
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                hasNotificationPermission = isGranted
                if (!isGranted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        )
        LaunchedEffect(key1 = hasNotificationPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!hasNotificationPermission) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }

    }

    @Composable
    fun AskFullscreenIntentPermission(context: Context) {
        var hasFullscreenIntentPermission by remember {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mutableStateOf(
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.USE_FULL_SCREEN_INTENT
                    ) == PackageManager.PERMISSION_GRANTED
                )
            } else mutableStateOf(true)
        }
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                hasFullscreenIntentPermission = isGranted
                if (!isGranted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        shouldShowRequestPermissionRationale(Manifest.permission.USE_FULL_SCREEN_INTENT)
                }
            }
        )
        LaunchedEffect(key1 = hasFullscreenIntentPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!hasFullscreenIntentPermission) {
                    permissionLauncher.launch(Manifest.permission.USE_FULL_SCREEN_INTENT)
                }
            }
        }

    }




}



