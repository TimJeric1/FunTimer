package com.tjcoding.funtimer.presentation.active_timers

import android.os.CountDownTimer
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tjcoding.funtimer.BuildConfig
import com.tjcoding.funtimer.presentation.components.TimerCard
import com.tjcoding.funtimer.presentation.components.CustomItemsVerticalGrid
import com.tjcoding.funtimer.ui.theme.FunTimerTheme
import com.tjcoding.funtimer.utility.Util.SecondsFormatTommss
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun ActiveTimersScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ActiveTimersViewModel = hiltViewModel()
) {
    ActiveTimersScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveTimersScreen(
    modifier: Modifier = Modifier,
    state: ActiveTimersState,
    onEvent: (ActiveTimersEvent) -> Unit,
) {

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
                    onCardLongClick = { activeTimerItems ->
                        onEvent(
                            ActiveTimersEvent.OnCardLongClick(
                                activeTimerItems
                            )
                        )
                    },
                )
            }
        }
    }

@Composable
private fun TimerCardsVerticalGrid(

    modifier: Modifier,
    activeTimerItems: List<ActiveTimerItem>,
    onCardLongClick: (ActiveTimerItem) -> Unit,
) {
    CustomItemsVerticalGrid(modifier = modifier, items = activeTimerItems, key = { pastTimerItem ->  pastTimerItem.hashCode() }) { lazyListModifier, activeTimerItem ->
        CountdownActiveTimerItem(lazyListModifier ,activeTimerItem, onCardLongClick)
    }

}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun CountdownActiveTimerItem(
    modifier: Modifier,
    activeTimerItem: ActiveTimerItem,
    onCardLongClick: (ActiveTimerItem) -> Unit
) {
    val isInDebugMode = BuildConfig.DEBUG


    val millisInFutureTriggerTime = remember {(activeTimerItem.triggerTime.atZone(ZoneId.systemDefault())
        .toEpochSecond() * 1000 - System.currentTimeMillis())}
    val millisInFutureAlarmTime = remember { if(isInDebugMode) activeTimerItem.alarmTime * 1000L else activeTimerItem.alarmTime * 60 * 1000L}

    if(millisInFutureTriggerTime >  millisInFutureAlarmTime) {

        val millisInFutureExtraTime = remember { millisInFutureTriggerTime - millisInFutureAlarmTime }

        val alarmTime = remember { mutableLongStateOf(millisInFutureAlarmTime / 1000) }
        val extraTime = remember { mutableLongStateOf(millisInFutureExtraTime / 1000) }

        val timeCountDown = remember {
            object : CountDownTimer(millisInFutureAlarmTime, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    alarmTime.longValue = (millisUntilFinished / 1000)
                }

                override fun onFinish() {}
            }
        }

        remember {
            object : CountDownTimer(millisInFutureExtraTime, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    extraTime.longValue = millisUntilFinished / 1000
                }

                override fun onFinish() {
                    timeCountDown.start()
                }
            }.start()
        }

        TimerCard(modifier = modifier
            .aspectRatio(7/8f)
            .padding(4.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = { onCardLongClick(activeTimerItem) }
            ),
            numbers = activeTimerItem.selectedNumbers,
            time = alarmTime.longValue.SecondsFormatTommss(),
            extraTime = extraTime.longValue.SecondsFormatTommss(),
            onNumberBoxClick = {}
        )
    } else {
        val alarmTime = remember { mutableLongStateOf(millisInFutureTriggerTime / 1000) }

        remember {
            object : CountDownTimer(millisInFutureTriggerTime, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    alarmTime.longValue = (millisUntilFinished / 1000)
                }

                override fun onFinish() {}
            }.start()
        }
        TimerCard(modifier = modifier
            .aspectRatio(7/8f)
            .padding(4.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = { onCardLongClick(activeTimerItem) }
            ),
            numbers = activeTimerItem.selectedNumbers,
            time = alarmTime.longValue.SecondsFormatTommss(),
            extraTime = "00:00",
            onNumberBoxClick = {}
        )
    }


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
                            selectedNumbers = listOf(1, 2, 3, 4, 5, 6, 10, 15, 16, 17),
                            triggerTime = LocalDateTime.now().plusMinutes(32),
                            alarmTime = 30,
                            extraTime = 2,
                        ),
                        ActiveTimerItem(
                            selectedNumbers = listOf(1, 2, 3, 4, 5, 6, 10, 15, 16, 17),
                            triggerTime = LocalDateTime.now(),
                            alarmTime = 30,
                            extraTime = 2,
                        ),
                        ActiveTimerItem(
                            selectedNumbers = listOf(1, 2, 3, 4, 5, 6, 10, 15, 16, 17),
                            triggerTime = LocalDateTime.now(),
                            alarmTime = 30,
                            extraTime = 2,
                        ),
                        ActiveTimerItem(
                            selectedNumbers = listOf(1, 2, 3, 4, 5, 6, 10, 15, 16, 17),
                            triggerTime = LocalDateTime.now(),
                            alarmTime = 30,
                            extraTime = 2,
                        )
                    )
                ),
                onEvent = {}
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
                onEvent = {}
            )
        }
    }
}



