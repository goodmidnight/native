package io.goodmidnight.scanner.ui.feature.camera.result.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.core.save.DocumentSaveController.SaveFormat
import io.goodmidnight.scanner.designsystem.component.SBodyMediumText
import io.goodmidnight.scanner.designsystem.component.SButton
import io.goodmidnight.scanner.designsystem.component.SButtonType
import io.goodmidnight.scanner.designsystem.component.SDialog
import io.goodmidnight.scanner.designsystem.component.STitleMediumText
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme
import io.goodmidnight.scanner.ui.core.utils.LocalSnackbarHostState
import io.goodmidnight.scanner.ui.feature.camera.result.data.ResultState
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedState
import io.goodmidnight.scanner.designsystem.theme.Icons as AppIcons

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                color = Color.Black.copy(alpha = 0.7f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomBarButton(
                        onClick = { bitmap?.let { onShare(it) } },
                        icon = AppIcons.Share,
                        text = "공유"
                    )
                    BottomBarButton(
                        onClick = { bitmap?.let { onSave(it) } },
                        icon = AppIcons.Folder,
                        text = "폴더에 저장",
                        enabled = bitmap != null
                    )
                    if (!captureResult?.fullText.isNullOrBlank()) {
                        BottomBarButton(
                            onClick = {
                                captureResult.fullText.let {
                                    clipboardManager.setText(AnnotatedString(it))
                                    onTextCopy()
                                }
                            },
                            icon = AppIcons.SaveAlt,
                            text = "복사"
                        )
                    }
                }
            }
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            val hasText = !captureResult?.fullText.isNullOrBlank()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                bitmap?.let { b ->
                    Surface(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(b.width.toFloat() / b.height.toFloat()),
                        shadowElevation = 12.dp,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Box {
                            Image(
                                bitmap = b.asImageBitmap(),
                                contentDescription = "Scanned Document",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                            OcrOverlay(
                                bitmapWidth = b.width,
                                bitmapHeight = b.height,
                                blocks = captureResult.ocrBlocks
                            )
                        }
                    }
                }
            }

            if (hasText) {
                val extractedText = captureResult.fullText
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    STitleMediumText(
                        text = "추출된 텍스트",
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SelectionContainer {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.DarkGray.copy(alpha = 0.5f))
                                .padding(20.dp)
                        ) {
                            SBodyMediumText(
                                text = extractedText,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
        if (state.showFormatDialog) {
            SDialog(onDismissRequest = onDismissDialog) {
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
                        onClick = onDismissDialog,
                        type = SButtonType.SECONDARY,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomBarButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier.clickable(enabled = enabled, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (enabled) Color.White else Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        SBodyMediumText(
            text = text,
            color = if (enabled) Color.White else Color.Gray
        )
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
                sharedState = SharedState(),
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
