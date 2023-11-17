package com.tjcoding.funtimer.service.alarm

import android.provider.MediaStore.Images
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tjcoding.funtimer.R
import com.tjcoding.funtimer.ui.theme.Typography


@Composable
@Preview
fun AlarmScreen(
    numbers: List<Int> = listOf(1, 2, 3)
) {
    Scaffold { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.Pool, contentDescription = "App icon")
            Text(
                text = "Time's up for numbers\n" +
                        numbers.joinToString(separator = " ") { it.toString() },
                style = Typography.titleLarge,
                textAlign = TextAlign.Center
            )

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SwipeableButton(
    onClickAction: () -> Unit
) {
    var swipeProgress = remember { mutableFloatStateOf(0f) }


    Box(
        modifier = Modifier
            .size(48.dp)
            .padding(8.dp)
            .graphicsLayer(
                scaleX = 1 + (swipeProgress.value / 1000),
                scaleY = 1 + (swipeProgress.value / 1000),
                alpha = 1 - (swipeProgress.value / 1000)
            )
            .clip(CircleShape)
            .background(Color.Blue)
            .clickable { } // Prevent accidental clicks while swiping
    ) {
        // Your icon content here
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Swipeable Icon",
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}