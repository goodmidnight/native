package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Theme.colorScheme.onAccent,
            checkedTrackColor = Theme.colorScheme.accent,
            checkedBorderColor = Theme.colorScheme.accent,
            uncheckedThumbColor = Theme.colorScheme.secondaryIcon,
            uncheckedTrackColor = Theme.colorScheme.surfaceVariant,
            uncheckedBorderColor = Theme.colorScheme.surfaceVariant
        )
    )
}

@Composable
@ComponentPreview
fun SSwitchPreview() {
    Theme {
        Column {
            SToggleSwitch(
                checked = true,
                onCheckedChange = {}
            )
            SToggleSwitch(
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}