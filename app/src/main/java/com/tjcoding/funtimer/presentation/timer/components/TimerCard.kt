package com.tjcoding.funtimer.presentation.timer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp


@Composable
fun TimerCard(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    time: Int,
    onNumberBoxClick: ((Int) -> Unit)? = null
) {
    Card(
        modifier = modifier
            .size(width = 180.dp, height = 180.dp),
    ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Icon(modifier = Modifier.padding(top = 4.dp), imageVector = Icons.Outlined.Timer, contentDescription = null)
                Box(
                    modifier = Modifier

                        .fillMaxWidth()
                        .weight(8f),
                    contentAlignment = Alignment.Center,
                ) {
                    NumbersLayout(selectedNumbers = numbers, onClick = onNumberBoxClick)
                }
                Text(
                    modifier = Modifier.weight(2f),
                    text = "${time}:00"
                )
            }
        }
    }




