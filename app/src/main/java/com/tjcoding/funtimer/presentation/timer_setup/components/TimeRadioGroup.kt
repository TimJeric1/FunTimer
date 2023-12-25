package com.tjcoding.funtimer.presentation.timer_setup.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
                        onClick = {onOptionSelected(index)},
                        onLongClick = {onLongClick(index)},
                        onDoubleClick = {onDoubleClick(index)},
                    ),
                    selected = (index == selectedOption),
                    onClick = null, // null recommended for accessibility with screenreaders
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall.merge(),
                )
            }
        }
    }
}