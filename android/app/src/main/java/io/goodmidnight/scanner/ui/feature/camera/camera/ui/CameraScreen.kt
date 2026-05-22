package io.goodmidnight.scanner.ui.feature.camera.camera.ui

import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import io.goodmidnight.scanner.R
import io.goodmidnight.scanner.designsystem.component.SBodyMediumText
import io.goodmidnight.scanner.designsystem.component.SLabelMediumText
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.input.pointer.pointerInput
import io.goodmidnight.scanner.ui.core.utils.LocalSnackbarHostState
import io.goodmidnight.scanner.ui.feature.camera.camera.data.CameraState
import io.goodmidnight.scanner.ui.feature.camera.camera.ui.component.CaptureButton
import io.goodmidnight.scanner.ui.feature.camera.camera.ui.component.DocumentOverlay
import io.goodmidnight.scanner.ui.feature.camera.camera.ui.component.SwipeableModeSelector
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    state: CameraState,
    sharedState: SharedState,
    shutterTriggerTime: Long = 0L,
    onBack: () -> Unit,
    onInitCamera: (LifecycleOwner, PreviewView) -> Unit,
    onTakePicture: () -> Unit,
    onChangeDocumentType: (SharedState.DocumentType) -> Unit,
    onChangeProcessingMode: (SharedState.CaptureMode) -> Unit,
    onShutdownCamera: () -> Unit,
    onZoomRatioChanged: (Float) -> Unit,
    onToggleTorch: () -> Unit = {},
    onToggleGridLines: () -> Unit = {},
    onNavigateToSettings: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val docTypes = SharedState.DocumentType.entries
    val docPagerState = rememberPagerState(initialPage = sharedState.documentType.ordinal) {
        docTypes.size
    }

    LaunchedEffect(docPagerState.currentPage) {
        onChangeDocumentType(docTypes[docPagerState.currentPage])
    }

    val procModes = SharedState.CaptureMode.entries
    val procPagerState = rememberPagerState(initialPage = sharedState.captureMode.ordinal) {
        procModes.size
    }

    LaunchedEffect(procPagerState.currentPage) {
        onChangeProcessingMode(procModes[procPagerState.currentPage])
    }

    DisposableEffect(lifecycleOwner) {
        onDispose { onShutdownCamera() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AnimatedContent(
            targetState = sharedState.currentStep,
            transitionSpec = {
                if (targetState.ordinal > initialState.ordinal) {
                    (slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(400)
                    ) + fadeIn(animationSpec = tween(400)))
                        .togetherWith(
                            slideOutHorizontally(
                                targetOffsetX = { -it },
                                animationSpec = tween(400)
                            ) + fadeOut(animationSpec = tween(400))
                        )
                } else {
                    (slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(400)
                    ) + fadeIn(animationSpec = tween(400)))
                        .togetherWith(
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(400)
                            ) + fadeOut(animationSpec = tween(400))
                        )
                }
            },
            label = "CameraStepTransition"
        ) { step ->
            when (step) {
                SharedState.ScannerStep.CAPTURING -> ProcessingScreen()
                else -> CameraPreviewAndControls(
                    state = state,
                    sharedState = sharedState,
                    shutterTriggerTime = shutterTriggerTime,
                    lifecycleOwner = lifecycleOwner,
                    docPagerState = docPagerState,
                    procPagerState = procPagerState,
                    docTypes = docTypes,
                    procModes = procModes,
                    onInitCamera = onInitCamera,
                    onTakePicture = onTakePicture,
                    onZoomRatioChanged = onZoomRatioChanged,
                    onToggleTorch = onToggleTorch,
                    onToggleGridLines = onToggleGridLines,
                    onBack = onBack,
                    onNavigateToSettings = onNavigateToSettings,
                )
            }
        }
    }
}

