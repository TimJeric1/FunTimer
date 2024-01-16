package com.tjcoding.funtimer.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.tjcoding.funtimer.ui.theme.FunTimerTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumbersGrid(
    modifier: Modifier = Modifier,
    selectedNumbers: List<Int> = listOf(9, 10,15,23,47),
    maxItemsInEachRow: Int = 4,
    onClick: (Int) -> Unit = {},
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
) {
    FlowRow(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        maxItemsInEachRow = maxItemsInEachRow
    ) {
        for (number in selectedNumbers) {
            if (number < 10) {
                NumberBox(
                    modifier = Modifier
                        .padding(
                            vertical = 2.dp,
                            horizontal = 4.dp
                        )
                        .clickable {
                            onClick(number)
                        }
                        .height(22.dp)
                        .width(26.dp),
                    number = number,
                    textStyle = MaterialTheme.typography.labelSmall
                )
            } else {
                NumberBox(
                    modifier = Modifier
                        .padding(
                            vertical = 2.dp,
                            horizontal = 4.dp
                        )
                        .clickable {
                            onClick(number)
                        }
                        .height(22.dp)
                        .width(26.dp),
                    number = number,
                    textStyle = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BigNumbersGrid(
    modifier: Modifier = Modifier,
    selectedNumbers: List<Int> = listOf(9, 10),
    maxItemsInEachRow: Int = 4,
    onClick: (Int) -> Unit = {},
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center
) {
    FlowRow(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        maxItemsInEachRow = maxItemsInEachRow
    ) {
        for (number in selectedNumbers) {
            if (number < 10) {
                NumberBox(
                    modifier = Modifier
                        .padding(
                            vertical = 2.dp,
                            horizontal = 4.dp
                        )
                        .clickable {
                            onClick(number)
                        }
                        .height(56.dp)
                        .width(56.dp),
                    number = number,
                    textStyle = MaterialTheme.typography.displayMedium
                )
            } else {
                NumberBox(
                    modifier = Modifier
                        .padding(
                            vertical = 2.dp,
                            horizontal = 4.dp
                        )
                        .clickable {
                            onClick(number)
                        }
                        .height(56.dp)
                        .width(56.dp),
                    number = number,
                    textStyle = MaterialTheme.typography.displayMedium
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun NumbersGridPreview() {
    FunTimerTheme {
        NumbersGrid(
            modifier = Modifier,
            selectedNumbers = listOf(9, 10,15,23,47),
            maxItemsInEachRow = 4,
            onClick = {},
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
        )
    }

}

@Composable
@PreviewLightDark
private fun BigNumbersGridPreview() {
    FunTimerTheme {
        BigNumbersGrid(
            modifier = Modifier,
            selectedNumbers = listOf(9,10,15,23,47),
            maxItemsInEachRow = 4,
            onClick = {},
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
        )
    }

}
