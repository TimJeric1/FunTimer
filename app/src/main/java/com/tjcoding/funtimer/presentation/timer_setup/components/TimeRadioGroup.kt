package com.tjcoding.funtimer.presentation.timer_setup.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.tjcoding.funtimer.ui.theme.FunTimerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeRadioGroup(
    radioOptions: List<String>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    onLongClick: (Int) -> Unit,
    onDoubleClick: (Int) -> Unit
) {
    Row(
        Modifier
            .selectableGroup()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        radioOptions.forEachIndexed { index, text ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RadioButton(
                    modifier = Modifier.combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            bounded = false,
                            // this is copied from radioButton implementation
                            radius = 20.dp
                        ),
                        onClick = { onOptionSelected(index) },
                        onLongClick = {onLongClick(index)},
                        onDoubleClick = {onDoubleClick(index)},
                        role = Role.RadioButton
                    ),
                    selected = (index == selectedOption),
                    onClick = null, // null recommended for accessibility with screenreaders
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
fun TimeRadioGroupPreview() {
    FunTimerTheme {
        Surface {
            TimeRadioGroup(
                radioOptions = listOf("30 min", "60 min", "custom"),
                selectedOption = 0,
                onOptionSelected = {},
                onLongClick = {},
                onDoubleClick = {}
            )
        }
    }
}