package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Alpha
import io.goodmidnight.scanner.designsystem.theme.Theme


@Composable
fun SRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {

    RadioButton(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        interactionSource = interactionSource,
        colors = RadioButtonDefaults.colors(
            selectedColor = Theme.colorScheme.accent,
            unselectedColor = Theme.colorScheme.outline,
            disabledSelectedColor = Theme.colorScheme.disabledText,
            disabledUnselectedColor = Theme.colorScheme.disabledText.copy(alpha = Alpha.LOW),
        ),
    )
}


@ComponentPreview
@Composable
fun SRadioButtonPreview() {
    var isSelected1 by remember { mutableStateOf(true) }
    var isSelected2 by remember { mutableStateOf(false) }
    Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SRadioButton(
                selected = isSelected1,
                onClick = { isSelected1 = !isSelected1 },
            )
            SRadioButton(
                selected = isSelected2,
                onClick = { isSelected2 = !isSelected2 },
            )
            SRadioButton(
                selected = true,
                onClick = null,
                enabled = false
            )
        }
    }
}
