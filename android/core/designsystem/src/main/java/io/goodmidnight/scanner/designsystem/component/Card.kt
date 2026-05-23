package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.softShadow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

enum class CardStyle {
    BASIC,
    PRIMARY,
    SECONDARY,
    PRIMARY_OUTLINE,
    SECONDARY_OUTLINE;
}

@Composable
fun SCard(
    modifier: Modifier = Modifier,
    style: CardStyle = CardStyle.PRIMARY,
    shape: Shape = RoundedCornerShape(24.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val containerColor = when (style) {
        CardStyle.BASIC -> Theme.colorScheme.background
        CardStyle.PRIMARY -> Theme.colorScheme.surface
        CardStyle.PRIMARY_OUTLINE -> Theme.colorScheme.surface
        else -> Theme.colorScheme.surfaceVariant
    }

    val isOutline = style == CardStyle.PRIMARY_OUTLINE || style == CardStyle.SECONDARY_OUTLINE

    val borderModifier = if (isOutline) {
        Modifier.border(1.dp, Theme.colorScheme.outline, shape)
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .softShadow(borderRadius = 24.dp, shadowRadius = 25.dp, offsetY = 4.dp)
            .clip(shape)
            .background(containerColor)
            .then(borderModifier),
        content = content
    )
}

@Composable
@ComponentPreview
fun SCardPreview() {
    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SCard(
                style = CardStyle.PRIMARY,
                content = {
                    Column(modifier = Modifier.padding(20.dp)) {
                        SHeadingMediumText("Primary Card")
                        SParagraphMediumText("Provides a soft white surface that is easy on the eyes with subtle, transparent shadows.")
                    }
                }
            )

            SCard(
                style = CardStyle.PRIMARY_OUTLINE,
                content = {
                    Column(modifier = Modifier.padding(20.dp)) {
                        SHeadingMediumText("Outline Card")
                        SParagraphMediumText("Renders thin, subtle dividers for areas that require clear visual separation.")
                    }
                }
            )
        }
    }
}