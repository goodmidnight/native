package io.goodmidnight.scanner.designsystem.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Icons
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
) {
    val backgroundColor = when {
        !enabled -> Theme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        selected -> Theme.colorScheme.accent
        else -> Color.Transparent
    }

    val textColor = when {
        !enabled -> Theme.colorScheme.disabledText
        selected -> Theme.colorScheme.onAccent
        else -> Theme.colorScheme.secondaryText
    }

    val borderModifier = if (!selected && enabled) {
        Modifier.border(1.dp, Theme.colorScheme.outline, shape)
    } else if (!enabled) {
        Modifier.border(1.dp, Theme.colorScheme.outline.copy(alpha = 0.5f), shape)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .then(borderModifier)
            .bounceClick(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        SLabelMediumText(
            text = text,
            color = textColor
        )
    }
}

@Composable
fun SFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    val backgroundColor = when {
        !enabled -> Theme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        selected -> Theme.colorScheme.accent
        else -> Color.Transparent
    }

    val contentColor = when {
        !enabled -> Theme.colorScheme.disabledText
        selected -> Theme.colorScheme.onAccent
        else -> Theme.colorScheme.primaryText
    }

    val borderModifier = if (!selected && enabled) {
        Modifier.border(1.dp, Theme.colorScheme.outline, shape)
    } else if (!enabled) {
        Modifier.border(1.dp, Theme.colorScheme.outline.copy(alpha = 0.5f), shape)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .then(borderModifier)
            .bounceClick(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                SIcon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            
            SLabelMediumText(
                text = text,
                color = contentColor
            )

            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(6.dp))
                SIcon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@ComponentPreview
@Composable
fun SChipsPreview() {
    var selectedChip1 by remember { mutableStateOf(true) }
    var selectedChip2 by remember { mutableStateOf(false) }

    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SChip(
                    text = "Selected",
                    selected = selectedChip1,
                    onClick = { selectedChip1 = !selectedChip1 }
                )
                SChip(
                    text = "Unselected",
                    selected = selectedChip2,
                    onClick = { selectedChip2 = !selectedChip2 }
                )
                SChip(
                    text = "Disabled",
                    selected = false,
                    enabled = false,
                    onClick = {}
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SFilterChip(
                    text = "Latest",
                    trailingIcon = Icons.ArrowDropUp,
                    selected = selectedChip1,
                    onClick = { selectedChip1 = !selectedChip1 }
                )
                SFilterChip(
                    text = "Popular",
                    trailingIcon = Icons.ArrowDropDown,
                    selected = selectedChip2,
                    onClick = { selectedChip2 = !selectedChip2 }
                )
            }
        }
    }
}