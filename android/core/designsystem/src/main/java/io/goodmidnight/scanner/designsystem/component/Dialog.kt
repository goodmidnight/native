package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.spring
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.goodmidnight.scanner.designsystem.modifier.softShadow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable ColumnScope.() -> Unit
) {
    var isAnimatedVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isAnimatedVisible = true
    }

    val animateDismiss = {
        coroutineScope.launch {
            isAnimatedVisible = false
            delay(180)
            onDismissRequest()
        }
    }

    Dialog(
        onDismissRequest = { animateDismiss() },
        properties = properties
    ) {
        AnimatedVisibility(
            visible = isAnimatedVisible,
            enter = scaleIn(initialScale = 0.92f, animationSpec = spring(dampingRatio = 0.78f, stiffness = 400f)) + fadeIn(),
            exit = scaleOut(targetScale = 0.95f, animationSpec = spring(dampingRatio = 0.85f, stiffness = 450f)) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .softShadow(borderRadius = 28.dp, shadowRadius = 30.dp, offsetY = 6.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Theme.colorScheme.surface)
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
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
        SHeadingMediumText(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(12.dp))
        SParagraphMediumText(
            text = message,
            modifier = Modifier.fillMaxWidth(),
            color = Theme.colorScheme.secondaryText,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(28.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
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
            title = "Confirm Action",
            message = "Applying this setting will overhaul the existing design system to a monotone style. Proceed?",
            confirmText = "Confirm",
            dismissText = "Go Back",
            onConfirm = {},
            onDismissRequest = {}
        )
    }
}