package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.modifier.softShadow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SSegmentedControl(
    segments: List<String>,
    selectedIndex: Int,
    onSegmentSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val controlShape = RoundedCornerShape(24.dp)
    val outerPadding = 4.dp

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .softShadow(borderRadius = 24.dp, shadowRadius = 15.dp, offsetY = 2.dp)
            .clip(controlShape)
            .background(Theme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            .padding(outerPadding),
        contentAlignment = Alignment.CenterStart
    ) {
        val totalWidth = maxWidth
        val segmentCount = segments.size.coerceAtLeast(1)
        val segmentWidth = (totalWidth - (outerPadding * 2)) / segmentCount

        val activeXOffset by animateDpAsState(
            targetValue = segmentWidth * selectedIndex,
            animationSpec = spring(dampingRatio = 0.78f, stiffness = 380f),
            label = "SegmentActiveOffset"
        )

        Box(
            modifier = Modifier
                .offset(x = activeXOffset)
                .width(segmentWidth)
                .fillMaxHeight()
                .softShadow(borderRadius = 20.dp, shadowRadius = 8.dp, offsetY = 1.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Theme.colorScheme.surface)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            segments.forEachIndexed { index, text ->
                val isSelected = index == selectedIndex

                val textColor by animateColorAsState(
                    targetValue = if (isSelected) Theme.colorScheme.primaryText else Theme.colorScheme.secondaryText,
                    label = "SegmentTextColor"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .bounceClick { onSegmentSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    SLabelMediumText(
                        text = text,
                        color = textColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
@ComponentPreview
fun SSegmentedControlPreview() {
    var selectedIndex1 by remember { mutableStateOf(0) }
    var selectedIndex2 by remember { mutableStateOf(1) }

    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SSegmentedControl(
                segments = listOf("Scan Mode", "Import", "Cloud"),
                selectedIndex = selectedIndex1,
                onSegmentSelected = { selectedIndex1 = it }
            )

            Box(modifier = Modifier.height(20.dp))

            SSegmentedControl(
                segments = listOf("PDF Format", "Image"),
                selectedIndex = selectedIndex2,
                onSegmentSelected = { selectedIndex2 = it }
            )
        }
    }
}
