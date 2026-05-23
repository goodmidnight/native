package io.goodmidnight.scanner.ui.feature.camera.crop.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.component.SButton
import io.goodmidnight.scanner.designsystem.component.SButtonType
import io.goodmidnight.scanner.designsystem.component.SScaffold
import io.goodmidnight.scanner.designsystem.component.STitleTopBar
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme
import io.goodmidnight.scanner.ui.core.utils.LocalSnackbarHostState
import io.goodmidnight.scanner.ui.feature.camera.crop.data.CropEditState
import io.goodmidnight.scanner.ui.feature.camera.crop.ui.component.CropOverlay
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedState

@Composable
fun CropEditScreen(
    modifier: Modifier = Modifier,
    state: CropEditState,
    sharedState: SharedState,
    onBack: () -> Unit,
    onComplete: (List<androidx.compose.ui.geometry.Offset>) -> Unit,
) {
    val bitmap = state.originalImage
    val imageBitmap = remember(bitmap) { bitmap?.asImageBitmap() }
    val frameState = sharedState.detectedFrame

    val finalPointsRef = remember(state.vertexPoints) {
        state.vertexPoints.toMutableList()
    }

    SScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            STitleTopBar(
                title = "Adjust Borders",
                onBack = onBack
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.colorScheme.surface)
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SButton(
                    text = "Retake",
                    type = SButtonType.SECONDARY,
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                )
                SButton(
                    text = "Complete",
                    type = SButtonType.PRIMARY,
                    onClick = {
                        if (finalPointsRef.size == 4) {
                            onComplete(finalPointsRef.toList())
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null && frameState != null) {
                var canvasWidth by remember { mutableStateOf(0f) }
                var canvasHeight by remember { mutableStateOf(0f) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.85f)
                        .onGloballyPositioned {
                            canvasWidth = it.size.width.toFloat()
                            canvasHeight = it.size.height.toFloat()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Original Raw Document",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    CropOverlay(
                        modifier = Modifier.fillMaxSize(),
                        vertexPoints = state.vertexPoints,
                        initialPoints = state.initialPoints,
                        imageWidth = bitmap.width,
                        imageHeight = bitmap.height,
                        onPinDrag = { index, offset ->
                            if (index in finalPointsRef.indices) {
                                finalPointsRef[index] = offset
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
@ComponentPreview
fun SCropEditScreenPreview() {
    Theme {
        CompositionLocalProvider(
            LocalSnackbarHostState provides SnackbarHostState(),
        ) {
            // Preview Mock state
            val mockState = CropEditState(
                vertexPoints = listOf(
                    androidx.compose.ui.geometry.Offset(100f, 100f),
                    androidx.compose.ui.geometry.Offset(400f, 110f),
                    androidx.compose.ui.geometry.Offset(380f, 600f),
                    androidx.compose.ui.geometry.Offset(90f, 580f)
                ),
                selectedFilter = 0
            )

            // Minimalist scaffold matching layout styling
            SScaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    STitleTopBar(title = "Adjust Borders", onBack = {})
                },
                bottomBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Theme.colorScheme.surface)
                            .padding(24.dp)
                    ) {
                        SButton(text = "Complete", onClick = {}, modifier = Modifier.fillMaxWidth())
                    }
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Theme.colorScheme.background)
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    // Empty canvas placeholder representing raw view screen
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.85f)
                            .background(Color.Gray.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}