@Composable
private fun ProcessingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = Theme.colorScheme.primary,
            strokeWidth = 4.dp
        )
        Spacer(modifier = Modifier.height(24.dp))

        SBodyMediumText(
            text = "Analyzing Document...",
            color = Theme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        SLabelMediumText(
            text = "Please hold still while we enhance the image",
            color = Theme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CameraPreviewAndControls(
    state: CameraState,
    sharedState: SharedState,
    shutterTriggerTime: Long,
    lifecycleOwner: LifecycleOwner,
    docPagerState: PagerState,
    procPagerState: PagerState,
    docTypes: List<SharedState.DocumentType>,
    procModes: List<SharedState.CaptureMode>,
    onInitCamera: (LifecycleOwner, PreviewView) -> Unit,
    onTakePicture: () -> Unit,
    onZoomRatioChanged: (Float) -> Unit,
    onToggleTorch: () -> Unit,
    onToggleGridLines: () -> Unit,
    onBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    var flashOpacity by remember { mutableStateOf(0f) }
    LaunchedEffect(shutterTriggerTime) {
        if (shutterTriggerTime > 0L) {
            flashOpacity = 1f
            androidx.compose.animation.core.animate(
                initialValue = 1f,
                targetValue = 0f,
                animationSpec = tween(durationMillis = 200)
            ) { value, _ ->
                flashOpacity = value
            }
        }
    }

    val currentZoomRatio by rememberUpdatedState(state.zoomRatio)
    val currentZoomRatioRange by rememberUpdatedState(state.zoomRatioRange)
    val currentOnZoomRatioChanged by rememberUpdatedState(onZoomRatioChanged)

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        if (zoom != 1.0f) {
                            val newRatio = (currentZoomRatio * zoom).coerceIn(currentZoomRatioRange)
                            currentOnZoomRatioChanged(newRatio)
                        }
                    }
                },
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    onInitCamera(lifecycleOwner, this)
                }
            }
        )

        sharedState.detectedFrame?.let { frame ->
            if (frame.isDetected && frame.points.size == 8) {
                DocumentOverlay(frame = frame)
            }
        }

        if (state.showGridLines) {
            GridOverlay()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = io.goodmidnight.scanner.designsystem.theme.Icons.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onToggleGridLines) {
                        Icon(
                            imageVector = io.goodmidnight.scanner.designsystem.theme.Icons.Grid3x3,
                            contentDescription = "Toggle Grid Lines",
                            tint = if (state.showGridLines) Theme.colorScheme.primary else Color.White
                        )
                    }

                    IconButton(onClick = onToggleTorch) {
                        Icon(
                            imageVector = if (state.isTorchEnabled) io.goodmidnight.scanner.designsystem.theme.Icons.FlashOn else io.goodmidnight.scanner.designsystem.theme.Icons.FlashOff,
                            contentDescription = "Toggle Torch",
                            tint = if (state.isTorchEnabled) Theme.colorScheme.primary else Color.White
                        )
                    }

                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SwipeableModeSelector(
                    pagerState = docPagerState,
                    items = docTypes.map { it.displayName },
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                CaptureButton(onClick = onTakePicture)
                SwipeableModeSelector(
                    pagerState = procPagerState,
                    items = procModes.map { it.displayName },
                    isBold = true,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }

        if (flashOpacity > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = flashOpacity))
            )
        }
    }
}

@Composable
fun GridOverlay(
    modifier: Modifier = Modifier,
    color: Color = Color.White.copy(alpha = 0.5f),
    strokeWidth: Float = 1f,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // 2 vertical lines
        drawLine(
            color = color,
            start = Offset(x = width / 3, y = 0f),
            end = Offset(x = width / 3, y = height),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = color,
            start = Offset(x = 2 * width / 3, y = 0f),
            end = Offset(x = 2 * width / 3, y = height),
            strokeWidth = strokeWidth
        )

        // 2 horizontal lines
        drawLine(
            color = color,
            start = Offset(x = 0f, y = height / 3),
            end = Offset(x = width, y = height / 3),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = color,
            start = Offset(x = 0f, y = 2 * height / 3),
            end = Offset(x = width, y = 2 * height / 3),
            strokeWidth = strokeWidth
        )
    }
}

@Composable
@ComponentPreview
fun SCameraScreenPreview() {
    Theme {
        CompositionLocalProvider(
            LocalSnackbarHostState provides SnackbarHostState(),
        ) {
            CameraScreen(
                state = CameraState(),
                sharedState = SharedState(
                    showGridLines = true,
                    detectedFrame = SharedState.DocumentFrameState(
                        points = floatArrayOf(100f, 100f, 300f, 100f, 300f, 400f, 100f, 400f),
                        isDetected = true,
                        confidence = 0.9f,
                        isStable = true
                    )
                ),
                onBack = {},
                onInitCamera = { _, _ -> },
                onTakePicture = {},
                onChangeDocumentType = {},
                onChangeProcessingMode = {},
                onShutdownCamera = {},
                onZoomRatioChanged = {},
                onNavigateToSettings = {}
            )
        }
    }
}

