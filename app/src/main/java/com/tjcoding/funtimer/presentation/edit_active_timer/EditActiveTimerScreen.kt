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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import com.tjcoding.funtimer.presentation.common.DurationOption
import com.tjcoding.funtimer.presentation.common.LayoutView
import com.tjcoding.funtimer.presentation.common.toIndex
import com.tjcoding.funtimer.presentation.components.AlarmAndExtraTimeCountdown
import com.tjcoding.funtimer.presentation.components.BasicTimerCard
import com.tjcoding.funtimer.presentation.timer_setup.components.PickerAlertDialog
import com.tjcoding.funtimer.presentation.timer_setup.components.PickerState
import com.tjcoding.funtimer.presentation.timer_setup.components.rememberPickerState
import com.tjcoding.funtimer.ui.theme.FunTimerTheme
import com.tjcoding.funtimer.utility.Util.ObserveAsEvents
import com.tjcoding.funtimer.utility.Util.SecondsFormatTommss
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID


@Composable
fun EditActiveTimerScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: EditActiveTimerViewModel = hiltViewModel(),
    timerItemIdAsString: String,
    navController: NavController
) {
    EditActiveTimerScreen(
        modifier = modifier,
        onEvent = viewModel::onEvent,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        shouldShowCustomTimePickerDialogStream = viewModel.shouldShowCustomTimePickerDialogStream,
        shouldNavigateUpStream = viewModel.shouldNavigateUpStream,
        timerItemId = UUID.fromString(timerItemIdAsString),
        navigateUp = { navController.navigateUp() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditActiveTimerScreen(
    modifier: Modifier = Modifier,
    state: EditActiveTimerState,
    onEvent: (EditActiveTimerEvent) -> Unit,
    shouldShowCustomTimePickerDialogStream: Flow<Boolean>,
    shouldNavigateUpStream: Flow<Boolean>,
    timerItemId: UUID,
    navigateUp: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        onEvent(EditActiveTimerEvent.OnScreenLaunch(timerItemId))
    }

    var shouldShowCustomTimePickerDialog by remember { mutableStateOf(false) }
    var radioOptions = state.displayedDurations.values.map {
        if (it == -1) "custom"
        else {
            if (it > 0) "+$it min"
            else "$it min"
        }
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp

    LaunchedEffect(key1 = state.displayedDurations.values) {
        radioOptions = state.displayedDurations.values.map { if (it == -1) "custom" else "$it min" }
    }

    ObserveAsEvents(stream = shouldShowCustomTimePickerDialogStream) { shouldShowCustomTimePickerDialogNew ->
        shouldShowCustomTimePickerDialog = shouldShowCustomTimePickerDialogNew
    }

    ObserveAsEvents(stream = shouldNavigateUpStream) { shouldNavigateUp ->
        if (shouldNavigateUp) navigateUp()
    }


    Scaffold(
        topBar = {

            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { onEvent(EditActiveTimerEvent.OnLayoutViewIconClick) }) {
                        Icon(
                            imageVector = if (state.selectedLayoutView == LayoutView.STANDARD) Icons.Default.ViewCompact else Icons.Default.ViewCompactAlt,
                            contentDescription = "layout view icon"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
            )
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
    }


}


@Composable
private fun StandardLayout(
    modifier: Modifier,
    paddingValues: PaddingValues,
    screenHeight: Int,
    state: EditActiveTimerState,
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
        AlarmAndExtraTimeCountdown(
            alarmTime = state.editedActiveTimerItem.alarmTime,
            triggerTime = state.editedActiveTimerItem.triggerTime
        ) { countDownAlarmTime, countDownExtraTime ->
            EditActiveTimerCard(
                modifier = Modifier
                    .height(screenHeight.dp * 0.25f)
                    .aspectRatio(7 / 8f),
                numbers = state.editedActiveTimerItem.selectedNumbers,
                time = countDownAlarmTime.SecondsFormatTommss(),
                extraTime = countDownExtraTime.SecondsFormatTommss(),
                onNumberBoxClick = { number: Int ->
                    onEvent(
                        EditActiveTimerEvent.OnSelectedNumberClick(
                            number
                        )
                    )
                },
                onBackspaceIconClick = { onEvent(EditActiveTimerEvent.OnBackspaceIconClick) },
                onRestartIconClick = { onEvent(EditActiveTimerEvent.OnRestartIconClick) },
            )
        }


    }
}

@Composable
private fun AlternativeLayout(
    modifier: Modifier,
    paddingValues: PaddingValues,
    screenHeight: Int,
    state: EditActiveTimerState,
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
        AlarmAndExtraTimeCountdown(
            alarmTime = state.editedActiveTimerItem.alarmTime,
            triggerTime = state.editedActiveTimerItem.triggerTime
        ) { countDownAlarmTime, countDownExtraTime ->
            EditActiveTimerCard(
                modifier = Modifier
                    .height(screenHeight.dp * 0.25f)
                    .aspectRatio(7 / 8f),
                numbers = state.editedActiveTimerItem.selectedNumbers,
                time = countDownAlarmTime.SecondsFormatTommss(),
                extraTime = countDownExtraTime.SecondsFormatTommss(),
                onNumberBoxClick = { number: Int ->
                    onEvent(
                        EditActiveTimerEvent.OnSelectedNumberClick(
                            number
                        )
                    )
                },
                onBackspaceIconClick = { onEvent(EditActiveTimerEvent.OnBackspaceIconClick) },
                onRestartIconClick = { onEvent(EditActiveTimerEvent.OnRestartIconClick) },
            )
        }

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


    }
}

@Composable
fun CustomTimePickerAlertDialog(
    state: EditActiveTimerState,
    pickerState: PickerState,
    onEvent: (EditActiveTimerEvent) -> Unit,
    onDismissRequest: () -> Unit
) {
    val items = (-30..30 step 5).toList()
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
        items = items.map {
            if (it > 0) "+$it"
            else "$it"
        },
        startIndex = startIndex,
        titleText = "Input your custom time"
    )

}

@Composable
fun EditActiveTimerCard(
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
private fun EditActiveTimerScreenPreview() {
    FunTimerTheme {
        Surface(
            tonalElevation = 2.dp
        ) {
            EditActiveTimerScreen(
                modifier = Modifier,
                state = EditActiveTimerState(),
                onEvent = {},
                shouldShowCustomTimePickerDialogStream = flowOf(false),
                shouldNavigateUpStream = flowOf(false),
                timerItemId = UUID.randomUUID(),
                navigateUp = {}
            )
        }
    }
}

