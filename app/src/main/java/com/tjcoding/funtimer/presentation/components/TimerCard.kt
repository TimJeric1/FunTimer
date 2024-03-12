package com.tjcoding.funtimer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.tjcoding.funtimer.ui.theme.FunTimerTheme

@Composable
@PreviewLightDark
private fun TimerCardPreview() {
    FunTimerTheme {
        TimerCard(
            numbers = listOf(1, 2, 3, 11, 12, 13),
            time = "30:00",
            extraTime = "2:00",
            onNumberBoxClick = {}
        )
    }

}

@Composable
fun TimerCard(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    time: String,
    extraTime: String?,
    onNumberBoxClick: (Int) -> Unit
) {
    BasicTimerCard(modifier = modifier, numbers = numbers,
        onNumberBoxClick = onNumberBoxClick,
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        boxBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
        boxTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
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
private fun PastTimerCardPreview() {
    FunTimerTheme {
        PastTimerCard(
            numbers = listOf(1, 2, 3, 11, 12, 13),
            time = "30:00",
            onNumberBoxClick = {}
        )
    }

}

@Composable
fun PastTimerCard(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    time: String,
    onNumberBoxClick: (Int) -> Unit
) {
    BasicTimerCard(modifier = modifier,
        numbers = numbers,
        onNumberBoxClick = onNumberBoxClick,
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        boxTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
        boxBorderColor = MaterialTheme.colorScheme.onTertiaryContainer,
        top = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    modifier = Modifier.padding(top = 4.dp),
                    imageVector = Icons.Outlined.Timer,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = time,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

        }, bottom = {
            // to keep the same numbersLayout placement
            Box(modifier = Modifier.size(68.dp))
        }
    )
}

@Composable
fun BasicTimerCard(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    onNumberBoxClick: (Int) -> Unit,
    top: @Composable () -> Unit = {},
    bottom: @Composable () -> Unit = {},
    cardColors: CardColors = CardDefaults.cardColors(),
    boxTextColor : Color = MaterialTheme.colorScheme.onSurface,
    boxBorderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Card(
        modifier = modifier,
        colors = cardColors
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            top()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
                contentAlignment = Alignment.Center,
            ) {
                NumbersGrid(
                    selectedNumbers = numbers,
                    onClick = onNumberBoxClick,
                    modifier = Modifier.fillMaxSize(),
                    boxBorderColor = boxBorderColor,
                    boxTextColor = boxTextColor
                )
            }
            bottom()
        }
    }
}
