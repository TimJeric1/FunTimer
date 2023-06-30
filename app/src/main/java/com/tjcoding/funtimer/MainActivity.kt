package com.tjcoding.funtimer

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tjcoding.funtimer.presentation.history.HistoryScreen
import com.tjcoding.funtimer.navigation.Screen
import com.tjcoding.funtimer.presentation.timer.TimerScreen
import com.tjcoding.funtimer.ui.theme.FunTimerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screens = listOf(
            Screen.Timer,
            Screen.History,
        )

        setContent {
            FunTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    Scaffold(
                        topBar = {
                            Column() {
                                TopAppBar(title = {
                                    Text(text = "Fun Timer")
                                },
                                    actions = {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = "Settings icon"
                                        )
                                    })
                            }
                        },
                        bottomBar = {
                            NavigationBar {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                screens.forEachIndexed { index, screen ->
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = screen.route) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                // Pop up to the start destination of the graph to
                                                // avoid building up a large stack of destinations
                                                // on the back stack as users select items
                                                popUpTo(navController.graph.id){
                                                    inclusive = true
                                                }
                                                // Avoid multiple copies of the same destination when
                                                // reselecting the same item
                                                launchSingleTop = true
                                                // Restore state when reselecting a previously selected item
                                            }
                                        }

                                    )
                                }
                            }
                        }
                    ) { contentPadding ->
                        NavHost(navController = navController, startDestination = "timer") {
                            composable("timer") { TimerScreen(modifier = Modifier.padding(contentPadding)) }
                            composable("history") { HistoryScreen(modifier = Modifier.padding(contentPadding)) }
                            /*...*/
                        }
                    }
                }
            }
        }
    }
}


