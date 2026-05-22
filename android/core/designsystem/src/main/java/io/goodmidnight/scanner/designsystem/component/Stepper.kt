package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import io.goodmidnight.scanner.designsystem.theme.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.modifier.softShadow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: IntRange = 0..99,
    enabled: Boolean = true
) {
    val canDecrement = enabled && value > valueRange.first
    val canIncrement = enabled && value < valueRange.last

    val scaleAnim = remember { Animatable(1.0f) }

    LaunchedEffect(value) {
        scaleAnim.snapTo(1.25f)
        scaleAnim.animateTo(
            targetValue = 1.0f,
            animationSpec = spring(
                dampingRatio = 0.55f,
                stiffness = 300f
            )
        )
    }

    Row(
        modifier = modifier
            .softShadow(borderRadius = 24.dp, shadowRadius = 10.dp, offsetY = 1.dp)
            .border(1.dp, Theme.colorScheme.outline, CircleShape)
            .background(Theme.colorScheme.surface, CircleShape)
            .padding(horizontal = 6.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    if (canDecrement) Theme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    else Theme.colorScheme.outlineVariant.copy(alpha = 0.15f)
                )
                .bounceClick(enabled = canDecrement) {
                    onValueChange(value - 1)
                },
            contentAlignment = Alignment.Center
        ) {
            SIcon(
                imageVector = Icons.Remove,
                contentDescription = "감소",
                tint = if (canDecrement) Theme.colorScheme.primaryText else Theme.colorScheme.disabledText,
                modifier = Modifier.size(18.dp)
            )
        }

        Box(
            modifier = Modifier
                .width(48.dp)
                .scale(scaleAnim.value),
            contentAlignment = Alignment.Center
        ) {
            SLabelLargeText(
                text = value.toString(),
                color = if (enabled) Theme.colorScheme.primaryText else Theme.colorScheme.disabledText
            )
        }

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    if (canIncrement) Theme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    else Theme.colorScheme.outlineVariant.copy(alpha = 0.15f)
                )
                .bounceClick(enabled = canIncrement) {
                    onValueChange(value + 1)
                },
            contentAlignment = Alignment.Center
        ) {
            SIcon(
                imageVector = Icons.Add,
                contentDescription = "증가",
                tint = if (canIncrement) Theme.colorScheme.primaryText else Theme.colorScheme.disabledText,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
@ComponentPreview
fun SStepperPreview() {
    var count1 by remember { mutableStateOf(1) }
    var count2 by remember { mutableStateOf(10) }

    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SStepper(
                value = count1,
                onValueChange = { count1 = it },
                valueRange = 1..5
            )

            SStepper(
                value = count2,
                onValueChange = { count2 = it },
                valueRange = 0..100,
                enabled = false
            )
        }
    }
}
