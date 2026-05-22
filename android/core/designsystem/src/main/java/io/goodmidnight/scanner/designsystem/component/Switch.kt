package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val thumbWidth by animateDpAsState(
        targetValue = if (isPressed) 30.dp else 24.dp,
        label = "SwitchThumbWidth"
    )

    val targetOffset = when {
        checked && isPressed -> 17.dp
        checked -> 23.dp
        else -> 3.dp
    }

    val thumbOffset by animateDpAsState(
        targetValue = targetOffset,
        label = "SwitchThumbOffset"
    )

    val trackColor by animateColorAsState(
        targetValue = if (checked) Theme.colorScheme.accent else Theme.colorScheme.outline,
        label = "SwitchTrackColor"
    )

    Box(
        modifier = modifier
            .bounceClick { onCheckedChange(!checked) }
            .size(width = 50.dp, height = 30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(trackColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(width = thumbWidth, height = 24.dp)
                .clip(CircleShape)
                .background(Theme.colorScheme.surface)
        )
    }
}

@Composable
@ComponentPreview
fun SSwitchPreview() {
    var check1 by remember { mutableStateOf(true) }
    var check2 by remember { mutableStateOf(false) }

    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SToggleSwitch(
                checked = check1,
                onCheckedChange = { check1 = it }
            )
            SToggleSwitch(
                checked = check2,
                onCheckedChange = { check2 = it }
            )
        }
    }
}