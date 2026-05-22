package io.goodmidnight.scanner.ui.feature.camera.camera.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import io.goodmidnight.scanner.designsystem.theme.Theme
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedState

@Composable
fun DocumentOverlay(
    frame: SharedState.DocumentFrameState,
    modifier: Modifier = Modifier
) {
    val strokeColor = if (frame.isStable) Theme.colorScheme.success else Color(0x80FFFFFF)

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val imageWidth = frame.imageWidth.toFloat()
        val imageHeight = frame.imageHeight.toFloat()

        if (imageWidth == 0f || imageHeight == 0f) return@Canvas

        val scale = maxOf(canvasWidth / imageWidth, canvasHeight / imageHeight)
        val offsetX = (canvasWidth - imageWidth * scale) / 2f
        val offsetY = (canvasHeight - imageHeight * scale) / 2f

        val rawPts = frame.points
        val transformedPts = FloatArray(8) { index ->
            if (index % 2 == 0) {
                (rawPts[index] * scale) + offsetX
            } else {
                (rawPts[index] * scale) + offsetY
            }
        }

        val path = Path().apply {
            moveTo(transformedPts[0], transformedPts[1]) // Top-Left
            lineTo(transformedPts[2], transformedPts[3]) // Top-Right
            lineTo(transformedPts[4], transformedPts[5]) // Bottom-Right
            lineTo(transformedPts[6], transformedPts[7]) // Bottom-Left
            close()
        }

        drawPath(
            path = path,
            color = strokeColor,
            style = Stroke(width = 8f)
        )

        for (i in 0 until 4) {
            drawCircle(
                color = strokeColor,
                radius = 12f,
                center = Offset(transformedPts[i * 2], transformedPts[i * 2 + 1])
            )
        }
    }
}