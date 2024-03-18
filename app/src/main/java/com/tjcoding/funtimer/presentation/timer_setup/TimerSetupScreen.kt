package com.tjcoding.funtimer.presentation.timer_setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.ViewCompact
import androidx.compose.material.icons.filled.ViewCompactAlt
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import com.tjcoding.funtimer.presentation.timer_setup.components.NumberSelector
import com.tjcoding.funtimer.presentation.timer_setup.components.TimeRadioGroup
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView
import com.tjcoding.funtimer.presentation.common.toIndex
import com.tjcoding.funtimer.presentation.components.BasicTimerCard
import com.tjcoding.funtimer.presentation.timer_setup.components.PickerAlertDialog
import com.tjcoding.funtimer.presentation.timer_setup.components.PickerState
import com.tjcoding.funtimer.presentation.timer_setup.components.rememberPickerState
import com.tjcoding.funtimer.ui.theme.FunTimerTheme
import com.tjcoding.funtimer.utility.Util.ObserveAsEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@Composable
fun TimerSetupScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: TimerSetupViewModel = hiltViewModel()
) {

    TimerSetupScreen(
        modifier = modifier,
        onEvent = viewModel::onEvent,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        shouldShowCustomTimePickerDialogStream = viewModel.shouldShowCustomTimePickerDialogStream,
        shouldShowExtraTimePickerDialogStream = viewModel.shouldShowExtraTimePickerDialogStream,
        shouldShowSnackbarWithTextStream = viewModel.shouldShowSnackbarWithTextStream
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerSetupScreen(
    modifier: Modifier = Modifier,
    state: TimerSetupState,
    onEvent: (TimerSetupEvent) -> Unit,
    shouldShowCustomTimePickerDialogStream: Flow<Boolean>,
    shouldShowExtraTimePickerDialogStream: Flow<Boolean>,
    shouldShowSnackbarWithTextStream: Flow<String>,

) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var shouldShowCustomTimePickerDialog by remember { mutableStateOf(false) }

    var shouldShowExtraTimePickerDialog by remember { mutableStateOf(false) }

    var radioOptions = state.displayedDurations.values.map { if (it == -1) "custom" else "$it min" }

    val screenHeight = LocalConfiguration.current.screenHeightDp

    LaunchedEffect(key1 = state.displayedDurations.values) {
        radioOptions = state.displayedDurations.values.map { if (it == -1) "custom" else "$it min" }
    }

    ObserveAsEvents(stream = shouldShowCustomTimePickerDialogStream) { shouldShowCustomTimePickerDialogNew ->
        shouldShowCustomTimePickerDialog = shouldShowCustomTimePickerDialogNew
    }
    ObserveAsEvents(stream = shouldShowExtraTimePickerDialogStream) { shouldShowExtraTimePickerDialogNew ->
        shouldShowExtraTimePickerDialog = shouldShowExtraTimePickerDialogNew
    }
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
            TopAppBar(title = {
                Text(text = "Fun Timer")
            },
                actions = {
                    IconButton(onClick = { onEvent(TimerSetupEvent.OnLayoutViewIconClick) }) {
                        Icon(
                            imageVector = if (state.selectedLayoutView == LayoutView.STANDARD) Icons.Default.ViewCompact else Icons.Default.ViewCompactAlt,
                            contentDescription = "layout view icon"
                        )
                    }
                    IconButton(onClick = { onEvent(TimerSetupEvent.OnExtraTimeIconClick) }) {
                        Text(text = "ET")
                    }

                })
        },
    ) { paddingValues ->
        if (state.selectedLayoutView == LayoutView.STANDARD)
            StandardLayout(
                Modifier.fillMaxSize().padding(paddingValues),
                state,
                onEvent,
                radioOptions
            )
        else
            AlternativeLayout(
                Modifier.fillMaxSize(),
                screenHeight,
                state,
                onEvent,
                radioOptions
            )
        if (shouldShowCustomTimePickerDialog) {
            CustomTimePickerAlertDialog(
                state = state,
                pickerState = rememberPickerState(),
                onEvent = onEvent,
                onDismissRequest = { shouldShowCustomTimePickerDialog = false })
        }
        if (shouldShowExtraTimePickerDialog) {
            ExtraTimePickerAlertDialog(
                state = state,
                pickerState = rememberPickerState(),
                onEvent = onEvent,
                onDismissRequest = { shouldShowExtraTimePickerDialog = false })
        }
    }


}


@Composable
private fun StandardLayout(
    modifier: Modifier,
    state: TimerSetupState,
    onEvent: (TimerSetupEvent) -> Unit,
    radioOptions: List<String>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        NumberSelector(
            modifier = Modifier.fillMaxWidth(0.7f),
            displayedNumber = state.displayedNumber,
            onLeftFilledArrowClick = {
                onEvent(TimerSetupEvent.OnLeftFilledArrowClick)
            },
            onRightFilledArrowClick = {
                onEvent(TimerSetupEvent.OnRightFilledArrowClick)
            }
        )
        TimeRadioGroup(
            radioOptions = radioOptions,
            selectedOption = state.selectedDurationOption.toIndex(),
            onOptionSelected = { index ->
                onEvent(
                    TimerSetupEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
            },
            onLongClick = { index ->
                onEvent(
                    TimerSetupEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
                onEvent(TimerSetupEvent.OnDurationRadioButtonLongClick)
            },
            onDoubleClick = { index ->
                onEvent(
                    TimerSetupEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
                onEvent(TimerSetupEvent.OnDurationRadioButtonLongClick)
            }
        )
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = {
                onEvent(TimerSetupEvent.OnAddButtonClick)
            }) {
                Text(text = "Add")
            }
            Button(onClick = {
                onEvent(TimerSetupEvent.OnSaveButtonClick)
            }) {
                Text(text = "Save")
            }
        }
        TimerSetupTimerCard(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(10.dp)
                .aspectRatio(7 / 8f),
            numbers = state.selectedNumbers,
            time = state.getDurationInTimeFormat(),
            extraTime = state.getExtraTimeInTimeFormat(),
            onNumberBoxClick = { number: Int -> onEvent(TimerSetupEvent.OnSelectedNumberClick(number)) },
            onBackspaceIconClick = { onEvent(TimerSetupEvent.OnBackspaceIconClick) },
            onRestartIconClick = { onEvent(TimerSetupEvent.OnRestartIconClick) }
        )


    }
}

