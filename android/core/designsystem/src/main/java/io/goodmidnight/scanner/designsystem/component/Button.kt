package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Alpha
import io.goodmidnight.scanner.designsystem.theme.Theme

enum class SButtonType {
    PRIMARY,
    SECONDARY
}

@Composable
fun SButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: SButtonType = SButtonType.PRIMARY,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val content: @Composable () -> Unit = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            val textColor = when (type) {
                SButtonType.PRIMARY -> if (enabled) Theme.colorScheme.onAccent else Theme.colorScheme.onAccent.copy(alpha = Alpha.MEDIUM)
                SButtonType.SECONDARY -> if (enabled) Theme.colorScheme.primaryText else Theme.colorScheme.disabledText
            }
            STitleSmallText(
                text = text,
                color = textColor,
            )
        }
    }

    if (type == SButtonType.PRIMARY) {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Theme.colorScheme.accent,
                contentColor = Theme.colorScheme.onAccent,
                disabledContainerColor = Theme.colorScheme.accent.copy(alpha = Alpha.LOW),
                disabledContentColor = Theme.colorScheme.onAccent.copy(alpha = Alpha.MEDIUM)
            )
        ) {
            content()
        }
    } else { // SECONDARY
        OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            border = BorderStroke(1.dp, Theme.colorScheme.outline),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Theme.colorScheme.primaryText,
                disabledContentColor = Theme.colorScheme.disabledText
            )
        ) {
            content()
        }
    }
}


@Composable
@ComponentPreview
fun SButtonPreview() {
    Theme {
        Column(
            modifier = Modifier.background(Theme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SButton(
                onClick = {},
                text = "테스트"
            )
            SButton(
                onClick = {},
                enabled = false,
                text = "테스트"
            )
            SButton(
                onClick = {},
                type = SButtonType.SECONDARY,
                text = "테스트"
            )
            SButton(
                onClick = {},
                enabled = false,
                type = SButtonType.SECONDARY,
                text = "테스트"
            )
        }
    }
}