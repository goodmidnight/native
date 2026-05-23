package io.goodmidnight.scanner.ui.feature.settings.ui.component

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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.component.SBodyLargeText
import io.goodmidnight.scanner.designsystem.component.SBodyMediumText
import io.goodmidnight.scanner.designsystem.component.SRadioButton
import io.goodmidnight.scanner.designsystem.component.STitleMediumText
import io.goodmidnight.scanner.designsystem.component.SToggleSwitch
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SettingsHeader(title: String) {
    STitleMediumText(
        text = title,
        color = Theme.colorScheme.accent,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            SBodyLargeText(
                text = title,
                color = Theme.colorScheme.primaryText
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                SBodyMediumText(
                    text = subtitle,
                    color = Theme.colorScheme.secondaryText
                )
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
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                onCheckedChange(!checked)
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SBodyLargeText(
            text = title,
            modifier = Modifier.weight(1f),
            color = Theme.colorScheme.primaryText
        )
        SToggleSwitch(
            checked = checked,
            onCheckedChange = { checkedState ->
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                onCheckedChange(checkedState)
            }
        )
    }
}

@Composable
fun <T> RadioGroupSettingsItem(
    title: String,
    options: List<Pair<String, T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        SBodyLargeText(
            text = title,
            color = Theme.colorScheme.primaryText
        )
        Spacer(modifier = Modifier.height(12.dp))
        options.forEach { (text, option) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .bounceClick {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                        onOptionSelected(option)
                    }
                    .padding(vertical = 8.dp)
            ) {
                SRadioButton(
                    selected = (option == selectedOption),
                    onClick = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                        onOptionSelected(option)
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                SBodyLargeText(
                    text = text,
                    color = Theme.colorScheme.primaryText
                )
            }
        }
    }
}
