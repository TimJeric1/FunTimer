package com.tjcoding.funtimer.presentation.timer_setup.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview


        @Composable
        @Preview
        fun LeftOutlinedArrow(modifier: Modifier = Modifier) {
            val isDarkModeOn = isSystemInDarkTheme()
            Canvas(modifier = modifier) {
                val canvasWidth = this.size.width
                val canvasHeight = this.size.height
                val strokeWidth = 4f

                val paint = Paint()
                paint.strokeWidth = strokeWidth
                paint.color = if(isDarkModeOn) Color.LightGray else Color.DarkGray
                paint.style = PaintingStyle.Stroke
                paint.isAntiAlias = true

                val point1 = Offset(canvasWidth-strokeWidth, 0f)
                val point2 = Offset(canvasWidth-strokeWidth, canvasHeight)
                val point3 = Offset(0f, canvasHeight / 2)

                val path = Path()

                path.moveTo(point1.x, point1.y)
                path.lineTo(point2.x, point2.y)
                path.lineTo(point3.x, point3.y)
                path.close()


                drawContext.canvas.apply {
                    drawPath(path,paint)
                }

            }
        }

        @Composable
        @Preview
        fun RightOutlinedArrow(modifier: Modifier = Modifier) {
            val isDarkModeOn = isSystemInDarkTheme()
            Canvas(modifier = modifier) {
                val canvasWidth = this.size.width
                val canvasHeight = this.size.height
                val strokeWidth = 4f

                val paint = Paint()
                paint.strokeWidth = strokeWidth
                paint.color = if(isDarkModeOn) Color.LightGray else Color.DarkGray
                paint.style = PaintingStyle.Stroke
                paint.isAntiAlias = true

                val point1 = Offset(0f+strokeWidth, 0f)
                val point2 = Offset(0f+strokeWidth, canvasHeight)
                val point3 = Offset(canvasWidth, canvasHeight / 2)

                val path = Path()

                path.moveTo(point1.x, point1.y)
                path.lineTo(point2.x, point2.y)
                path.lineTo(point3.x, point3.y)
                path.close()

                drawContext.canvas.apply {
                    drawPath(path,paint)
                }
            }
        }


