package com.tjcoding.funtimer.presentation.active_timers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.presentation.timer_setup.components.TimerCard
import com.tjcoding.funtimer.utility.getDuration
import java.time.LocalDateTime

@Composable
fun ActiveTimersScreenRoot(
    viewModel: ActiveTimersViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    ActiveTimersScreen(
        state = viewModel.historyState.collectAsStateWithLifecycle().value,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun ActiveTimersScreen(
    state: ActiveTimersState = ActiveTimersState(timerItems = listOf(TimerItem(listOf(1,2,3), LocalDateTime.now()))),
    onEvent: (ActiveTimersEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        columns = GridCells.Fixed(2),
        content = {
            items(state.timerItems) { timerItem ->
                TimerCard(modifier = Modifier.combinedClickable(
                    onClick = {},
                    onLongClick = {
                        onEvent(ActiveTimersEvent.onCardLongClick(timerItem))
                    }
                ),
                    numbers = timerItem.selectedNumbers,
                    time = getDuration(timerItem.time))
            }
        },
    )
}
