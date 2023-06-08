package com.tjcoding.funtimer.presentation.timer.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tjcoding.funtimer.ui.CommonCanvasIcons

@Composable
fun NumberSelector(
    displayedNumber: Int,
    onLeftFilledArrowClick: () -> Unit,
    onRightFilledArrowClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CommonCanvasIcons.left_filled_arrow(modifier = Modifier
            .size(48.dp)
            .clickable {
                onLeftFilledArrowClick()
            })
        Spacer(modifier = Modifier.size(height = 0.dp, width = 48.dp))
        NumberBox(fontSize = 48.sp, number = displayedNumber)
        Spacer(modifier = Modifier.size(height = 0.dp, width = 48.dp))
        CommonCanvasIcons.right_filled_arrow(modifier = Modifier
            .size(48.dp)
            .clickable {
                onRightFilledArrowClick()
            })
    }
}
