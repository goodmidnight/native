package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Alpha
import io.goodmidnight.scanner.designsystem.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
) {
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(value) {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    Slider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        steps = steps,
        thumb = {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        color = Theme.colorScheme.seekbarThumb,
                        shape = CircleShape
                    )
            )
        },
        track = { sliderState ->
            val range = valueRange.endInclusive - valueRange.start
            val activeFraction =
                if (range > 0) (sliderState.value - valueRange.start) / range else 0f


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Theme.colorScheme.seekbarTrack,
                            shape = CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(activeFraction)
                        .fillMaxHeight()
                        .background(
                            color = Theme.colorScheme.seekbarActiveTrack,
                            shape = CircleShape
                        )
                )
            }
        }
    )
}

@Composable
@ComponentPreview
fun SSliderPreview() {
    Theme {
        Surface(color = Theme.colorScheme.background) {
            SSlider(
                value = 1.5f,
                onValueChange = {},
                valueRange = 0.5f..2f,
                steps = 5,
            )
        }
    }
}