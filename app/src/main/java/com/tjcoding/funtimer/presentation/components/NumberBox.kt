package com.tjcoding.funtimer.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.tjcoding.funtimer.ui.theme.FunTimerTheme


@Composable
fun NumberBox(
    modifier: Modifier = Modifier,
    number: Int,
    textStyle: TextStyle,
    textColor : Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {

    Box(
        modifier = modifier
            .border(
                border = BorderStroke(
                    2.dp,
                    borderColor
                ), shape = RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = number.toString(),
            style = textStyle,
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}

@Composable
@PreviewLightDark
private fun NumberBoxPreview() {
    FunTimerTheme {
        NumberBox(
            modifier = Modifier
                .padding(
                    vertical = 2.dp,
                    horizontal = 4.dp
                )
                .height(22.dp)
                .width(26.dp),
            number = 21,
            textStyle = MaterialTheme.typography.labelSmall,
        )
    }
}
