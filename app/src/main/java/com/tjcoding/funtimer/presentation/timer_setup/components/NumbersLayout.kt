package com.tjcoding.funtimer.presentation.timer_setup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NumbersLayout(selectedNumbers: List<Int>, onClick: ((Int) -> Unit)?) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
                                if (onClick != null) {
                                    onClick(number)
                                }
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
                                if (onClick != null) {
                                    onClick(number)
                                }
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
                                if (onClick != null) {
                                    onClick(number)
                                }
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
                                if (onClick != null) {
                                    onClick(number)
                                }
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
                                if (onClick != null) {
                                    onClick(number)
                                }
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
                                if (onClick != null) {
                                    onClick(number)
                                }
                            }, number = number
                    )
                }
            }
        }
    }
}


