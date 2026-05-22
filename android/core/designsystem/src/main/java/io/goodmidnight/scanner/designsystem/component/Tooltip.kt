package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.softShadow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

enum class TooltipDirection {
    Top,
    Bottom
}

@Composable
fun STooltip(
    text: String,
    modifier: Modifier = Modifier,
    direction: TooltipDirection = TooltipDirection.Top,
    visible: Boolean = true
) {
    val tooltipBgColor = Theme.colorScheme.primaryText
    val tooltipTextColor = Theme.colorScheme.background

    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(initialScale = 0.5f, animationSpec = spring(dampingRatio = 0.62f, stiffness = 350f)) + fadeIn(),
        exit = scaleOut(targetScale = 0.5f) + fadeOut(),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (direction == TooltipDirection.Top) {
                TooltipArrow(color = tooltipBgColor, isUpward = true)
            }

            Box(
                modifier = Modifier
                    .softShadow(borderRadius = 10.dp, shadowRadius = 10.dp, offsetY = 2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(tooltipBgColor)
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                SLabelSmallText(
                    text = text,
                    color = tooltipTextColor
                )
            }

            if (direction == TooltipDirection.Bottom) {
                TooltipArrow(color = tooltipBgColor, isUpward = false)
            }
        }
    }
}

@Composable
private fun TooltipArrow(
    color: Color,
    isUpward: Boolean
) {
    Canvas(
        modifier = Modifier.size(width = 14.dp, height = 7.dp)
    ) {
        val path = Path().apply {
            if (isUpward) {
                moveTo(size.width / 2f, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
            } else {
                moveTo(size.width / 2f, size.height)
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
            }
            close()
        }
        drawPath(path = path, color = color)
    }
}

@Composable
@ComponentPreview
fun STooltipPreview() {
    Theme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            STooltip(
                text = "여기를 길게 누르면 수동 촬영 모드로 변경됩니다",
                direction = TooltipDirection.Top,
                visible = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            STooltip(
                text = "스캔 경계 정밀 보정 팁",
                direction = TooltipDirection.Bottom,
                visible = true
            )
        }
    }
}
