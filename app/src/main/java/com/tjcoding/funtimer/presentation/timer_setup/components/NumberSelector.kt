package com.tjcoding.funtimer.presentation.timer_setup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.tjcoding.funtimer.presentation.components.NumberBox
import com.tjcoding.funtimer.ui.theme.FunTimerTheme

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
        LeftOutlinedArrow(
            modifier = Modifier
                .size(48.dp)
                .clickable {
                    onLeftFilledArrowClick()
                },
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        NumberBox(
            modifier = Modifier.height(55.dp).width(65.dp),
            textStyle = MaterialTheme.typography.displayMedium, number = displayedNumber)
        RightOutlinedArrow(
            modifier = Modifier
                .size(48.dp)
                .clickable {
                    onRightFilledArrowClick()
                },
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    }
}

@Composable
@PreviewLightDark
private fun NumberSelectorPreview() {
    FunTimerTheme {
        Surface {
            NumberSelector(
                displayedNumber = 1,
                onLeftFilledArrowClick = {},
                onRightFilledArrowClick = {},
            )
        }
    }
}
