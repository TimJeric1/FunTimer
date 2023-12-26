package com.tjcoding.funtimer.presentation.active_timers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tjcoding.funtimer.presentation.components.TimerCard
import com.tjcoding.funtimer.presentation.components.CustomItemsVerticalGrid
import com.tjcoding.funtimer.utility.Util.getDuration
import java.time.LocalDateTime

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
@Preview
fun ActiveTimersScreen(
    modifier: Modifier = Modifier,
    state: ActiveTimersState = ActiveTimersState(
        activeTimerItemsUi = listOf(
            ActiveTimerItemUi(
                listOf(1, 2, 3),
                LocalDateTime.now(),
                2,
            )
        )
    ),
    onEvent: (ActiveTimersEvent) -> Unit = {}
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
        TimerCardsVerticalGrid(
            modifier.padding(paddingValues),
            state.activeTimerItemsUi,
            onLongClick = { activeTimerItemsUi -> onEvent(ActiveTimersEvent.OnCardLongClick(activeTimerItemsUi)) },
        )
    }

}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun TimerCardsVerticalGrid(
    modifier: Modifier,
    activeTimeItemsUi: List<ActiveTimerItemUi>,
    onLongClick: (ActiveTimerItemUi) -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    CustomItemsVerticalGrid(modifier = modifier, items = activeTimeItemsUi) { activeTimerItemsUi ->
        TimerCard(modifier = Modifier
            .size(screenHeight.dp * 0.25f)
            .combinedClickable(
                onClick = {},
                onLongClick = { onLongClick(activeTimerItemsUi) }
            ),
            numbers = activeTimerItemsUi.selectedNumbers,
            time = getDuration(activeTimerItemsUi.alarmTime),
            extraTime = "${activeTimerItemsUi.extraTime}:00")
    }

}