@Composable
private fun AlternativeLayout(
    modifier: Modifier,
    screenHeight: Int,
    state: TimerSetupState,
    onEvent: (TimerSetupEvent) -> Unit,
    radioOptions: List<String>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TimerSetupTimerCard(
            modifier = Modifier
                .height(screenHeight.dp * 0.25f)
                .aspectRatio(7 / 8f),
            numbers = state.selectedNumbers,
            time = state.getDurationInTimeFormat(),
            extraTime = state.getExtraTimeInTimeFormat(),
            onNumberBoxClick = { number: Int -> onEvent(TimerSetupEvent.OnSelectedNumberClick(number)) },
            onBackspaceIconClick = { onEvent(TimerSetupEvent.OnBackspaceIconClick) },
            onRestartIconClick = { onEvent(TimerSetupEvent.OnRestartIconClick) },
        )
        NumberSelector(
            modifier = Modifier.fillMaxWidth(0.7f),
            displayedNumber = state.displayedNumber,
            onLeftFilledArrowClick = {
                onEvent(TimerSetupEvent.OnLeftFilledArrowClick)
            },
            onRightFilledArrowClick = {
                onEvent(TimerSetupEvent.OnRightFilledArrowClick)
            }
        )
        TimeRadioGroup(
            radioOptions = radioOptions,
            selectedOption = state.selectedDurationOption.toIndex(),
            onOptionSelected = { index ->
                onEvent(
                    TimerSetupEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
            },
            onLongClick = { index ->
                onEvent(
                    TimerSetupEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
                onEvent(TimerSetupEvent.OnDurationRadioButtonLongClick)
            },
            onDoubleClick = { index ->
                onEvent(
                    TimerSetupEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
                onEvent(TimerSetupEvent.OnDurationRadioButtonLongClick)
            }
        )
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = {
                onEvent(TimerSetupEvent.OnAddButtonClick)
            }) {
                Text(text = "Add")
            }
            Button(onClick = {
                onEvent(TimerSetupEvent.OnSaveButtonClick)
            }) {
                Text(text = "Save")
            }
        }


    }
}

@Composable
fun CustomTimePickerAlertDialog(
    state: TimerSetupState,
    pickerState: PickerState,
    onEvent: (TimerSetupEvent) -> Unit,
    onDismissRequest: () -> Unit
) {
    val items = (5..100 step 5).toList()
    val startIndex =
        if (items.indexOf(state.displayedDurations[state.selectedDurationOption]) == -1) 0 else items.indexOf(
            state.displayedDurations[state.selectedDurationOption]
        )
    PickerAlertDialog(
        pickerState = pickerState,
        onConfirmRequest = { pickedItem ->
            onEvent(TimerSetupEvent.OnCustomDurationPicked(pickedItem.toInt()))
        },
        onDismissRequest = onDismissRequest,
        items = items.map { it.toString() },
        startIndex = startIndex,
        titleText = "Input your custom time"
    )

}

@Composable
fun ExtraTimePickerAlertDialog(
    state: TimerSetupState,
    pickerState: PickerState,
    onEvent: (TimerSetupEvent) -> Unit,
    onDismissRequest: () -> Unit
) {
    val items = (1..10).toList()
    PickerAlertDialog(
        pickerState = pickerState,
        onConfirmRequest = { pickedItem ->
            onEvent(TimerSetupEvent.OnExtraTimePicked(pickedItem.toInt()))
        },
        onDismissRequest = onDismissRequest,
        items = items.map { it.toString() },
        startIndex = items.indexOf(state.selectedExtraTime),
        titleText = "Input your extra time"
    )
}

@Composable
fun TimerSetupTimerCard(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    time: String,
    extraTime: String?,
    onNumberBoxClick: (Int) -> Unit,
    onBackspaceIconClick: () -> Unit,
    onRestartIconClick: () -> Unit,
) {
    BasicTimerCard(modifier = modifier, numbers = numbers,
        onNumberBoxClick = onNumberBoxClick,
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
                IconButton(modifier = Modifier.size(32.dp), onClick = onBackspaceIconClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                        modifier = Modifier.size(18.dp),
                        contentDescription = null
                    )
                }
                Icon(
                    modifier = Modifier.padding(vertical = 2.dp),
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = null
                )
                IconButton(modifier = Modifier.size(32.dp), onClick = onRestartIconClick) {
                    Icon(
                        imageVector = Icons.Filled.RestartAlt,
                        modifier = Modifier.size(20.dp),
                        contentDescription = null
                    )
                }
            }

        }, bottom = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = CenterHorizontally,
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
private fun TimerSetupScreenPreview() {
    FunTimerTheme {
        Surface(
            tonalElevation = 2.dp
        ) {
            TimerSetupScreen(
                modifier = Modifier,
                state = TimerSetupState(),
                onEvent = {},
                shouldShowCustomTimePickerDialogStream = flowOf(false),
                shouldShowExtraTimePickerDialogStream = flowOf(false),
                shouldShowSnackbarWithTextStream = flowOf("")
            )
        }
    }

}

