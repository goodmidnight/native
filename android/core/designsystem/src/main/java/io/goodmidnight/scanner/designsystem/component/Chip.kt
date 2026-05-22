package io.goodmidnight.scanner.designsystem.component


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Alpha
import io.goodmidnight.scanner.designsystem.theme.Icons
import io.goodmidnight.scanner.designsystem.theme.Theme

/**
 * 필터링, 태그 선택 등 간단한 선택/해제에 사용되는 칩 버튼입니다.
 *
 * @param text 칩에 표시될 텍스트입니다.
 * @param selected 칩의 선택 여부입니다.
 * @param onClick 칩을 클릭했을 때 호출될 콜백입니다.
 * @param enabled 칩의 활성화 여부입니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            SLabelMediumText(
                text = text,
                color = when {
                    !enabled -> Theme.colorScheme.disabledText
                    selected -> Theme.colorScheme.onAccent
                    else -> Theme.colorScheme.secondaryText
                }
            )
        },
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Theme.colorScheme.onSurfaceVariant.copy(Alpha.HIGH),
            containerColor = Color.Transparent,
            disabledSelectedContainerColor = Theme.colorScheme.surfaceVariant.copy(alpha = Alpha.MEDIUM),
            disabledContainerColor = Color.Transparent
        ),
        border = BorderStroke(
            width = 1.dp,
            brush = when {
                !enabled -> Brush.radialGradient(
                    colors = listOf(
                        Theme.colorScheme.outline.copy(alpha = Alpha.LOW),
                        Theme.colorScheme.outline.copy(alpha = Alpha.MEDIUM),
                        Theme.colorScheme.outline.copy(alpha = Alpha.LOW)
                    )
                )

                selected -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
                else -> Brush.radialGradient(
                    colors = listOf(
                        Theme.colorScheme.outlineVariant.copy(alpha = Alpha.LOW),
                        Theme.colorScheme.outlineVariant.copy(alpha = Alpha.MEDIUM),
                        Theme.colorScheme.outlineVariant.copy(alpha = Alpha.LOW)
                    )
                )
            }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            SLabelMediumText(
                text = text,
                color = when {
                    !enabled -> Theme.colorScheme.disabledText
                    selected -> Theme.colorScheme.primaryText
                    else -> Theme.colorScheme.secondaryText
                }
            )
        },
        trailingIcon = trailingIcon?.let { icon ->
            {
                Icon(
                    imageVector = icon,
                    contentDescription = "$icon",
                    tint = when {
                        !enabled -> Theme.colorScheme.disabledText
                        selected -> Theme.colorScheme.primaryText
                        else -> Theme.colorScheme.secondaryText
                    },
                    modifier = modifier.size(20.dp)
                )
            }
        },
        leadingIcon = leadingIcon?.let { icon ->
            {
                Icon(
                    imageVector = icon,
                    contentDescription = "$icon",
                    tint = when {
                        !enabled -> Theme.colorScheme.disabledText
                        selected -> Theme.colorScheme.primaryText
                        else -> Theme.colorScheme.secondaryText
                    },
                    modifier = modifier.size(20.dp)
                )
            }
        },
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color.Transparent,
            containerColor = Color.Transparent,
            disabledSelectedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                !enabled -> Theme.colorScheme.outline.copy(alpha = Alpha.MEDIUM)
                selected -> Theme.colorScheme.outline
                else -> Theme.colorScheme.outlineVariant
            }
        )
    )
}

@ComponentPreview
@Composable
fun SChipsPreview() {
    var selectedChip1 by remember { mutableStateOf(true) }
    var selectedChip2 by remember { mutableStateOf(false) }

    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SChip(
                text = "선택됨",
                selected = selectedChip1,
                onClick = { selectedChip1 = !selectedChip1 })
            SChip(
                text = "선택 안됨",
                selected = selectedChip2,
                onClick = { selectedChip2 = !selectedChip2 })

            SChip(text = "비활성 (선택됨)", selected = true, onClick = {}, enabled = false)
            SChip(text = "비활성 (선택 안됨)", selected = false, onClick = {}, enabled = false)

            SFilterChip(
                text = "최신순",
                trailingIcon = Icons.ArrowDropUp,
                selected = selectedChip1,
                onClick = { selectedChip1 = !selectedChip1 })
            SFilterChip(
                text = "인기순",
                trailingIcon = Icons.ArrowDropDown,
                selected = selectedChip2,
                onClick = { selectedChip2 = !selectedChip2 })
            SFilterChip(text = "비활성 (선택됨)", selected = true, onClick = {}, enabled = false)
            SFilterChip(text = "비활성 (선택 안됨)", selected = false, onClick = {}, enabled = false)
        }
    }
}