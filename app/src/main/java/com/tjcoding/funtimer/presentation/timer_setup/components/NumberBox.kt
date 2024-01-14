package com.tjcoding.funtimer.presentation.timer_setup.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun NumberBox(
    modifier: Modifier = Modifier,
    number: Int,
    spanStyle: SpanStyle,
) {

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = spanStyle.fontSize, color = spanStyle.color)) {
            append("#")
        }
        withStyle(style = spanStyle) {
            append("$number")
        }
    }

    Box(
        modifier = modifier
            .border(
                border = BorderStroke(
                    2.dp,
                    if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                ), shape = RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = annotatedString,
            textAlign = TextAlign.Left,
        )
    }
}

@Composable
@Preview
private fun NumberBoxPreview() {
    NumberBox(
        number = 29,
        spanStyle = SpanStyle(
            fontSize = 14.sp,
        )
    )
}
