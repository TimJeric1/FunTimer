package com.tjcoding.funtimer.presentation.active_timers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.tjcoding.funtimer.domain.model.TimerItem
import com.tjcoding.funtimer.presentation.timer_setup.components.TimerCard
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ActiveTimersScreen(
    modifier: Modifier = Modifier,
    state: ActiveTimersState = ActiveTimersState(
        timerItems = listOf(
            TimerItem(
                listOf(1, 2, 3),
                LocalDateTime.now(),
                2
            )
        )
    ),
    onEvent: (ActiveTimersEvent) -> Unit = {}
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Fun Timer")
                },
            )
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            columns = GridCells.Fixed(2),
            content = {
                items(state.timerItems) { timerItem ->
                    TimerCard(modifier = Modifier
                        .size(screenHeight.dp * 0.25f)
                        .combinedClickable(
                            onClick = {},
                            onLongClick = {
                                onEvent(ActiveTimersEvent.OnCardLongClick(timerItem))
                            }
                        ),
                        numbers = timerItem.selectedNumbers,
                        time = getDuration(timerItem.alarmTime),
                        extraTime = "${timerItem.extraTime}:00")
                }
            },
        )
    }

}

