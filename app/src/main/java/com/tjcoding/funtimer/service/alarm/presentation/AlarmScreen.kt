package com.tjcoding.funtimer.service.alarm.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.tjcoding.funtimer.presentation.components.BigNumbersGrid
import com.tjcoding.funtimer.service.alarm.presentation.components.SwipeableButton
import com.tjcoding.funtimer.ui.theme.FunTimerTheme

@Composable
@PreviewLightDark
private fun AlarmScreenPreview() {
    FunTimerTheme {
        Surface(
            tonalElevation = 5.dp
        ) {
            AlarmScreen(
                numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18),
                onDismiss = {},
                onMute = {}
            )
        }
    }
}

@Composable
fun AlarmScreen(
    numbers: List<Int>,
    onDismiss: () -> Unit,
    onMute: () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp

    FunTimerTheme {
        Surface(
            tonalElevation = 2.dp
        ) {

            Scaffold(
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pool,
                            contentDescription = "App icon",
                            modifier = Modifier.size(128.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Time's up for numbers",
                            style = MaterialTheme.typography.displayMedium,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.W400,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        BigNumbersGrid(
                            selectedNumbers = numbers,
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .heightIn(max = (screenHeight * 0.3).dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Center,
                            horizontalArrangement = Arrangement.Center
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            imageVector = Icons.AutoMirrored.Filled.VolumeOff,
                            contentDescription = "Volume Off",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant

                        )
                        SwipeableButton(onLeftSwipeAction = onMute, onRightSwipeAction = onDismiss)
                        Icon(
                            modifier = Modifier.size(64.dp),
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height((screenHeight * 0.15).dp))
                }
            }
        }
    }

}


