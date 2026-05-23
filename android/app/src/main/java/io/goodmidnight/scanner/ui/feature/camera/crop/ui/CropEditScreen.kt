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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import io.goodmidnight.scanner.designsystem.component.SButton
import io.goodmidnight.scanner.designsystem.component.SButtonType
import io.goodmidnight.scanner.designsystem.component.SScaffold
import io.goodmidnight.scanner.designsystem.component.STitleTopBar
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme
import io.goodmidnight.scanner.ui.core.utils.LocalSnackbarHostState
import io.goodmidnight.scanner.ui.core.utils.showSnackbarImmediately
import io.goodmidnight.scanner.ui.feature.camera.crop.data.CropEditEffect
import io.goodmidnight.scanner.ui.feature.camera.crop.data.CropEditEvent
import io.goodmidnight.scanner.ui.feature.camera.crop.data.CropEditState
import io.goodmidnight.scanner.ui.feature.camera.crop.data.CropEditViewModel
import io.goodmidnight.scanner.ui.feature.camera.crop.ui.component.CropOverlay
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedEvent
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedState
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedViewModel

@Composable
fun CropEditScreen(
    modifier: Modifier = Modifier,
    state: CropEditState,
    sharedState: SharedState,
    onBack: () -> Unit,
    onNavigateToResult: () -> Unit,
    sharedViewModel: SharedViewModel,
    viewModel: CropEditViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = LocalSnackbarHostState.current
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    // Initialize with original captured image and mapped high-res vertices on enter
    LaunchedEffect(sharedState.rawCapturedBitmap, sharedState.detectedFrame) {
        val bitmap = sharedState.rawCapturedBitmap
        val frame = sharedState.detectedFrame
        if (bitmap != null && frame != null) {
            val previewW = frame.imageWidth
            val previewH = frame.imageHeight
            val capturedW = bitmap.width
            val capturedH = bitmap.height

            val scaleX = capturedW.toFloat() / previewW.toFloat()
            val scaleY = capturedH.toFloat() / previewH.toFloat()

            val mappedPoints = FloatArray(frame.points.size)
            for (i in 0 until frame.points.size step 2) {
                if (i + 1 < frame.points.size) {
                    mappedPoints[i] = frame.points[i] * scaleX
                    mappedPoints[i + 1] = frame.points[i + 1] * scaleY
                }
            }
            viewModel.onEvent(CropEditEvent.OnInitialize(bitmap, mappedPoints))
        }
    }

    BackHandler {
        viewModel.onEvent(CropEditEvent.OnBack)
    }

    // Capture viewModel effects
    LaunchedEffect(Unit) {
        viewModel.bindEffect(scope = this) { effect ->
            when (effect) {
                is CropEditEffect.PopBackStack -> {
                    sharedViewModel.onEvent(SharedEvent.OnUpdateStep(SharedState.ScannerStep.PREVIEW))
                    onBack()
                }
                is CropEditEffect.NavigateToResult -> {
                    // Collect final edited vertices in crop state and post OnCompleteCrop event to SharedViewModel
                    val points = viewModel.state.value.vertexPoints
                    if (points.size == 4) {
                        val rawPts = FloatArray(8)
                        for (i in 0 until 4) {
                            rawPts[i * 2] = points[i].x
                            rawPts[i * 2 + 1] = points[i].y
                        }
                        // Fire event in shared view model to compute warp and process OCR/save
                        sharedViewModel.onEvent(
                            SharedEvent.OnCompleteCrop(
                                points = rawPts,
                                selectedFilter = viewModel.state.value.selectedFilter
                            )
                        )
                    }
                }
                is CropEditEffect.TriggerLongPressHaptic -> {
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                }
                is CropEditEffect.TriggerSnapHaptic -> {
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                }
                is CropEditEffect.ShowSnackBar -> {
                    snackbarHostState.showSnackbarImmediately(coroutineScope, effect.message)
                }
            }
        }
    }

    // Monitor global ScannerStep change to navigate to results
    LaunchedEffect(sharedState.currentStep) {
        if (sharedState.currentStep == SharedState.ScannerStep.RESULT) {
            onNavigateToResult()
        }
    }

    val bitmap = state.originalImage
    val imageBitmap = remember(bitmap) { bitmap?.asImageBitmap() }
    val frameState = sharedState.detectedFrame

    val finalPointsRef = remember(state.vertexPoints) {
        state.vertexPoints.toMutableList()
    }

    val initialPoints = remember(frameState, bitmap) {
        val points = mutableListOf<androidx.compose.ui.geometry.Offset>()
        val rawPts = frameState?.points ?: floatArrayOf()
        if (bitmap != null && frameState != null) {
            val scaleX = bitmap.width.toFloat() / frameState.imageWidth.toFloat()
            val scaleY = bitmap.height.toFloat() / frameState.imageHeight.toFloat()
            for (i in 0 until 4) {
                val xIndex = i * 2
                val yIndex = i * 2 + 1
                if (xIndex < rawPts.size && yIndex < rawPts.size) {
                    points.add(
                        androidx.compose.ui.geometry.Offset(
                            rawPts[xIndex] * scaleX,
                            rawPts[yIndex] * scaleY
                        )
                    )
                }
            }
        }
        points.toList()
    }

    SScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            STitleTopBar(
                title = "Adjust Borders",
                onBack = { viewModel.onEvent(CropEditEvent.OnBack) }
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
                    onClick = { viewModel.onEvent(CropEditEvent.OnBack) },
                    modifier = Modifier.weight(1f)
                )
                SButton(
                    text = "Complete",
                    type = SButtonType.PRIMARY,
                    onClick = {
                        if (finalPointsRef.size == 4) {
                            val rawPts = FloatArray(8)
                            for (i in 0 until 4) {
                                rawPts[i * 2] = finalPointsRef[i].x
                                rawPts[i * 2 + 1] = finalPointsRef[i].y
                            }
                            sharedViewModel.onEvent(
                                SharedEvent.OnCompleteCrop(
                                    points = rawPts,
                                    selectedFilter = viewModel.state.value.selectedFilter
                                )
                            )
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
                        initialPoints = initialPoints,
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
