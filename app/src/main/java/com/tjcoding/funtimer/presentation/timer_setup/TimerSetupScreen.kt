package com.tjcoding.funtimer.presentation.timer_setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.tjcoding.funtimer.presentation.timer_setup.components.NumberSelector
import com.tjcoding.funtimer.presentation.timer_setup.components.TimeRadioGroup
import com.tjcoding.funtimer.presentation.timer_setup.components.TimerCard
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tjcoding.funtimer.presentation.timer_setup.components.Picker
import com.tjcoding.funtimer.presentation.timer_setup.components.rememberPickerState


@Composable
fun TimerSetupScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: TimerSetupViewModel = hiltViewModel()
) {
    TimerSetupScreen(
        modifier = modifier,
        onEvent = viewModel::onEvent,
        state = viewModel.state.collectAsStateWithLifecycle().value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TimerSetupScreen(
    modifier: Modifier = Modifier,
    state: TimerSetupState = TimerSetupState(),
    onEvent: (TimerSetupEvent) -> Unit = {},
) {
    val pickerState = rememberPickerState()
    val openDialog = remember { mutableStateOf(true) }
    val radioOptions = listOf("30 min", "60 min", "Custom")
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        NumberSelector(
            displayedNumber = state.displayedNumber,
            onLeftFilledArrowClick = {
                onEvent(TimerSetupEvent.onLeftFilledArrowClick)
            },
            onRightFilledArrowClick = {
                onEvent(TimerSetupEvent.onRightFilledArrowClick)
            }
        )
        TimeRadioGroup(
            radioOptions = radioOptions,
            selectedOption = state.durationOption.toIndex(),
            onOptionSelected = { index ->
                onEvent(
                    TimerSetupEvent.onDurationRadioButtonClick(
                        duration = DurationOption.indexToDurationOption(
                            index
                        )
                    )
                )
            }
        )
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = {
                onEvent(TimerSetupEvent.onAddButtonClick)
            }) {
                Text(text = "Add")
            }
            OutlinedButton(onClick = {
                onEvent(TimerSetupEvent.onSaveButtonClick)
            }) {
                Text(text = "Save")
            }
        }
        TimerCard(
            numbers = state.selectedNumbers,
            time = state.getDurationInTimeFormat(),
            onNumberBoxClick = { number: Int -> onEvent(TimerSetupEvent.onSelectedNumberClick(number)) }
        )

    }
    if (openDialog.value) {
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
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Picker(
                            modifier = Modifier.fillMaxWidth(0.25f),
                            state = pickerState,
                            items = (5..999 step 5).toList().map { it.toString() })
                        Text(text = "min")
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = {
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

}








