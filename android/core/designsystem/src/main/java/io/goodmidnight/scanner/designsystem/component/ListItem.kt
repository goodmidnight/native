package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SListItem(
    headline: String,
    modifier: Modifier = Modifier,
    supportingContent: String? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    showDivider: Boolean = true
) {
    val roundedShape = RoundedCornerShape(16.dp)

    val clickModifier = if (onClick != null) {
        Modifier.bounceClick(onClick = onClick)
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(roundedShape)
            .then(clickModifier)
            .background(Theme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingContent != null) {
                    leadingContent()
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    SParagraphLargeText(
                        text = headline,
                        color = Theme.colorScheme.primaryText
                    )
                    if (supportingContent != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        SParagraphSmallText(
                            text = supportingContent,
                            color = Theme.colorScheme.secondaryText
                        )
                    }
                }
            }

            if (trailingContent != null) {
                Spacer(modifier = Modifier.width(16.dp))
                trailingContent()
            }
        }

        if (showDivider) {
            Spacer(modifier = Modifier.height(14.dp))
            // Custom-drawn divider replacing M3 HorizontalDivider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Theme.colorScheme.outlineVariant)
            )
        }
    }
}

@Composable
@ComponentPreview
fun SListItemPreview() {
    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SListItem(
                headline = "Enable Auto Scan",
                supportingContent = "Automatically captures when a document is detected by the camera.",
                onClick = {},
                trailingContent = {
                    SToggleSwitch(checked = true, onCheckedChange = {})
                }
            )

            SListItem(
                headline = "PDF Export Quality",
                supportingContent = "High Quality (300 DPI)",
                onClick = {},
                trailingContent = {
                    SLabelMediumText("Change", color = Theme.colorScheme.accent)
                }
            )

            SListItem(
                headline = "Notification Settings",
                showDivider = false,
                onClick = {}
            )
        }
    }
}
