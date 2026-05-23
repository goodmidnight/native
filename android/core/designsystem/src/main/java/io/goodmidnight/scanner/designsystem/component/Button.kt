package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.modifier.softShadow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

enum class SButtonType {
    PRIMARY,
    SECONDARY
}

@Composable
fun SButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: SButtonType = SButtonType.PRIMARY,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val buttonShape = RoundedCornerShape(24.dp)

    val backgroundColor = when (type) {
        SButtonType.PRIMARY -> if (enabled) Theme.colorScheme.accent else Theme.colorScheme.outlineVariant
        SButtonType.SECONDARY -> if (enabled) Theme.colorScheme.surface else Theme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    }

    val textColor = when (type) {
        SButtonType.PRIMARY -> if (enabled) Theme.colorScheme.onAccent else Theme.colorScheme.disabledText
        SButtonType.SECONDARY -> if (enabled) Theme.colorScheme.primaryText else Theme.colorScheme.disabledText
    }

    val borderModifier = if (type == SButtonType.SECONDARY && enabled) {
        Modifier.border(1.dp, Theme.colorScheme.outline, buttonShape)
    } else {
        Modifier
    }

    val shadowModifier = if (enabled) {
        Modifier.softShadow(borderRadius = 24.dp, shadowRadius = 15.dp, offsetY = 3.dp)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(shadowModifier)
            .clip(buttonShape)
            .background(backgroundColor)
            .then(borderModifier)
            .bounceClick(enabled = enabled, onClick = onClick)
            .padding(horizontal = 28.dp, vertical = 14.dp), // Spacing: ensure sufficient padding
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            STitleSmallText(
                text = text,
                color = textColor,
            )
        }
    }
}

@Composable
@ComponentPreview
fun SButtonPreview() {
    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SButton(
                onClick = {},
                text = "Primary Button"
            )
            SButton(
                onClick = {},
                enabled = false,
                text = "Disabled Primary Button"
            )
            SButton(
                onClick = {},
                type = SButtonType.SECONDARY,
                text = "Secondary Button"
            )
            SButton(
                onClick = {},
                enabled = false,
                type = SButtonType.SECONDARY,
                text = "Disabled Secondary"
            )
        }
    }
}