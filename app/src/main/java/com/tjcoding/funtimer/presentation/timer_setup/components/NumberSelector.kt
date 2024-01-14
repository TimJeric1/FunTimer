package com.tjcoding.funtimer.presentation.timer_setup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tjcoding.funtimer.presentation.components.NumberBox

@Composable
fun NumberSelector(
    modifier: Modifier = Modifier,
    displayedNumber: Int,
    onLeftFilledArrowClick: () -> Unit,
    onRightFilledArrowClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LeftOutlinedArrow(modifier = Modifier
            .size(48.dp)
            .clickable {
                onLeftFilledArrowClick()
            })
        NumberBox(spanStyle = SpanStyle(fontSize = 48.sp), number = displayedNumber)
        RightOutlinedArrow(modifier = Modifier
            .size(48.dp)
            .clickable {
                onRightFilledArrowClick()
            })
    }
}
@Composable
@Preview
private fun NumberSelectorPreview() {
    NumberSelector(
        displayedNumber = 1,
        onLeftFilledArrowClick = {},
    onRightFilledArrowClick = {},
    )
}
