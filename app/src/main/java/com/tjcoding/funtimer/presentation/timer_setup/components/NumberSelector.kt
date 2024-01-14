package com.tjcoding.funtimer.presentation.timer_setup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tjcoding.funtimer.presentation.components.NumberBox

@Composable
fun NumberSelector(
    displayedNumber: Int,
    onLeftFilledArrowClick: () -> Unit,
    onRightFilledArrowClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LeftOutlinedArrow(modifier = Modifier
            .size(48.dp)
            .clickable {
                onLeftFilledArrowClick()
            })
        Spacer(modifier = Modifier.size(height = 0.dp, width = 48.dp))
        NumberBox(spanStyle = SpanStyle(fontSize = 48.sp), number = displayedNumber)
        Spacer(modifier = Modifier.size(height = 0.dp, width = 48.dp))
        RightOutlinedArrow(modifier = Modifier
            .size(48.dp)
            .clickable {
                onRightFilledArrowClick()
            })
    }
}
