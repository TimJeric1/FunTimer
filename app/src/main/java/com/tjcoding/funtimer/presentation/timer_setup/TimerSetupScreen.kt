package com.tjcoding.funtimer.presentation.timer_setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewCompact
import androidx.compose.material.icons.filled.ViewCompactAlt
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import com.tjcoding.funtimer.presentation.timer_setup.components.NumberSelector
import com.tjcoding.funtimer.presentation.timer_setup.components.TimeRadioGroup
import com.tjcoding.funtimer.presentation.timer_setup.components.TimerCard
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.tjcoding.funtimer.presentation.timer_setup.components.Picker
import com.tjcoding.funtimer.presentation.timer_setup.components.PickerState
import com.tjcoding.funtimer.presentation.timer_setup.components.rememberPickerState
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
        shouldShowDialogStream = viewModel.shouldShowDialogStream
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TimerSetupScreen(
    modifier: Modifier = Modifier,
    state: TimerSetupState = TimerSetupState(),
    onEvent: (TimerSetupEvent) -> Unit = {},
    shouldShowDialogStream: Flow<Boolean> = flowOf(false)
) {


    val pickerState = rememberPickerState()
    val shouldOpenDialog = remember { mutableStateOf(false) }
    var radioOptions = state.displayedDurations.values.map { if (it == -1) "custom" else "$it min" }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    LaunchedEffect(key1 = state.displayedDurations.values) {
        radioOptions = state.displayedDurations.values.map { if (it == -1) "custom" else "$it min" }
    }

    ObserveAsEvents(stream = shouldShowDialogStream) { shouldShowDialog ->
        shouldOpenDialog.value = shouldShowDialog
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Fun Timer")
            },
                actions = {
                    IconButton(onClick = { onEvent(TimerSetupEvent.OnLayoutViewButtonClick) }) {
                        Icon(
                            imageVector = if(state.selectedLayoutView == LayoutView.STANDARD) Icons.Default.ViewCompact else Icons.Default.ViewCompactAlt,
                            contentDescription = "layout view icon"
                        )
                    }

                })
        },
    ) { paddingValues ->
        if (state.selectedLayoutView == LayoutView.STANDARD)
            StandardLayout(modifier, paddingValues, screenWidth, screenHeight, state, onEvent, radioOptions)
        else
            AlternativeLayout(modifier, paddingValues, screenWidth, screenHeight, state, onEvent, radioOptions)
        if (shouldOpenDialog.value) {
            customAlertDialog(state, pickerState, onEvent, shouldOpenDialog)
        }
    }


}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun customAlertDialog(
    state: TimerSetupState,
    pickerState: PickerState,
    onEvent: (TimerSetupEvent) -> Unit,
    openDialog: MutableState<Boolean>
) {
    BasicAlertDialog(
        onDismissRequest = {}
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = "Input your custom time",
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val items = (5..999 step 5).toList()
                    val startIndex =
                        if (items.indexOf(state.displayedDurations[DurationOption.CUSTOM]) == -1) 0 else items.indexOf(
                            state.displayedDurations[DurationOption.CUSTOM]
                        )
                    Picker(
                        modifier = Modifier.fillMaxWidth(0.25f),
                        state = pickerState,
                        items = items.map { it.toString() },
                        startIndex = startIndex
                    )
                    Text(text = "min")
                }
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onEvent(TimerSetupEvent.OnCustomDurationPicked(pickerState.selectedItem.toInt()))
                        openDialog.value = false
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

@Composable
private fun StandardLayout(
    modifier: Modifier,
    paddingValues: PaddingValues,
    screenWidth: Int,
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
                        duration = DurationOption.indexToDurationOption(
                            index
                        )
                    )
                )
            },
            onLongClick = { index ->
                onEvent(TimerSetupEvent.OnDurationRadioButtonLongClick(index = index))
            },
            onDoubleClick = { index ->
                onEvent(TimerSetupEvent.OnDurationRadioButtonLongClick(index = index))
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
                .width(screenWidth.dp * 0.5f - 12.dp)
                .height(screenHeight.dp * 0.25f - 6.dp),
            numbers = state.selectedNumbers,
            time = state.getDurationInTimeFormat(),
            onNumberBoxClick = { number: Int -> onEvent(TimerSetupEvent.OnSelectedNumberClick(number)) }
        )


    }
}

@Composable
private fun AlternativeLayout(
    modifier: Modifier,
    paddingValues: PaddingValues,
    screenWidth: Int,
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
                .width(screenWidth.dp * 0.5f - 12.dp)
                .height(screenHeight.dp * 0.25f - 6.dp),
            numbers = state.selectedNumbers,
            time = state.getDurationInTimeFormat(),
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
                        duration = DurationOption.indexToDurationOption(
                            index
                        )
                    )
                )
            },
            onLongClick = { index ->
                onEvent(TimerSetupEvent.OnDurationRadioButtonLongClick(index = index))
            },
            onDoubleClick = { index ->
                onEvent(TimerSetupEvent.OnDurationRadioButtonLongClick(index = index))
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





