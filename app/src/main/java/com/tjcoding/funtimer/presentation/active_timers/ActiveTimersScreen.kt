package com.tjcoding.funtimer.presentation.active_timers

import android.os.CountDownTimer
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tjcoding.funtimer.BuildConfig
import com.tjcoding.funtimer.presentation.components.BasicTimerCard
import com.tjcoding.funtimer.presentation.components.CustomItemsVerticalGrid
import com.tjcoding.funtimer.presentation.timer_setup.ObserveAsEvents
import com.tjcoding.funtimer.ui.theme.FunTimerTheme
import com.tjcoding.funtimer.utility.Util.SecondsFormatTommss
import com.tjcoding.funtimer.utility.navigation.SecondaryScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun ActiveTimersScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ActiveTimersViewModel = hiltViewModel(),
    navController: NavController
) {
    ActiveTimersScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        modifier = modifier,
        shouldShowDeleteTimerItemDialogStream = viewModel.shouldShowDeleteTimerItemDialogStream,
        shouldNavigateToEditTimerItemScreenStream = viewModel.shouldNavigateToEditTimerItemScreenStream,
        selectedActiveTimerItemStream = viewModel.selectedActiveTimerItemStream,
        navigateToEditActiveTimerScreen = {id ->
            val route = SecondaryScreen.EditActiveTimerScreen.createRoute(id)
            navController.navigate(route)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveTimersScreen(
    modifier: Modifier = Modifier,
    state: ActiveTimersState,
    onEvent: (ActiveTimersEvent) -> Unit,
    shouldShowDeleteTimerItemDialogStream: Flow<Boolean>,
    shouldNavigateToEditTimerItemScreenStream: Flow<Boolean>,
    selectedActiveTimerItemStream: Flow<ActiveTimerItem>,
    navigateToEditActiveTimerScreen: (Int) -> Unit
) {

    var shouldShowAlertDialog by remember { mutableStateOf(false) }
    var selectedTimerItem: ActiveTimerItem? by remember { mutableStateOf(null) }

    ObserveAsEvents(stream = shouldShowDeleteTimerItemDialogStream) { shouldShowDeleteTimerItemDialogNew ->
        shouldShowAlertDialog = shouldShowDeleteTimerItemDialogNew
    }
    ObserveAsEvents(stream = shouldNavigateToEditTimerItemScreenStream) { shouldNavigateToEditTimerItemScreen ->
        if(shouldNavigateToEditTimerItemScreen) navigateToEditActiveTimerScreen(selectedTimerItem?.toTimerItem().hashCode())
    }
    ObserveAsEvents(stream = selectedActiveTimerItemStream) { selectedTimerItemNew ->
        selectedTimerItem = selectedTimerItemNew
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Fun Timer")
                },
            )
        },
    ) { paddingValues ->

        if (state.activeTimerItems.isEmpty()) {
            Box(
                modifier = modifier.fillMaxSize(), // Fill the entire screen
                contentAlignment = Alignment.Center // Center content within the Box
            ) {
                Text(
                    text = "No active timers",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        } else {
            TimerCardsVerticalGrid(
                modifier.padding(paddingValues),
                state.activeTimerItems,
                onXClick = { activeTimerItem ->
                    onEvent(
                        ActiveTimersEvent.OnXClick(
                            activeTimerItem
                        )
                    )
                },
                onEditClick = { activeTimerItem ->
                    onEvent(
                        ActiveTimersEvent.OnEditClick(
                            activeTimerItem
                        )
                    )
                }
            )
        }
        if (shouldShowAlertDialog) {
            DeleteTimerItemDialog(
                onConfirmClick = {
                    if (selectedTimerItem != null) {
                        onEvent(
                            ActiveTimersEvent.OnAlertDialogDeleteClick(
                                selectedTimerItem!!
                            )
                        )
                        selectedTimerItem = null
                    }
                    shouldShowAlertDialog = false

                },
                onDismissClick = { shouldShowAlertDialog = false }
            )
        }
    }
}

@Composable
fun DeleteTimerItemDialog(
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        confirmButton = {
            TextButton(
                onClick = onConfirmClick
            ) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissClick
            ) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(
                text = "Delete Timer Item",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete this timer item?",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        shape = MaterialTheme.shapes.extraLarge
    )
}

@Composable
private fun TimerCardsVerticalGrid(

    modifier: Modifier,
    activeTimerItems: List<ActiveTimerItem>,
    onXClick: (ActiveTimerItem) -> Unit,
    onEditClick: (ActiveTimerItem) -> Unit
) {
    CustomItemsVerticalGrid(
        modifier = modifier,
        items = activeTimerItems,
        key = { pastTimerItem -> pastTimerItem.hashCode() }) { lazyListModifier, activeTimerItem ->
        CountdownActiveTimerItem(lazyListModifier, activeTimerItem, onXClick, onEditClick)
    }

}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun CountdownActiveTimerItem(
    modifier: Modifier,
    activeTimerItem: ActiveTimerItem,
    onXClick: (ActiveTimerItem) -> Unit,
    onEditClick: (ActiveTimerItem) -> Unit,
) {
    val isInDebugMode = BuildConfig.DEBUG


    val millisInFutureTriggerTime = remember {
        (activeTimerItem.triggerTime.atZone(ZoneId.systemDefault())
            .toEpochSecond() * 1000 - System.currentTimeMillis())
    }
    val millisInFutureAlarmTime =
        remember { if (isInDebugMode) activeTimerItem.alarmTime * 1000L else activeTimerItem.alarmTime * 60 * 1000L }

    if (millisInFutureTriggerTime > millisInFutureAlarmTime) {

        val millisInFutureExtraTime =
            remember { millisInFutureTriggerTime - millisInFutureAlarmTime }

        var alarmTime by remember { mutableLongStateOf(millisInFutureAlarmTime / 1000) }
        var extraTime by remember { mutableLongStateOf(millisInFutureExtraTime / 1000) }

        val timeCountDown = remember {
            object : CountDownTimer(millisInFutureAlarmTime, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    alarmTime = (millisUntilFinished / 1000)
                }

                override fun onFinish() {}
            }
        }

        remember {
            object : CountDownTimer(millisInFutureExtraTime, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    extraTime = millisUntilFinished / 1000
                }

                override fun onFinish() {
                    timeCountDown.start()
                }
            }.start()
        }

        ActiveTimerCard(modifier = modifier
            .aspectRatio(7 / 8f)
            .padding(4.dp),
            numbers = activeTimerItem.selectedNumbers,
            time = alarmTime.SecondsFormatTommss(),
            extraTime = extraTime.SecondsFormatTommss(),
            onEditClick = { onEditClick(activeTimerItem) },
            onXClick = { onXClick(activeTimerItem) },
        )
    } else {
        var alarmTime by remember { mutableLongStateOf(millisInFutureTriggerTime / 1000) }

        remember {
            object : CountDownTimer(millisInFutureTriggerTime, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    alarmTime = (millisUntilFinished / 1000)
                }

                override fun onFinish() {}
            }.start()
        }
        ActiveTimerCard(modifier = modifier
            .aspectRatio(7 / 8f)
            .padding(4.dp),
            numbers = activeTimerItem.selectedNumbers,
            time = alarmTime.SecondsFormatTommss(),
            extraTime = "00:00",
            onEditClick = { onEditClick(activeTimerItem) },
            onXClick = { onXClick(activeTimerItem) },
        )
    }


}

