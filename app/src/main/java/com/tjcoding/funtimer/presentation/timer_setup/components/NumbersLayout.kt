package com.tjcoding.funtimer.presentation.timer_setup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
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
                    SpanStyle(
                        fontSize = 12.sp,
                        color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                    )
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
                    SpanStyle(
                        fontSize = 12.sp,
                        letterSpacing = (-2).sp,
                        color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
fun BigNumbersGrid(
    modifier: Modifier = Modifier,
    selectedNumbers: List<Int> = listOf(9, 10),
    onClick: (Int) -> Unit = {},
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center
) {
    FlowRow(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        maxItemsInEachRow = 4
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
                    SpanStyle(
                        fontSize = 32.sp,
                        color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                    )
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
                    SpanStyle(
                        fontSize = 32.sp,
                        letterSpacing = (-6).sp,
                        color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                    )
                )
            }
        }
    }
}
