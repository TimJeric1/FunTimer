package com.tjcoding.funtimer.presentation.past_timers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tjcoding.funtimer.presentation.components.CustomItemsVerticalGrid
import com.tjcoding.funtimer.presentation.components.PastTimerCard
import com.tjcoding.funtimer.ui.theme.FunTimerTheme
import com.tjcoding.funtimer.utility.Util.ObserveAsEvents
import com.tjcoding.funtimer.utility.Util.formatTo24HourAndMinute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@Composable
fun PastTimersScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: PastTimersViewModel = hiltViewModel()
) {
    PastTimersScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        modifier = modifier,
        shouldShowSnackbarWithTextStream = viewModel.shouldShowSnackbarWithTextStream,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastTimersScreen(
    modifier: Modifier = Modifier,
    state: PastTimersState,
    shouldShowSnackbarWithTextStream: Flow<String>
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(stream = shouldShowSnackbarWithTextStream) { text ->
        scope.launch {
            snackbarHostState
                .showSnackbar(
                    message = text,
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
        }
    }



    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Fun Timer")
                },
            )
        },
    ) { paddingValues ->

        if (state.pastTimerItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(), // Fill the entire screen
                contentAlignment = Alignment.Center // Center content within the Box
            ) {
                Text(
                    text = "No past timers",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        } else {
            PastTimerCardsVerticalGrid(
                Modifier.padding(paddingValues),
                state.pastTimerItems,
                onLongClick = {},
            )
        }
    }
}


@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun PastTimerCardsVerticalGrid(
    modifier: Modifier,
    pastTimerItems: List<PastTimerItem>,
    onLongClick: (PastTimerItem) -> Unit,
) {
    CustomItemsVerticalGrid(
        modifier = modifier,
        items = pastTimerItems,
        key = { pastTimerItem -> pastTimerItem.hashCode() }) { lazyListModifier, pastTimerItem ->
        PastTimerCard(modifier = lazyListModifier
            .aspectRatio(7 / 8f)
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
@PreviewLightDark
private fun PastTimersScreenPreview() {
    FunTimerTheme {
        Surface(
            tonalElevation = 5.dp
        ) {
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
                ),
                shouldShowSnackbarWithTextStream = flowOf("")
            )
        }
    }

}

@Composable
@PreviewLightDark
private fun PastTimersScreenEmptyPreview() {
    FunTimerTheme {
        Surface(
            tonalElevation = 5.dp
        ) {
            PastTimersScreen(
                modifier = Modifier,
                state = PastTimersState(
                    pastTimerItems = emptyList()
                ),
                shouldShowSnackbarWithTextStream = flowOf("")
            )
        }
    }

}
