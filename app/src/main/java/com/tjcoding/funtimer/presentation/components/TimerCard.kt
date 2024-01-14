package com.tjcoding.funtimer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
private fun TimerCardPreview() {
    TimerCard(
        numbers = listOf(1, 2, 3, 11, 12, 13),
        time = "30:00",
        extraTime = "2:00",
        onNumberBoxClick = {}
    )
}

@Composable
fun TimerCard(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    time: String,
    extraTime: String?,
    onNumberBoxClick: (Int) -> Unit
) {
    BasicTimerCard(modifier = modifier, numbers = numbers, onNumberBoxClick = onNumberBoxClick,
        top = {
            Icon(
                modifier = Modifier.padding(top = 4.dp),
                imageVector = Icons.Outlined.Timer,
                contentDescription = null
            )
        }, bottom = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = time
                )
                if (extraTime != null) {
                    Text(
                        text = "ET: $extraTime",
                        style = TextStyle(
                            fontSize = 11.sp
                        )
                    )
                }
            }
        })
}

@Composable
@Preview
private fun PastTimerCardPreview() {
    PastTimerCard(
        numbers = listOf(1, 2, 3),
        time = "30:00",
        onNumberBoxClick = {}
    )
}

@Composable
fun PastTimerCard(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    time: String,
    onNumberBoxClick: (Int) -> Unit
) {
    BasicTimerCard(modifier = modifier, numbers = numbers, onNumberBoxClick = onNumberBoxClick,
        top = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    modifier = Modifier.padding(top = 4.dp),
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = time,
                    style = TextStyle(fontSize = 14.sp)
                )
            }

        }, bottom = {
            // to keep the same numbersLayout placement
            Box(modifier = Modifier.size(68.dp))
        }
    )
}

@Composable
private fun BasicTimerCard(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    onNumberBoxClick: (Int) -> Unit,
    top: @Composable () -> Unit = {},
    bottom: @Composable () -> Unit = {},
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            top()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                NumbersGrid(
                    selectedNumbers = numbers,
                    onClick = onNumberBoxClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
            bottom()
        }
    }
}
