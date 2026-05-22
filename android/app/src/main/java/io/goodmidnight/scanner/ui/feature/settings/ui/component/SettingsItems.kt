package io.goodmidnight.scanner.ui.feature.settings.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.component.SBodyLargeText
import io.goodmidnight.scanner.designsystem.component.SBodyMediumText
import io.goodmidnight.scanner.designsystem.component.SRadioButton
import io.goodmidnight.scanner.designsystem.component.STitleMediumText
import io.goodmidnight.scanner.designsystem.component.SToggleSwitch
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SettingsHeader(title: String) {
    STitleMediumText(
        text = title,
        color = Theme.colorScheme.primary,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            SBodyLargeText(text = title)
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                SBodyMediumText(text = subtitle, color = Theme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun SwitchSettingsItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SBodyLargeText(text = title, modifier = Modifier.weight(1f))
        SToggleSwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun <T> RadioGroupSettingsItem(
    title: String,
    options: List<Pair<String, T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        SBodyLargeText(text = title)
        Spacer(modifier = Modifier.height(8.dp))
        options.forEach { (text, option) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(option) }
                    .padding(vertical = 4.dp)
            ) {
                SRadioButton(
                    selected = (option == selectedOption),
                    onClick = { onOptionSelected(option) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                SBodyLargeText(text = text)
            }
        }
    }
}
