package com.tjcoding.funtimer.presentation.timer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.tjcoding.funtimer.presentation.timer.components.NumberSelector
import com.tjcoding.funtimer.presentation.timer.components.TimeRadioGroup
import com.tjcoding.funtimer.presentation.timer.components.TimerCard

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = hiltViewModel()
) {


    val radioOptions = listOf("30 min", "60 min", "Custom")

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        NumberSelector(
            displayedNumber = viewModel.state.displayedNumber,
            onLeftFilledArrowClick = {
                viewModel.onEvent(TimerEvent.onLeftFilledArrowClick)
            },
            onRightFilledArrowClick = {
                viewModel.onEvent(TimerEvent.onRightFilledArrowClick)
            }
        )
        TimeRadioGroup(
            radioOptions = radioOptions,
            selectedOption = DurationOption.durationOptionToIndex(viewModel.state.durationOption),
            onOptionSelected = { index ->
                viewModel.onEvent(
                    TimerEvent.onDurationRadioButtonClick(
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
                viewModel.onEvent(TimerEvent.onAddButtonClick)
            }) {
                Text(text = "Add")
            }
            OutlinedButton(onClick = {
                viewModel.onEvent(TimerEvent.onSaveButtonClick)
            }) {
                Text(text = "Save")
            }
        }
        TimerCard(
            numbers = viewModel.state.selectedNumbers,
            time = DurationOption.durationOptionToDuration(
                viewModel.state.durationOption,
                viewModel.state.durations
            ),
            onNumberBoxClick = { number: Int -> viewModel.onEvent(TimerEvent.onSelectedNumberClick(number)) }
        )

    }


}


