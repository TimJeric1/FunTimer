package com.tjcoding.funtimer.service.alarm.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay
import kotlin.math.abs


enum class SwipeLocations {
    LEFT,
    MIDDLE,
    RIGHT
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun SwipeableButton(
    modifier: Modifier = Modifier,
    onLeftSwipeAction: () -> Unit = {},
    onRightSwipeAction: () -> Unit = {}
) {
    val density = LocalDensity.current
    val buttonSize = 64.dp
    val isDarkModeOn = isSystemInDarkTheme()

    val translation = remember {
        Animatable(0f)
    }
    translation.updateBounds(0f, with(density) { buttonSize.toPx() })


    val anchors = DraggableAnchors {
        SwipeLocations.RIGHT at with(density) { buttonSize.toPx() }
        SwipeLocations.MIDDLE at 0f
        SwipeLocations.LEFT at with(density) { -1 * buttonSize.toPx() }
    }

    val stateHorizontal = remember {
        AnchoredDraggableState(
            initialValue = SwipeLocations.MIDDLE,
            anchors = anchors,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            animationSpec = spring(),
            velocityThreshold = { with(density) { 80.dp.toPx() } }
        )
    }

    var areArrowsVisible by remember {
        mutableStateOf(false)
    }
    
    val arrowsAlpha by animateFloatAsState(
        targetValue =  if(areArrowsVisible) 100f else 0f ,
        animationSpec = spring(),
        label = "alpha"
    )


    LaunchedEffect(key1 = stateHorizontal.currentValue) {
        areArrowsVisible = stateHorizontal.currentValue == SwipeLocations.MIDDLE
        if(stateHorizontal.currentValue == SwipeLocations.MIDDLE) return@LaunchedEffect
        if(stateHorizontal.currentValue == SwipeLocations.LEFT) onLeftSwipeAction()
        if(stateHorizontal.currentValue == SwipeLocations.RIGHT) onRightSwipeAction()
        stateHorizontal.animateTo(targetValue = SwipeLocations.MIDDLE)
    }




    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
            LeftMovingArrow(
                modifier = Modifier
                    .width(48.dp)
                    .height(16.dp)
                    .alpha(arrowsAlpha)
            )
        Spacer(modifier = Modifier.padding(4.dp))
        Box {
            Canvas(modifier = Modifier.size(buttonSize)) {
                this.apply {
                    val radiusScale = lerp(
                        1f,
                        4f,
                        abs(stateHorizontal.requireOffset()) * 0.5f / buttonSize.toPx()
                    )
                    val width = lerp(
                        4f,
                        128f,
                        abs(stateHorizontal.requireOffset()) * 0.5f / buttonSize.toPx()
                    )
                    val alpha =
                        lerp(1f, 0.2f, abs(stateHorizontal.requireOffset()) / buttonSize.toPx())
                    drawCircle(
                        color = if (isDarkModeOn) Color.LightGray else Color.DarkGray,
                        radius = buttonSize.value * radiusScale,
                        style = Stroke(width = width),
                        alpha = if (alpha < 0f) 0f else alpha
                    )
                }
            }
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        areArrowsVisible = false
                        val newScale =
                            lerp(1f, 0.8f, abs(stateHorizontal.requireOffset()) / buttonSize.toPx())
                        val newAlpha =
                            lerp(1f, 0.6f, abs(stateHorizontal.requireOffset()) / buttonSize.toPx())
                        scaleX = newScale
                        scaleY = newScale
                        this.alpha = newAlpha
                    }
                    .background(
                        color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
                        shape = CircleShape
                    )
                    .size(buttonSize)
                    .anchoredDraggable(stateHorizontal, Orientation.Horizontal),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = if (isSystemInDarkTheme()) Color.DarkGray else Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.padding(4.dp))
            RightMovingArrow(
                modifier = Modifier
                    .width(48.dp)
                    .height(16.dp)
                    .alpha(arrowsAlpha)
            )

    }

}


