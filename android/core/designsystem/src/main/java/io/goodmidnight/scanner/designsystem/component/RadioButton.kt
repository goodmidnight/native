package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val dotScale by animateFloatAsState(
        targetValue = if (selected) 1.0f else 0.0f,
        label = "RadioDotScale"
    )

    val outlineColor by animateColorAsState(
        targetValue = when {
            !enabled -> Theme.colorScheme.disabledText.copy(alpha = 0.5f)
            selected -> Theme.colorScheme.accent
            else -> Theme.colorScheme.outline
        },
        label = "RadioOutlineColor"
    )

    val dotColor by animateColorAsState(
        targetValue = if (enabled) Theme.colorScheme.accent else Theme.colorScheme.disabledText,
        label = "RadioDotColor"
    )

    val bounceModifier = if (onClick != null && enabled) {
        Modifier.bounceClick(onClick = onClick)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(24.dp)
            .then(bounceModifier),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(2.dp, outlineColor, CircleShape)
        )

        Box(
            modifier = Modifier
                .scale(dotScale)
                .size(10.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
    }
}

@ComponentPreview
@Composable
fun SRadioButtonPreview() {
    var isSelected1 by remember { mutableStateOf(true) }
    var isSelected2 by remember { mutableStateOf(false) }
    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SRadioButton(
                selected = isSelected1,
                onClick = { isSelected1 = !isSelected1 },
            )
            SRadioButton(
                selected = isSelected2,
                onClick = { isSelected2 = !isSelected2 },
            )
            SRadioButton(
                selected = true,
                onClick = null,
                enabled = false
            )
        }
    }
}
