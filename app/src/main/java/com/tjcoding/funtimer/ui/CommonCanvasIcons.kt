package com.tjcoding.funtimer.ui

import android.graphics.Paint
import android.graphics.Path
import androidx.annotation.ColorInt
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class CommonCanvasIcons {

    companion object {
        @Composable
        fun left_filled_arrow(modifier: Modifier = Modifier.size(32.dp)) {
            Canvas(modifier = modifier) {
                val canvasWidth = this.size.width
                val canvasHeight = this.size.height

                val paint = Paint()
                paint.strokeWidth = 2f
                paint.color = android.graphics.Color.BLACK
                paint.style = Paint.Style.FILL_AND_STROKE
                paint.isAntiAlias = true

                val point1 = Offset(canvasWidth, 0f)
                val point2 = Offset(canvasWidth, canvasHeight)
                val point3 = Offset(canvasWidth / 5, canvasHeight / 2)

                val path = Path()

                path.moveTo(point1.x, point1.y)
                path.lineTo(point2.x, point2.y)
                path.lineTo(point3.x, point3.y)
                path.close()


                drawContext.canvas.nativeCanvas.apply {
                    drawPath(path, paint)
                }

            }
        }

        @Composable
        fun right_filled_arrow(modifier: Modifier = Modifier.size(32.dp), @ColorInt color : Int = android.graphics.Color.BLACK) {
            Canvas(modifier = modifier) {
                val canvasWidth = this.size.width
                val canvasHeight = this.size.height

                val paint = Paint()
                paint.strokeWidth = 2f
                paint.color = android.graphics.Color.BLACK
                paint.style = Paint.Style.FILL_AND_STROKE
                paint.isAntiAlias = true

                val point1 = Offset(0f, 0f)
                val point2 = Offset(0f, canvasHeight)
                val point3 = Offset((canvasWidth - canvasWidth / 5), canvasHeight / 2)

                val path = Path()

                path.moveTo(point1.x, point1.y)
                path.lineTo(point2.x, point2.y)
                path.lineTo(point3.x, point3.y)
                path.close()


                drawContext.canvas.nativeCanvas.apply {
                    drawPath(path, paint)
                }

            }
        }
    }
}

