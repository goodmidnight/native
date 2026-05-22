package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SBadge(
    modifier: Modifier = Modifier,
    count: Int? = null,
    visible: Boolean = true,
    color: Color = Theme.colorScheme.accent,
    textColor: Color = Theme.colorScheme.onAccent
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(initialScale = 0.6f, animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f)) + fadeIn(),
        exit = scaleOut(targetScale = 0.6f) + fadeOut(),
        modifier = modifier
    ) {
        if (count == null) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        } else {
            Box(
                modifier = Modifier
                    .widthIn(min = 18.dp)
                    .height(18.dp)
                    .clip(CircleShape)
                    .background(color)
                    .padding(horizontal = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                SLabelXSmallText(
                    text = if (count > 99) "99+" else count.toString(),
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun SBadgeBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier.padding(top = 4.dp, end = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }

        Box(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            badge()
        }
    }
}
