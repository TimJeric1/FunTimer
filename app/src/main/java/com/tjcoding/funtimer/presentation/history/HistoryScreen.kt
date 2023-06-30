package com.tjcoding.funtimer.presentation.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tjcoding.funtimer.presentation.timer.components.TimerCard
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(modifier: Modifier = Modifier, viewModel: HistoryViewModel = hiltViewModel()) {
    val timerItems = viewModel.timerItems
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        columns = GridCells.Fixed(2),
        content = {
            items(timerItems) { timerItem ->
                TimerCard(modifier = Modifier.combinedClickable(
                    onClick = {},
                    onLongClick = {
                        viewModel.onEvent(HistoryEvent.onCardLongClick(timerItem))
                    }
                ), numbers = timerItem.selectedNumbers, time = viewModel.getDuration(timerItem.time))
            }
        },
    )
}