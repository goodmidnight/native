package io.goodmidnight.scanner.ui.feature.camera.crop.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import io.goodmidnight.scanner.designsystem.theme.Theme
import kotlin.math.sqrt

@Composable
fun CropOverlay(
    modifier: Modifier = Modifier,
    vertexPoints: List<Offset>,
    initialPoints: List<Offset>,
    imageWidth: Int,
    imageHeight: Int,
    onPinDrag: (Int, Offset) -> Unit,
) {
    val accentColor = Theme.colorScheme.accent
    val successColor = Theme.colorScheme.success
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    var isSnapped by remember { mutableStateOf(false) }
    var activePinIndex by remember { mutableStateOf<Int?>(null) }

    var localPoints by remember(vertexPoints) {
        mutableStateOf(vertexPoints)
    }

    // Use rememberUpdatedState to capture the latest coordinates and indices
    // without triggering pointerInput cancellation and restarts during drag gestures.
    val currentLocalPoints by rememberUpdatedState(localPoints)
    val currentActivePinIndex by rememberUpdatedState(activePinIndex)
    val currentInitialPoints by rememberUpdatedState(initialPoints)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(imageWidth, imageHeight) {
                detectDragGestures(
                    onDragStart = { startOffset ->
                        // Determine if startOffset is near any vertex point in screen coordinate space
                        val sizeWidth = size.width.toFloat()
                        val sizeHeight = size.height.toFloat()
                        val scale = minOf(sizeWidth / imageWidth, sizeHeight / imageHeight)
                        val offsetX = (sizeWidth - imageWidth * scale) / 2f
                        val offsetY = (sizeHeight - imageHeight * scale) / 2f

                        // Map points from preview coordinate space to screen space
                        val screenPts = currentLocalPoints.map { pt ->
                            Offset((pt.x * scale) + offsetX, (pt.y * scale) + offsetY)
                        }

                        var nearestIndex = -1
                        var minDist = 120f // ~45dp touch target radius in pixels
                        for (i in 0 until 4) {
                            val dist = distance(startOffset, screenPts[i])
                            if (dist < minDist) {
                                minDist = dist
                                nearestIndex = i
                            }
                        }

                        if (nearestIndex != -1) {
                            activePinIndex = nearestIndex
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        }
                    },
                    onDragEnd = {
                        isSnapped = false
                        activePinIndex = null
                    },
                    onDragCancel = {
                        isSnapped = false
                        activePinIndex = null
                    },
                    onDrag = { change, _ ->
                        val activeIndex = currentActivePinIndex
                        if (activeIndex != null) {
                            val sizeWidth = size.width.toFloat()
                            val sizeHeight = size.height.toFloat()
                            val scale = minOf(sizeWidth / imageWidth, sizeHeight / imageHeight)
                            val offsetX = (sizeWidth - imageWidth * scale) / 2f
                            val offsetY = (sizeHeight - imageHeight * scale) / 2f

                            // Convert screen coordinate back to preview coordinate space
                            val pos = change.position
                            val previewX = ((pos.x - offsetX) / scale).coerceIn(0f, imageWidth.toFloat())
                            val previewY = ((pos.y - offsetY) / scale).coerceIn(0f, imageHeight.toFloat())
                            val rawDragPos = Offset(previewX, previewY)

                            // Apply local snapping logic
                            val originalPos = currentInitialPoints.getOrNull(activeIndex)
                            var finalPos = rawDragPos
                            if (originalPos != null) {
                                val dist = distance(rawDragPos, originalPos)
                                if (dist < 24f) {
                                    finalPos = originalPos
                                    if (!isSnapped) {
                                        isSnapped = true
                                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                    }
                                } else if (dist < 45f && isSnapped) {
                                    finalPos = originalPos
                                } else {
                                    isSnapped = false
                                }
                            }

                            val updated = currentLocalPoints.toMutableList()
                            if (activeIndex in updated.indices) {
                                updated[activeIndex] = finalPos
                            }
                            localPoints = updated.toList()

                            onPinDrag(activeIndex, finalPos)
                            change.consume()
                        }
                    }
                )
            }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val scale = minOf(canvasWidth / imageWidth, canvasHeight / imageHeight)
        val offsetX = (canvasWidth - imageWidth * scale) / 2f
        val offsetY = (canvasHeight - imageHeight * scale) / 2f

        // Convert vertex coordinates to Compose Canvas space
        val transformedPts = localPoints.map { pt ->
            Offset((pt.x * scale) + offsetX, (pt.y * scale) + offsetY)
        }

        // Draw cropped polygon boundary path
        val cropPath = Path().apply {
            moveTo(transformedPts[0].x, transformedPts[0].y)
            lineTo(transformedPts[1].x, transformedPts[1].y)
            lineTo(transformedPts[2].x, transformedPts[2].y)
            lineTo(transformedPts[3].x, transformedPts[3].y)
            close()
        }

        // Draw semi-opaque dim backdrop outside document boundary
        // We can draw a full canvas backdrop, and then clip the inner document path out
        // Or simply draw the bounding stroke first

        drawPath(
            path = cropPath,
            color = accentColor.copy(alpha = 0.15f)
        )

        drawPath(
            path = cropPath,
            color = accentColor,
            style = Stroke(width = 6f)
        )

        // Draw active drag guide lines
        for (i in 0 until 4) {
            val isCurrentActive = activePinIndex == i
            val circleColor = if (isCurrentActive) successColor else accentColor
            val circleRadius = if (isCurrentActive) 28f else 18f

            drawCircle(
                color = circleColor.copy(alpha = 0.3f),
                radius = circleRadius + 10f,
                center = transformedPts[i]
            )

            drawCircle(
                color = circleColor,
                radius = circleRadius,
                center = transformedPts[i]
            )

            // Draw tiny inner white dot for premium touch handle style
            drawCircle(
                color = Color.White,
                radius = 6f,
                center = transformedPts[i]
            )
        }
    }
}

private fun distance(p1: Offset, p2: Offset): Float {
    val dx = p1.x - p2.x
    val dy = p1.y - p2.y
    return sqrt(dx * dx + dy * dy)
}
