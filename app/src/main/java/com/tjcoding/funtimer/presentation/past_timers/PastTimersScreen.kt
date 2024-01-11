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
@Preview
fun PastTimersScreen(
    modifier: Modifier = Modifier,
    state: PastTimersState = PastTimersState(
        pastTimerItemsUi = listOf(
            PastTimerItemUi(
                listOf(1, 2, 3),
                LocalDateTime.now(),
            )
        )
    ),
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
            state.pastTimerItemsUi,
            onLongClick = {},
        )
    }

}


@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun PastTimerCardsVerticalGrid(
    modifier: Modifier,
    pastTimerItemsUi: List<PastTimerItemUi>,
    onLongClick: (PastTimerItemUi) -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    CustomItemsVerticalGrid(modifier = modifier, items = pastTimerItemsUi) { pastTimerItemUi ->
        PastTimerCard(modifier = Modifier
            .size(screenHeight.dp * 0.25f)
            .combinedClickable(
                onClick = {},
                onLongClick = { onLongClick(pastTimerItemUi) }
            ),
            numbers = pastTimerItemUi.selectedNumbers,
            time = "${pastTimerItemUi.triggerTime.hour}:${pastTimerItemUi.triggerTime.minute}",
            extraTime = null,
            )
    }

}