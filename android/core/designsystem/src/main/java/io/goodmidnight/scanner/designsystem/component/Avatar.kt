package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

enum class AvatarSize(val dp: Dp) {
    Small(32.dp),
    Medium(48.dp),
    Large(64.dp)
}

enum class AvatarShape {
    Circle,
    Squircle
}

@Composable
fun SAvatar(
    modifier: Modifier = Modifier,
    initials: String? = null,
    painter: Painter? = null,
    size: AvatarSize = AvatarSize.Medium,
    shape: AvatarShape = AvatarShape.Circle,
    isActive: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val avatarShape: Shape = when (shape) {
        AvatarShape.Circle -> CircleShape
        AvatarShape.Squircle -> RoundedCornerShape(percent = 32)
    }

    val clickModifier = if (onClick != null) {
        Modifier.bounceClick(onClick = onClick)
    } else {
        Modifier
    }

    val activeBorderAlpha by if (isActive) {
        val infiniteTransition = rememberInfiniteTransition(label = "AvatarPulseTransition")
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "AvatarPulseAlpha"
        )
    } else {
        androidx.compose.runtime.rememberUpdatedState(0.0f)
    }

    Box(
        modifier = modifier
            .then(clickModifier)
            .size(size.dp + 8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isActive) {
            Box(
                modifier = Modifier
                    .size(size.dp + 6.dp)
                    .border(
                        width = 2.dp,
                        color = Theme.colorScheme.accent.copy(alpha = activeBorderAlpha),
                        shape = avatarShape
                    )
            )
        }

        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(avatarShape)
                .background(Theme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            if (painter != null) {
                Image(
                    painter = painter,
                    contentDescription = "Avatar Image",
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (!initials.isNullOrEmpty()) {
                val textStyle = when (size) {
                    AvatarSize.Small -> Theme.type.labelSmall
                    AvatarSize.Medium -> Theme.type.labelLarge
                    AvatarSize.Large -> Theme.type.headingMedium
                }
                SLabelMediumText(
                    text = initials.take(2).uppercase(),
                    color = Theme.colorScheme.primaryText,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(size.dp / 2)
                        .clip(CircleShape)
                        .background(Theme.colorScheme.secondaryText.copy(alpha = 0.3f))
                )
            }
        }
    }
}

@Composable
@ComponentPreview
fun SAvatarPreview() {
    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SAvatar(initials = "JD", size = AvatarSize.Small)
                SAvatar(initials = "JD", size = AvatarSize.Medium)
                SAvatar(initials = "JD", size = AvatarSize.Large)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SAvatar(
                    initials = "SM",
                    size = AvatarSize.Medium,
                    shape = AvatarShape.Squircle,
                    isActive = false,
                    onClick = {}
                )
                SAvatar(
                    initials = "AC",
                    size = AvatarSize.Medium,
                    shape = AvatarShape.Circle,
                    isActive = true,
                    onClick = {}
                )
                SAvatar(
                    initials = "SQ",
                    size = AvatarSize.Large,
                    shape = AvatarShape.Squircle,
                    isActive = true,
                    onClick = {}
                )
            }
        }
    }
}
