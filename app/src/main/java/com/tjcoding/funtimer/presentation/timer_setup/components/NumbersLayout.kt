package com.tjcoding.funtimer.presentation.timer_setup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun NumbersLayout(
    modifier: Modifier = Modifier,
    selectedNumbers: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
    onClick: (Int) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        if (selectedNumbers.size <= 4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                selectedNumbers.forEachIndexed() { index, number ->
                    NumberBox(
                        modifier = Modifier
                            .padding(
                                vertical = 2.dp,
                                horizontal = 4.dp
                            )
                            .clickable {
                                onClick(number)
                            }, number = number
                    )
                }
            }
        }
        if (selectedNumbers.size in 5..8) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                selectedNumbers.forEachIndexed() { index, number ->
                    if (index > 3) return@forEachIndexed
                    NumberBox(
                        modifier = Modifier
                            .padding(
                                vertical = 2.dp,
                                horizontal = 4.dp
                            )
                            .clickable {
                                onClick(number)
                            }, number = number
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                selectedNumbers.forEachIndexed() { index, number ->
                    if (index <= 3) return@forEachIndexed
                    NumberBox(
                        modifier = Modifier
                            .padding(
                                vertical = 2.dp,
                                horizontal = 4.dp
                            )
                            .clickable {
                                onClick(number)
                            }, number = number
                    )
                }
            }
        }
        if (selectedNumbers.size in 9..11) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                selectedNumbers.forEachIndexed() { index, number ->
                    if (index > 3) return@forEachIndexed
                    NumberBox(
                        modifier = Modifier
                            .padding(
                                vertical = 2.dp,
                                horizontal = 4.dp
                            )
                            .clickable {
                                onClick(number)
                            }, number = number
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                selectedNumbers.forEachIndexed() { index, number ->
                    if (index <= 3 || index > 7) return@forEachIndexed
                    NumberBox(
                        modifier = Modifier
                            .padding(
                                vertical = 2.dp,
                                horizontal = 4.dp
                            )
                            .clickable {
                                onClick(number)
                            }, number = number
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                selectedNumbers.forEachIndexed() { index, number ->
                    if (index <= 7) return@forEachIndexed
                    NumberBox(
                        modifier = Modifier
                            .padding(
                                vertical = 2.dp,
                                horizontal = 4.dp
                            )
                            .clickable {
                                onClick(number)
                            }, number = number
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
fun NumbersGrid(
    modifier: Modifier = Modifier,
    selectedNumbers: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
    onClick: (Int) -> Unit = {},
) {
    FlowRow(modifier = modifier) {
        for (number in selectedNumbers) {
            NumberBox(
                modifier = Modifier
                    .padding(
                        vertical = 2.dp,
                        horizontal = 4.dp
                    )
                    .clickable {
                        onClick(number)
                    },
                number = number,
            )
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