@Composable
@Preview
fun RightMovingArrow(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 6f
) {

    val isDarkModeOn = isSystemInDarkTheme()

    val alpha = remember {
        Animatable(1f)
    }
    val translationX = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        while (true) {
            if (translationX.value == 0f && alpha.value == 1f) {
                translationX.animateTo(1f, animationSpec = tween(750))
                alpha.animateTo(0f, animationSpec = tween(200))
                delay(250)
            } else {
                translationX.snapTo(0f)
                alpha.animateTo(1f, animationSpec = tween(200))
            }
        }
    }

    Canvas(modifier = modifier) {
        val canvasWidth = this.size.width
        val canvasHeight = this.size.height

        val paint = Paint().apply {
            color = if (isDarkModeOn) Color.White else Color.DarkGray
            style = PaintingStyle.Stroke
            this.strokeWidth = strokeWidth
            strokeJoin = StrokeJoin.Round
            this.alpha = alpha.value
        }

        val point1 = Offset(0f, strokeWidth / 2)
        val point2 = Offset(0f, canvasHeight - strokeWidth / 2)
        val point3 = Offset((canvasWidth / 5), canvasHeight / 2)

        val path = Path()

        path.moveTo(lerp(point1.x, canvasWidth - point3.x, translationX.value), point1.y)
        path.lineTo(lerp(point3.x, canvasWidth - (strokeWidth / 2), translationX.value), point3.y)
        path.lineTo(lerp(point2.x, canvasWidth - point3.x, translationX.value), point2.y)

        drawContext.canvas.apply {
            drawPath(path, paint)
        }

    }
}

@Composable
@Preview
fun LeftMovingArrow(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 6f
) {

    val isDarkModeOn = isSystemInDarkTheme()

    val alpha = remember {
        Animatable(1f)
    }
    val translationX = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        while (true) {
            if (translationX.value == 0f && alpha.value == 1f) {
                translationX.animateTo(1f, animationSpec = tween(750))
                alpha.animateTo(0f, animationSpec = tween(200))
                delay(250)
            } else {
                translationX.snapTo(0f)
                alpha.animateTo(1f, animationSpec = tween(200))
            }
        }
    }

    Canvas(modifier = modifier) {
        val canvasWidth = this.size.width
        val canvasHeight = this.size.height

        val paint = Paint().apply {
            color = if (isDarkModeOn) Color.White else Color.DarkGray
            style = PaintingStyle.Stroke
            this.strokeWidth = strokeWidth
            strokeJoin = StrokeJoin.Round
            this.alpha = alpha.value
        }

        val point1 = Offset(canvasWidth, strokeWidth / 2)
        val point2 = Offset(canvasWidth, canvasHeight - strokeWidth / 2)
        val point3 = Offset(canvasWidth - (canvasWidth / 5), canvasHeight / 2)

        val path = Path()

        path.moveTo(lerp(point1.x, canvasWidth - point3.x, translationX.value), point1.y)
        path.lineTo(lerp(point3.x, strokeWidth / 2, translationX.value), point3.y)
        path.lineTo(lerp(point2.x, canvasWidth - point3.x, translationX.value), point2.y)

        drawContext.canvas.apply {
            drawPath(path, paint)
        }

    }
}


//@Composable
//@Preview
//fun RightFlashingArrow(
//    modifier: Modifier = Modifier
//        .height(48.dp)
//        .width(32.dp),
//    strokeWidth: Float = 12f
//) {
//
//
//    val infiniteTransition = rememberInfiniteTransition()
//    val xOffset by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(tween(1500, easing = EaseInOutQuart), repeatMode = RepeatMode.Restart), label = "shine"
//    )
//
//    val colorStops = arrayOf(
//        xOffset-0.1f to Color.DarkGray,
//        0.05f + xOffset to Color.White,
//        0.2f + xOffset to Color.DarkGray,
//    )
//
//    val isDarkModeOn = isSystemInDarkTheme()
//    Canvas(modifier = modifier) {
//        val canvasWidth = this.size.width
//        val canvasHeight = this.size.height
//
//        val brush = Brush.horizontalGradient(
//            colorStops = colorStops,
//            startX = 0f,
//            endX = canvasWidth,
//            tileMode = TileMode.Clamp
//        )
//
//        val point1 = Offset(2 * strokeWidth, strokeWidth / 2)
//        val point2 = Offset(2 * strokeWidth, canvasHeight - strokeWidth / 2)
//        val point3 = Offset((canvasWidth - canvasWidth / 5), canvasHeight / 2)
//
//        val path = Path()
//
//        path.moveTo(point1.x, point1.y)
//        path.lineTo(point3.x, point3.y)
//        path.lineTo(point2.x, point2.y)
//
//
//
//
//        drawContext.canvas.apply {
//            drawPath(path, brush, style = Stroke(width = strokeWidth, join = StrokeJoin.Round))
//        }
//
//    }
//}