@Composable
fun ActiveTimerCard(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    time: String,
    extraTime: String?,
    onXClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    BasicTimerCard(modifier = modifier, numbers = numbers,
        onNumberBoxClick = {},
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        boxBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
        boxTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        top = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(modifier = Modifier.size(32.dp), onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        modifier = Modifier.size(20.dp),
                        contentDescription = null
                    )
                }
                Icon(
                    modifier = Modifier.padding(vertical = 2.dp),
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = null
                )
                IconButton(modifier = Modifier.size(32.dp), onClick = onXClick) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        modifier = Modifier.size(20.dp),
                        contentDescription = null
                    )
                }
            }

        }, bottom = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelLarge
                )
                if (extraTime != null) {
                    Text(
                        text = "ET: $extraTime",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        })
}

@Composable
@PreviewLightDark
private fun ActiveTimersScreenPreview() {
    FunTimerTheme {
        Surface(
            tonalElevation = 2.dp
        ) {
            ActiveTimersScreen(
                modifier = Modifier,
                state = ActiveTimersState(
                    activeTimerItems = listOf(
                        ActiveTimerItem(
                            selectedNumbers = listOf(1, 2, 3, 4, 5, 6, 10, 15, 16, 17, 18),
                            triggerTime = LocalDateTime.now().plusMinutes(32),
                            alarmTime = 30,
                            extraTime = 2,
                        ),
                        ActiveTimerItem(
                            selectedNumbers = listOf(1, 2, 3, 4, 5, 6, 10, 15, 16, 17, 19),
                            triggerTime = LocalDateTime.now(),
                            alarmTime = 30,
                            extraTime = 2,
                        ),
                        ActiveTimerItem(
                            selectedNumbers = listOf(1, 2, 3, 4, 5, 6, 10, 15, 16, 17, 20),
                            triggerTime = LocalDateTime.now(),
                            alarmTime = 30,
                            extraTime = 2,
                        ),
                        ActiveTimerItem(
                            selectedNumbers = listOf(1, 2, 3, 4, 5, 6, 10, 15, 16, 17, 21),
                            triggerTime = LocalDateTime.now(),
                            alarmTime = 30,
                            extraTime = 2,
                        )
                    )
                ),
                onEvent = {},
                shouldNavigateToEditTimerItemScreenStream = flowOf(false),
                shouldShowDeleteTimerItemDialogStream = flowOf(false),
                selectedActiveTimerItemStream = flowOf(),
                navigateToEditActiveTimerScreen = {}
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun ActiveTimersScreenEmptyPreview() {
    FunTimerTheme {
        Surface(
            tonalElevation = 2.dp
        ) {
            ActiveTimersScreen(
                modifier = Modifier,
                state = ActiveTimersState(
                    activeTimerItems = emptyList()
                ),
                onEvent = {},
                shouldNavigateToEditTimerItemScreenStream = flowOf(false),
                shouldShowDeleteTimerItemDialogStream = flowOf(false),
                selectedActiveTimerItemStream = flowOf(),
                navigateToEditActiveTimerScreen = {}
            )
        }
    }
}



