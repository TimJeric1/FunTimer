package com.tjcoding.funtimer.presentation.timer_setup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.tjcoding.funtimer.presentation.timer_setup.components.NumberSelector
import com.tjcoding.funtimer.presentation.timer_setup.components.TimeRadioGroup
import com.tjcoding.funtimer.presentation.timer_setup.components.TimerCard
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle


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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun TimerSetupScreen(
    state: TimerSetupState = TimerSetupState(),
    onEvent: (TimerSetupEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
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

}








