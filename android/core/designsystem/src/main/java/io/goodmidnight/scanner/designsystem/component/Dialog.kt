package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Theme.colorScheme.surface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

@Composable
fun SConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    dismissText: String? = null,
    onDismiss: (() -> Unit)? = null,
) {
    SDialog(onDismissRequest = onDismissRequest) {
        STitleMediumText(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Start
        )
        Spacer(modifier = Modifier.height(8.dp))
        SBodyMediumText(
            text = message,
            modifier = Modifier.fillMaxWidth(),
            color = Theme.colorScheme.secondaryText,
            textAlign = androidx.compose.ui.text.style.TextAlign.Start
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            if (dismissText != null) {
                SButton(
                    text = dismissText,
                    onClick = {
                        onDismiss?.invoke()
                        onDismissRequest()
                    },
                    type = SButtonType.SECONDARY,
                    modifier = Modifier.weight(1f)
                )
            }
            SButton(
                text = confirmText,
                onClick = {
                    onConfirm()
                    onDismissRequest()
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
@ComponentPreview
fun SConfirmDialogPreview() {
    Theme {
        SConfirmDialog(
            title = "확인창",
            message = "정말로 진행하시겠습니까? 이 작업은 되돌릴 수 없습니다.",
            confirmText = "확인",
            dismissText = "취소",
            onConfirm = {},
            onDismissRequest = {}
        )
    }
}