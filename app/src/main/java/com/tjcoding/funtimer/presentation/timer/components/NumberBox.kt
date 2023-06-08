package com.tjcoding.funtimer.presentation.timer.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun NumberBox(modifier: Modifier = Modifier, number: Int = 1, fontSize: TextUnit = 14.sp) {

    Box(
        modifier = modifier
            .border(border = BorderStroke(2.dp, Color.White), shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = if (number <= 9) 8.dp else 6.dp),
            text = "#$number",
            textAlign = TextAlign.Center, style = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = if (number <= 9) fontSize else fontSize / 1.1,
            )
        )
    }
}
