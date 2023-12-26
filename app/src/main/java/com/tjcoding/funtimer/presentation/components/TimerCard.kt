package com.tjcoding.funtimer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.tjcoding.funtimer.presentation.timer_setup.components.NumbersLayout


@Composable
@Preview
fun TimerCard(
    modifier: Modifier = Modifier,
    numbers: List<Int> = listOf(1, 2, 3),
    time: String = "30:00",
    extraTime: String? = "2:00",
    onNumberBoxClick: (Int) -> Unit = { }
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                modifier = Modifier.padding(top = 4.dp),
                imageVector = Icons.Outlined.Timer,
                contentDescription = null
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                NumbersLayout(
                    selectedNumbers = numbers,
                    onClick = onNumberBoxClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = time
                )
                if(extraTime != null) {
                    Text(
                        text = "ET: $extraTime",
                        style = TextStyle(
                            fontSize = 11.sp
                        )
                    )
                }
            }


        }
    }
}




