package com.tjcoding.funtimer.presentation.edit_active_timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewCompact
import androidx.compose.material.icons.filled.ViewCompactAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import com.tjcoding.funtimer.presentation.timer_setup.components.NumberSelector
import com.tjcoding.funtimer.presentation.timer_setup.components.TimeRadioGroup
import com.tjcoding.funtimer.presentation.components.TimerCard
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.tjcoding.funtimer.presentation.timer_setup.components.PickerAlertDialog
import com.tjcoding.funtimer.presentation.timer_setup.components.PickerState
import com.tjcoding.funtimer.presentation.timer_setup.components.rememberPickerState
import com.tjcoding.funtimer.ui.theme.FunTimerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext


@Composable
fun EditActiveTimerScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: EditActiveTimerViewModel = hiltViewModel(),
    id: Int,
) {

    EditActiveTimerScreen(
        modifier = modifier,
        onEvent = viewModel::onEvent,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        shouldShowCustomTimePickerDialogStream = viewModel.shouldShowCustomTimePickerDialogStream,
        shouldShowExtraTimePickerDialogStream = viewModel.shouldShowExtraTimePickerDialogStream
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditActiveTimerScreen(
    modifier: Modifier = Modifier,
    state: TimerSetupState,
    onEvent: (EditActiveTimerEvent) -> Unit,
    shouldShowCustomTimePickerDialogStream: Flow<Boolean>,
    shouldShowExtraTimePickerDialogStream: Flow<Boolean>,

    ) {


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


    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Fun Timer")
            },
                actions = {
                    IconButton(onClick = { onEvent(EditActiveTimerEvent.OnLayoutViewIconClick) }) {
                        Icon(
                            imageVector = if (state.selectedLayoutView == LayoutView.STANDARD) Icons.Default.ViewCompact else Icons.Default.ViewCompactAlt,
                            contentDescription = "layout view icon"
                        )
                    }
                    IconButton(onClick = { onEvent(EditActiveTimerEvent.OnExtraTimeIconClick) }) {
                        Text(text = "ET")
                    }

                })
        },
    ) { paddingValues ->
        if (state.selectedLayoutView == LayoutView.STANDARD)
            StandardLayout(
                modifier,
                paddingValues,
                screenHeight,
                state,
                onEvent,
                radioOptions
            )
        else
            AlternativeLayout(
                modifier,
                paddingValues,
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
    paddingValues: PaddingValues,
    screenHeight: Int,
    state: TimerSetupState,
    onEvent: (EditActiveTimerEvent) -> Unit,
    radioOptions: List<String>
) {
    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        NumberSelector(
            modifier = Modifier.fillMaxWidth(0.7f),
            displayedNumber = state.displayedNumber,
            onLeftFilledArrowClick = {
                onEvent(EditActiveTimerEvent.OnLeftFilledArrowClick)
            },
            onRightFilledArrowClick = {
                onEvent(EditActiveTimerEvent.OnRightFilledArrowClick)
            }
        )
        TimeRadioGroup(
            radioOptions = radioOptions,
            selectedOption = state.selectedDurationOption.toIndex(),
            onOptionSelected = { index ->
                onEvent(
                    EditActiveTimerEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
            },
            onLongClick = { index ->
                onEvent(
                    EditActiveTimerEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
                onEvent(EditActiveTimerEvent.OnDurationRadioButtonLongClick)
            },
            onDoubleClick = { index ->
                onEvent(
                    EditActiveTimerEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
                onEvent(EditActiveTimerEvent.OnDurationRadioButtonLongClick)
            }
        )
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = {
                onEvent(EditActiveTimerEvent.OnAddButtonClick)
            }) {
                Text(text = "Add")
            }
            Button(onClick = {
                onEvent(EditActiveTimerEvent.OnSaveButtonClick)
            }) {
                Text(text = "Save")
            }
        }
        TimerCard(
            modifier = Modifier
                .height(screenHeight.dp * 0.25f)
                .aspectRatio(7/8f),
            numbers = state.selectedNumbers,
            time = state.getDurationInTimeFormat(),
            extraTime = state.getExtraTimeInTimeFormat(),
            onNumberBoxClick = { number: Int -> onEvent(EditActiveTimerEvent.OnSelectedNumberClick(number)) }
        )


    }
}

@Composable
private fun AlternativeLayout(
    modifier: Modifier,
    paddingValues: PaddingValues,
    screenHeight: Int,
    state: TimerSetupState,
    onEvent: (EditActiveTimerEvent) -> Unit,
    radioOptions: List<String>
) {
    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TimerCard(
            modifier = Modifier
                .height(screenHeight.dp * 0.25f)
                .aspectRatio(7/8f),
            numbers = state.selectedNumbers,
            time = state.getDurationInTimeFormat(),
            extraTime = state.getExtraTimeInTimeFormat(),
            onNumberBoxClick = { number: Int -> onEvent(EditActiveTimerEvent.OnSelectedNumberClick(number)) }
        )
        NumberSelector(
            displayedNumber = state.displayedNumber,
            onLeftFilledArrowClick = {
                onEvent(EditActiveTimerEvent.OnLeftFilledArrowClick)
            },
            onRightFilledArrowClick = {
                onEvent(EditActiveTimerEvent.OnRightFilledArrowClick)
            }
        )
        TimeRadioGroup(
            radioOptions = radioOptions,
            selectedOption = state.selectedDurationOption.toIndex(),
            onOptionSelected = { index ->
                onEvent(
                    EditActiveTimerEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
            },
            onLongClick = { index ->
                onEvent(
                    EditActiveTimerEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
                onEvent(EditActiveTimerEvent.OnDurationRadioButtonLongClick)
            },
            onDoubleClick = { index ->
                onEvent(
                    EditActiveTimerEvent.OnDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(index)
                    )
                )
                onEvent(EditActiveTimerEvent.OnDurationRadioButtonLongClick)
            }
        )
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = {
                onEvent(EditActiveTimerEvent.OnAddButtonClick)
            }) {
                Text(text = "Add")
            }
            Button(onClick = {
                onEvent(EditActiveTimerEvent.OnSaveButtonClick)
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
    onEvent: (EditActiveTimerEvent) -> Unit,
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
            onEvent(EditActiveTimerEvent.OnCustomDurationPicked(pickedItem.toInt()))
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
    onEvent: (EditActiveTimerEvent) -> Unit,
    onDismissRequest: () -> Unit
) {
    val items = (1..10).toList()
    PickerAlertDialog(
        pickerState = pickerState,
        onConfirmRequest = { pickedItem ->
            onEvent(EditActiveTimerEvent.OnExtraTimePicked(pickedItem.toInt()))
        },
        onDismissRequest = onDismissRequest,
        items = items.map { it.toString() },
        startIndex = items.indexOf(state.selectedExtraTime),
        titleText = "Input your extra time"
    )
}


@Composable
fun <T> ObserveAsEvents(stream: Flow<T>, onEvent: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(stream, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                stream.collect(onEvent)
            }
        }
    }
}



@Composable
@PreviewLightDark
private fun TimerSetupScreenPreview() {
    FunTimerTheme {
        Surface(
            tonalElevation = 2.dp
        ) {
            EditActiveTimerScreen(
                modifier = Modifier,
                state = TimerSetupState(),
                onEvent = {},
                shouldShowCustomTimePickerDialogStream = flowOf(false),
                shouldShowExtraTimePickerDialogStream = flowOf(false)
            )
        }
    }

}
