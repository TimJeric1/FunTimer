package com.tjcoding.funtimer.presentation.past_timers

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
import com.tjcoding.funtimer.presentation.components.CustomItemsVerticalGrid
import com.tjcoding.funtimer.presentation.components.PastTimerCard
import com.tjcoding.funtimer.utility.Util.formatTo24HourAndMinute
import java.time.LocalDateTime


@Composable
fun PastTimersScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: PastTimersViewModel = hiltViewModel()
) {
    PastTimersScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastTimersScreen(
    modifier: Modifier = Modifier,
    state: PastTimersState,
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
        PastTimerCardsVerticalGrid(
            modifier.padding(paddingValues),
            state.pastTimerItems,
            onLongClick = {},
        )
    }

}


@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun PastTimerCardsVerticalGrid(
    modifier: Modifier,
    pastTimerItems: List<PastTimerItem>,
    onLongClick: (PastTimerItem) -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    CustomItemsVerticalGrid(modifier = modifier, items = pastTimerItems) { pastTimerItem ->
        PastTimerCard(modifier = Modifier
            .size(screenHeight.dp * 0.25f)
            .padding(4.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = { onLongClick(pastTimerItem) }
            ),
            numbers = pastTimerItem.selectedNumbers,
            time = pastTimerItem.triggerTime.formatTo24HourAndMinute(),
            onNumberBoxClick = {}
            )
    }

}

@Composable
@Preview
private fun PastTimersScreenPreview() {
    PastTimersScreen(
        modifier = Modifier,
        state = PastTimersState(
            pastTimerItems = listOf(
                PastTimerItem(
                    listOf(1, 2, 3),
                    LocalDateTime.now(),
                ),
                PastTimerItem(
                    listOf(5, 6, 7, 8, 9, 10, 11, 12, 13, 14),
                    LocalDateTime.now().minusHours(1)
                ),
                PastTimerItem(
                    listOf(5, 6, 7, 8, 9, 10, 11, 12, 13, 14),
                    LocalDateTime.now().minusHours(1)
                ),
                PastTimerItem(
                    listOf(5, 6, 7, 8, 9, 10, 11, 12, 13, 14),
                    LocalDateTime.now().minusHours(1)
                ),
            )
        )
    )
}
