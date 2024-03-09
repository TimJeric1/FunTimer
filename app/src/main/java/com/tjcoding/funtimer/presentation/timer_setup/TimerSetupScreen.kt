package com.tjcoding.funtimer.presentation.timer_setup

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun TimerSetupScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: TimerSetupViewModel = hiltViewModel()
) {

    TimerSetupScreen(
        modifier = modifier,
        onEvent = viewModel::onEvent,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        shouldShowCustomTimePickerDialogStream = viewModel.shouldShowCustomTimePickerDialogStream,
        shouldShowExtraTimePickerDialogStream = viewModel.shouldShowExtraTimePickerDialogStream
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

) {


    val shouldShowCustomTimePickerDialog = remember { mutableStateOf(false) }
    val shouldShowExtraTimePickerDialog = remember { mutableStateOf(false) }
    var radioOptions = state.displayedDurations.values.map { if (it == -1) "custom" else "$it min" }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    LaunchedEffect(key1 = state.displayedDurations.values) {
        radioOptions = state.displayedDurations.values.map { if (it == -1) "custom" else "$it min" }
    }

    ObserveAsEvents(stream = shouldShowCustomTimePickerDialogStream) { shouldShowCustomTimePickerDialogNew ->
        shouldShowCustomTimePickerDialog.value = shouldShowCustomTimePickerDialogNew
    }
    ObserveAsEvents(stream = shouldShowExtraTimePickerDialogStream) { shouldShowExtraTimePickerDialogNew ->
        shouldShowExtraTimePickerDialog.value = shouldShowExtraTimePickerDialogNew
    }


    Scaffold(
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
        if (shouldShowCustomTimePickerDialog.value) {
            CustomTimePickerAlertDialog(
                state = state,
                pickerState = rememberPickerState(),
                onEvent = onEvent,
                onDismissRequest = { shouldShowCustomTimePickerDialog.value = false })
        }
        if (shouldShowExtraTimePickerDialog.value) {
            ExtraTimePickerAlertDialog(
                state = state,
                pickerState = rememberPickerState(),
                onEvent = onEvent,
                onDismissRequest = { shouldShowExtraTimePickerDialog.value = false })
        }
    }


}


@Composable
private fun StandardLayout(
    modifier: Modifier,
    paddingValues: PaddingValues,
    screenHeight: Int,
    state: TimerSetupState,
    onEvent: (TimerSetupEvent) -> Unit,
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
        TimerCard(
            modifier = Modifier
                .height(screenHeight.dp * 0.25f)
                .aspectRatio(7/8f),
            numbers = state.selectedNumbers,
            time = state.getDurationInTimeFormat(),
            extraTime = state.getExtraTimeInTimeFormat(),
            onNumberBoxClick = { number: Int -> onEvent(TimerSetupEvent.OnSelectedNumberClick(number)) }
        )


    }
}

@Composable
private fun AlternativeLayout(
    modifier: Modifier,
    paddingValues: PaddingValues,
    screenHeight: Int,
    state: TimerSetupState,
    onEvent: (TimerSetupEvent) -> Unit,
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
            onNumberBoxClick = { number: Int -> onEvent(TimerSetupEvent.OnSelectedNumberClick(number)) }
        )
        NumberSelector(
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
private fun <T> ObserveAsEvents(stream: Flow<T>, onEvent: (T) -> Unit) {
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
            TimerSetupScreen(
                modifier = Modifier,
                state = TimerSetupState(),
                onEvent = {},
                shouldShowCustomTimePickerDialogStream = flowOf(false),
                shouldShowExtraTimePickerDialogStream = flowOf(false)
            )
        }
    }

}

