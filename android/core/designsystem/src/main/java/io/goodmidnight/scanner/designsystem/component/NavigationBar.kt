package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.VideoLabel
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.modifier.softShadow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

data class NavigationItem(
    val name: String,
    val label: String,
    val icon: ImageVector,
    val route: String,
)

@Composable
fun SNavigationBar(
    items: List<NavigationItem>,
    currentRoute: String?,
    onItemClick: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val barShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .softShadow(borderRadius = 24.dp, shadowRadius = 20.dp, offsetY = (-3).dp)
            .clip(barShape)
            .background(Theme.colorScheme.surface)
            .border(1.dp, Theme.colorScheme.outlineVariant, barShape)
            .navigationBarsPadding()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            val textColor: Color by animateColorAsState(
                if (isSelected) Theme.colorScheme.primaryText else Theme.colorScheme.secondaryText,
                label = "textColor"
            )
            val iconColor: Color by animateColorAsState(
                if (isSelected) Theme.colorScheme.primaryText else Theme.colorScheme.secondaryIcon,
                label = "iconColor"
            )

            val iconScale by animateFloatAsState(
                targetValue = if (isSelected) 1.12f else 1.0f,
                animationSpec = spring(dampingRatio = 0.58f, stiffness = 300f),
                label = "NavIconScale"
            )
            val iconOffsetY by animateDpAsState(
                targetValue = if (isSelected) (-3).dp else 0.dp,
                animationSpec = spring(dampingRatio = 0.58f, stiffness = 300f),
                label = "NavIconOffsetY"
            )

            val indicatorWidth by animateDpAsState(
                targetValue = if (isSelected) 46.dp else 0.dp,
                animationSpec = spring(dampingRatio = 0.72f, stiffness = 350f),
                label = "NavIndicatorWidth"
            )
            val indicatorAlpha by animateFloatAsState(
                targetValue = if (isSelected) 1.0f else 0.0f,
                animationSpec = spring(stiffness = 400f),
                label = "NavIndicatorAlpha"
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .bounceClick(onClick = { onItemClick(item) })
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.height(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = indicatorWidth, height = 26.dp)
                            .graphicsLayer { alpha = indicatorAlpha }
                            .clip(RoundedCornerShape(13.dp))
                            .background(Theme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    )

                    SIcon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = iconColor,
                        modifier = Modifier
                            .offset(y = iconOffsetY)
                            .graphicsLayer {
                                scaleX = iconScale
                                scaleY = iconScale
                            }
                            .size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                SLabelSmallText(
                    text = item.label,
                    color = textColor
                )
            }
        }
    }
}

@Composable
@ComponentPreview
fun SNavigationBarPreview() {
    Theme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colorScheme.background),
            verticalArrangement = Arrangement.Bottom
        ) {
            SNavigationBar(
                items = listOf(
                    NavigationItem("library", "보관함", Icons.Rounded.VideoLabel, "library"),
                    NavigationItem("menu", "메뉴", Icons.Rounded.Menu, "menu")
                ),
                currentRoute = "scan",
                onItemClick = {}
            )
        }
    }
}
