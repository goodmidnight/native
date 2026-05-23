package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import io.goodmidnight.scanner.designsystem.theme.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.modifier.softShadow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SAccordion(
    title: String,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    val roundedShape = RoundedCornerShape(20.dp)

    val arrowRotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = spring(stiffness = 300f, dampingRatio = 0.75f),
        label = "AccordionArrowRotation"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .softShadow(borderRadius = 20.dp, shadowRadius = 12.dp, offsetY = 1.dp)
            .clip(roundedShape)
            .background(Theme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .bounceClick { expanded = !expanded }
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SHeadingSmallText(
                text = title,
                color = Theme.colorScheme.primaryText
            )
            SIcon(
                imageVector = Icons.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = Theme.colorScheme.secondaryText,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(arrowRotationAngle)
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(
                animationSpec = spring(stiffness = 250f, dampingRatio = 0.8f)
            ) + fadeIn(animationSpec = spring(stiffness = 250f)),
            exit = shrinkVertically(
                animationSpec = spring(stiffness = 250f, dampingRatio = 0.8f)
            ) + fadeOut(animationSpec = spring(stiffness = 250f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                content()
            }
        }
    }
}

@Composable
@ComponentPreview
fun SAccordionPreview() {
    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SAccordion(title = "Auto Scan Guide") {
                SParagraphMediumText(
                    text = "Hold the scanner camera straight over the document and wait. Capture begins automatically once the green outline fills the area. Keep lighting moderate.",
                    color = Theme.colorScheme.secondaryText
                )
            }

            SAccordion(title = "Supported Export Formats", initiallyExpanded = true) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SParagraphMediumText(
                        text = "1. PDF (Multi-page merge supported)",
                        color = Theme.colorScheme.secondaryText
                    )
                    SParagraphMediumText(
                        text = "2. JPEG (Adjustable compression)",
                        color = Theme.colorScheme.secondaryText
                    )
                    SParagraphMediumText(
                        text = "3. TXT (OCR text extraction)",
                        color = Theme.colorScheme.secondaryText
                    )
                }
            }
        }
    }
}
