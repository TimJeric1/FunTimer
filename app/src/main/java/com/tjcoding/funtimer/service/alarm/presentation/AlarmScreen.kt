package com.tjcoding.funtimer.service.alarm.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tjcoding.funtimer.presentation.timer_setup.components.BigNumbersGrid
import com.tjcoding.funtimer.service.alarm.presentation.components.SwipeableButton
import com.tjcoding.funtimer.ui.theme.Typography



@Composable
@Preview
fun AlarmScreen(
    numbers: List<Int> = listOf(1, 2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18),
    onDismiss: () -> Unit = {}
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Scaffold(Modifier.background(color = if(isSystemInDarkTheme()) Color.LightGray else Color.White)) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = if (isSystemInDarkTheme()) Color.DarkGray else Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Default.Pool,
                    contentDescription = "App icon",
                    tint = if(isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
                    modifier = Modifier.size(128.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Time's up for numbers",
                    style = Typography.displayMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W400,
                    color = if(isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
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
            SwipeableButton(onSwipeAction = onDismiss)
            Spacer(modifier = Modifier.height((screenHeight*0.15).dp))

        }
    }
}


