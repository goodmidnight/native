package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

/**
 * [SCheckBox]
 * - Material 3 CheckBox에 의존하지 않고 Box 기반으로 직접 구현한 둥근 사각형 다중 선택 체크박스입니다.
 * - 체크 선택 시 내측 체크 마크가 톡 튀어오르는 [scale] 애니메이션과 클릭 시 [bounceClick] 효과를 내장합니다.
 */
@Composable
fun SCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val checkboxShape = RoundedCornerShape(6.dp)

    // 선택 여부에 따른 배경색 및 테두리색 스무스 트랜지션
    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> Theme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            checked -> Theme.colorScheme.accent
            else -> Theme.colorScheme.surface
        },
        label = "CheckBoxBgColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> Theme.colorScheme.outlineVariant
            checked -> Theme.colorScheme.accent
            else -> Theme.colorScheme.outline
        },
        label = "CheckBoxBorderColor"
    )

    // 체크마크가 활성화될 때 톡 튀어나오는 스케일 효과
    val checkMarkScale by animateFloatAsState(
        targetValue = if (checked) 1.0f else 0.0f,
        label = "CheckBoxMarkScale"
    )

    Box(
        modifier = modifier
            .bounceClick(enabled = enabled) { onCheckedChange(!checked) }
            .size(22.dp)
            .clip(checkboxShape)
            .background(backgroundColor)
            .border(1.5.dp, borderColor, checkboxShape),
        contentAlignment = Alignment.Center
    ) {
        SIcon(
            imageVector = Icons.Rounded.Check,
            contentDescription = null,
            tint = Theme.colorScheme.onAccent,
            modifier = Modifier
                .size(16.dp)
                .scale(checkMarkScale)
        )
    }
}

@Composable
@ComponentPreview
fun SCheckBoxPreview() {
    var checked1 by remember { mutableStateOf(true) }
    var checked2 by remember { mutableStateOf(false) }

    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SCheckBox(checked = checked1, onCheckedChange = { checked1 = it })
                Spacer(modifier = Modifier.width(10.dp))
                SParagraphMediumText("선택된 체크박스")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                SCheckBox(checked = checked2, onCheckedChange = { checked2 = it })
                Spacer(modifier = Modifier.width(10.dp))
                SParagraphMediumText("선택 해제된 체크박스")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                SCheckBox(checked = true, onCheckedChange = {}, enabled = false)
                Spacer(modifier = Modifier.width(10.dp))
                SParagraphMediumText("비활성화된 체크박스", color = Theme.colorScheme.disabledText)
            }
        }
    }
}
