package io.goodmidnight.scanner.ui.feature.camera.result.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.core.save.DocumentSaveController.SaveFormat
import io.goodmidnight.scanner.designsystem.component.CardStyle
import io.goodmidnight.scanner.designsystem.component.SBodyMediumText
import io.goodmidnight.scanner.designsystem.component.SButton
import io.goodmidnight.scanner.designsystem.component.SButtonType
import io.goodmidnight.scanner.designsystem.component.SCard
import io.goodmidnight.scanner.designsystem.component.SDialog
import io.goodmidnight.scanner.designsystem.component.SIcon
import io.goodmidnight.scanner.designsystem.component.SScaffold
import io.goodmidnight.scanner.designsystem.component.STitleMediumText
import io.goodmidnight.scanner.designsystem.component.STitleTopBar
import io.goodmidnight.scanner.designsystem.modifier.bounceClick
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme
import io.goodmidnight.scanner.ui.core.utils.LocalSnackbarHostState
import io.goodmidnight.scanner.ui.feature.camera.result.data.ResultState
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedState

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    state: ResultState,
    sharedState: SharedState,
    onBack: () -> Unit,
    onSave: (Bitmap) -> Unit,
    onSelectFormat: (SaveFormat) -> Unit,
    onDismissDialog: () -> Unit,
    onShare: (Bitmap) -> Unit,
    onTextCopy: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val captureResult = sharedState.captureResult
    val bitmap = captureResult?.image
    val hasText = !captureResult?.fullText.isNullOrBlank()

    SScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            STitleTopBar(
                title = "스캔 결과",
                onBack = onBack
            )
        },
        bottomBar = {
            ResultBottomBar(
                bitmap = bitmap,
                hasText = hasText,
                onShare = { bitmap?.let { onShare(it) } },
                onSave = { bitmap?.let { onSave(it) } },
                onCopy = {
                    captureResult?.fullText?.let {
                        clipboardManager.setText(AnnotatedString(it))
                        onTextCopy()
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            ResultDocumentCard(
                bitmap = bitmap,
                ocrBlocks = captureResult?.ocrBlocks ?: emptyList()
            )

            if (hasText) {
                ResultExtractedTextPanel(text = captureResult?.fullText.orEmpty())
            }
        }

        if (state.showFormatDialog) {
            ResultFormatDialog(
                onDismiss = onDismissDialog,
                onSelectFormat = onSelectFormat
            )
        }
    }
}

@Composable
private fun ResultDocumentCard(
    modifier: Modifier = Modifier,
    bitmap: Bitmap?,
    ocrBlocks: List<io.goodmidnight.scanner.model.OcrBlock>
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(450.dp)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let { b ->
            SCard(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(b.width.toFloat() / b.height.toFloat()),
                style = CardStyle.PRIMARY_OUTLINE,
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        bitmap = b.asImageBitmap(),
                        contentDescription = "Scanned Document",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                    OcrOverlay(
                        bitmapWidth = b.width,
                        bitmapHeight = b.height,
                        blocks = ocrBlocks
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultExtractedTextPanel(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        STitleMediumText(
            text = "추출된 텍스트",
            color = Theme.colorScheme.primaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        SelectionContainer {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Theme.colorScheme.surfaceVariant)
                    .padding(20.dp)
            ) {
                SBodyMediumText(
                    text = text,
                    color = Theme.colorScheme.primaryText
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun ResultBottomBar(
    modifier: Modifier = Modifier,
    bitmap: Bitmap?,
    hasText: Boolean,
    onShare: () -> Unit,
    onSave: () -> Unit,
    onCopy: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Theme.colorScheme.surface)
            .navigationBarsPadding()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ResultBottomBarButton(
                onClick = onShare,
                icon = io.goodmidnight.scanner.designsystem.theme.Icons.Share,
                text = "공유",
                enabled = bitmap != null
            )
            ResultBottomBarButton(
                onClick = onSave,
                icon = io.goodmidnight.scanner.designsystem.theme.Icons.Folder,
                text = "저장",
                enabled = bitmap != null
            )
            if (hasText) {
                ResultBottomBarButton(
                    onClick = onCopy,
                    icon = io.goodmidnight.scanner.designsystem.theme.Icons.SaveAlt,
                    text = "텍스트 복사",
                    enabled = true
                )
            }
        }
    }
}

@Composable
private fun ResultBottomBarButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val contentColor = if (enabled) Theme.colorScheme.primaryText else Theme.colorScheme.disabledText

    Column(
        modifier = modifier
            .bounceClick(enabled = enabled) {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SIcon(
            imageVector = icon,
            contentDescription = text,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        SBodyMediumText(
            text = text,
            color = contentColor
        )
    }
}

@Composable
private fun ResultFormatDialog(
    onDismiss: () -> Unit,
    onSelectFormat: (SaveFormat) -> Unit
) {
    SDialog(onDismissRequest = onDismiss) {
        STitleMediumText(
            text = "저장 형식 선택",
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        SBodyMediumText(
            text = "스캔한 문서를 저장할 포맷을 선택해 주세요.",
            modifier = Modifier.fillMaxWidth(),
            color = Theme.colorScheme.secondaryText,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SButton(
                text = "JPEG 이미지로 저장",
                onClick = { onSelectFormat(SaveFormat.JPEG) },
                modifier = Modifier.fillMaxWidth()
            )
            SButton(
                text = "PNG 이미지로 저장",
                onClick = { onSelectFormat(SaveFormat.PNG) },
                modifier = Modifier.fillMaxWidth()
            )
            SButton(
                text = "PDF 문서로 저장",
                onClick = { onSelectFormat(SaveFormat.PDF) },
                modifier = Modifier.fillMaxWidth()
            )
            SButton(
                text = "취소",
                onClick = onDismiss,
                type = SButtonType.SECONDARY,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
@ComponentPreview
fun SResultScreenPreview() {
    Theme {
        CompositionLocalProvider(
            LocalSnackbarHostState provides SnackbarHostState(),
        ) {
            ResultScreen(
                state = ResultState(),
                sharedState = SharedState(
                    captureResult = SharedState.CaptureResultState(
                        image = null,
                        status = SharedState.CaptureStatus.SUCCESS,
                        isBlurry = false,
                        hasGlare = false,
                        message = "",
                        fullText = "Hello! This is scanned text with rich layout.",
                        ocrBlocks = emptyList()
                    )
                ),
                onBack = {},
                onSave = {},
                onSelectFormat = {},
                onDismissDialog = {},
                onShare = {},
                onTextCopy = {}
            )
        }
    }
}
