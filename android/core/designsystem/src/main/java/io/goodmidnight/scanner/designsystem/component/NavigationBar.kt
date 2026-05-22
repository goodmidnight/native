package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.VideoLabel
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.noRippleClickable
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Alpha
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
) {
    Row(
        modifier = Modifier
            .background(
                Theme.colorScheme.background.copy(alpha = Alpha.HIGH),
                RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
            .border(
                BorderStroke(
                    0.5.dp,
                    Theme.colorScheme.outlineVariant.copy(alpha = Alpha.LOW)
                ), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )

            .padding(vertical = 8.dp),
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val textColor: Color by animateColorAsState(
                if (isSelected) Theme.colorScheme.primary else Theme.colorScheme.secondaryText,
                label = "textColor"
            )
            val iconColor: Color by animateColorAsState(
                if (isSelected) Theme.colorScheme.primary else Theme.colorScheme.secondaryIcon,
                label = "iconColor"
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .noRippleClickable { onItemClick(item) },
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = iconColor
                )
                SLabelMediumText(
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
                listOf(
                    NavigationItem("비디오", "비디오", Icons.Rounded.VideoLabel, "home"),
                    NavigationItem("재생목록", "재생목록", Icons.Rounded.VideoLibrary, "profile"),
                    NavigationItem("전체", "전체", Icons.Rounded.Menu, "settings")
                ),
                currentRoute = "home",
                onItemClick = {}
            )
        }
    }
}
