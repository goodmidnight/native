package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Alpha
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
    shape: Shape = RoundedCornerShape(12.dp),
    content: @Composable ColumnScope.() -> Unit,
) {

    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = when (style) {
                CardStyle.BASIC -> Theme.colorScheme.background
                CardStyle.PRIMARY -> Theme.colorScheme.surface
                CardStyle.PRIMARY_OUTLINE -> Theme.colorScheme.surface
                else -> Theme.colorScheme.surfaceVariant
            },
            contentColor = when (style) {
                CardStyle.BASIC -> Theme.colorScheme.onBackground
                CardStyle.PRIMARY -> Theme.colorScheme.onSurface
                CardStyle.PRIMARY_OUTLINE -> Theme.colorScheme.onSurface
                else -> Theme.colorScheme.onSurfaceVariant
            }
        ),
        border = BorderStroke(
            width = 0.5.dp,
            brush = when (style) {
                CardStyle.PRIMARY_OUTLINE -> Brush.radialGradient(
                    colors = listOf(
                        Theme.colorScheme.outline.copy(alpha = Alpha.LOW),
                        Theme.colorScheme.outline.copy(alpha = Alpha.MEDIUM),
                        Theme.colorScheme.outline.copy(alpha = Alpha.LOW)
                    )
                )

                CardStyle.SECONDARY_OUTLINE -> Brush.radialGradient(
                    colors = listOf(
                        Theme.colorScheme.outline.copy(alpha = Alpha.LOW),
                        Theme.colorScheme.outline.copy(alpha = Alpha.MEDIUM),
                        Theme.colorScheme.outline.copy(alpha = Alpha.LOW)
                    )
                )

                else -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
            }
        ),
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SCard(
                modifier = Modifier,
                style = CardStyle.BASIC,
                shape = RoundedCornerShape(4.dp),
                content = {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        SBodyMediumText("테스트")
                        SBodyMediumText("테스트")
                    }
                }
            )
            SCard(
                modifier = Modifier,
                style = CardStyle.PRIMARY,
                shape = RoundedCornerShape(4.dp),
                content = {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        SBodyMediumText("테스트")
                        SBodyMediumText("테스트")
                    }
                }
            )
        }
    }
}