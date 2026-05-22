package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.softShadow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme
import kotlin.math.roundToInt

@Composable
fun SSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
) {
    val haptic = LocalHapticFeedback.current
    var isDragging by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isInteracting = isDragging || isPressed

    val thumbSize by animateDpAsState(
        targetValue = if (isInteracting) 26.dp else 20.dp,
        label = "SliderThumbSize"
    )

    val range = valueRange.endInclusive - valueRange.start
    val currentStepIndex = if (range > 0f && steps > 0) {
        val stepSize = range / (steps + 1)
        ((value - valueRange.start) / stepSize).roundToInt()
    } else {
        0
    }

    LaunchedEffect(currentStepIndex) {
        if (steps > 0) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val rawFraction = if (range > 0f) (value - valueRange.start) / range else 0f

        val animatedFraction by animateFloatAsState(
            targetValue = rawFraction.coerceIn(0f, 1f),
            animationSpec = if (isDragging) spring(stiffness = 1500f) else spring(stiffness = 400f),
            label = "SliderFractionSmooth"
        )

        val finalFraction = if (isInteracting) rawFraction else animatedFraction

        fun updateValue(offsetX: Float) {
            val clampedX = offsetX.coerceIn(0f, widthPx)
            val newFraction = clampedX / widthPx
            val newValue = valueRange.start + (newFraction * range)

            val finalValue = if (steps > 0) {
                val stepSize = range / (steps + 1)
                val closestStep = ((newValue - valueRange.start) / stepSize).roundToInt()
                val stepValue = valueRange.start + closestStep * stepSize
                stepValue.coerceIn(valueRange.start, valueRange.endInclusive)
            } else {
                newValue
            }
            onValueChange(finalValue)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isDragging = false
                            tryAwaitRelease()
                        },
                        onTap = { offset ->
                            updateValue(offset.x)
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false },
                        onDrag = { change, _ ->
                            change.consume()
                            updateValue(change.position.x)
                        }
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Theme.colorScheme.seekbarTrack)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(finalFraction.coerceIn(0f, 1f))
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Theme.colorScheme.seekbarActiveTrack)
            )

            val thumbOffset = (finalFraction * (this@BoxWithConstraints.maxWidth.value - thumbSize.value)).dp
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset)
                    .size(thumbSize)
                    .softShadow(
                        borderRadius = thumbSize / 2, 
                        shadowRadius = if (isInteracting) 12.dp else 8.dp, 
                        offsetY = 1.dp
                    )
                    .clip(CircleShape)
                    .background(Theme.colorScheme.seekbarThumb)
            )
        }
    }
}

@Composable
@ComponentPreview
fun SSliderPreview() {
    var value by remember { mutableFloatStateOf(1.0f) }
    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp)
        ) {
            SHeadingMediumText("Seeker Value: ${String.format("%.2f", value)}")
            SSlider(
                value = value,
                onValueChange = { value = it },
                valueRange = 0.5f..2.0f,
                steps = 5
            )
        }
    }
}