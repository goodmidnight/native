package io.goodmidnight.scanner.ui.feature.camera.camera.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.goodmidnight.scanner.ui.core.utils.LocalSnackbarHostState
import io.goodmidnight.scanner.ui.core.utils.showSnackbarImmediately
import io.goodmidnight.scanner.ui.feature.camera.camera.data.CameraEffect
import io.goodmidnight.scanner.ui.feature.camera.camera.data.CameraEvent
import io.goodmidnight.scanner.ui.feature.camera.camera.data.CameraState
import io.goodmidnight.scanner.ui.feature.camera.camera.data.CameraViewModel
import io.goodmidnight.scanner.ui.feature.camera.camera.ui.CameraScreen
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedEffect
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedEvent
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedState
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun CameraRoute(
    modifier: Modifier = Modifier,
    popBackStack: () -> Unit,
    navigateToResult: () -> Unit,
    navigateToSettings: () -> Unit,
    sharedViewModel: SharedViewModel,
    viewModel: CameraViewModel = hiltViewModel(),
) {
    val state: CameraState by viewModel.state.collectAsStateWithLifecycle()
    val sharedState: SharedState by sharedViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    var shutterTriggerTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(sharedState.currentStep) {
        if (sharedState.currentStep == SharedState.ScannerStep.CAPTURING) {
            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.bindEffect(scope = this) { effect ->
            when (effect) {
                is CameraEffect.ShowSnackBar -> snackbarHostState.showSnackbarImmediately(
                    coroutineScope,
                    effect.message
                )

                CameraEffect.PopBackStack -> popBackStack()
                CameraEffect.NativeToResult -> navigateToResult()
                CameraEffect.TriggerShutterFeedback -> {
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    shutterTriggerTime = System.currentTimeMillis()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        sharedViewModel.bindEffect(scope = this) { effect ->
            when (effect) {
                is SharedEffect.ShowSnackBar -> snackbarHostState.showSnackbarImmediately(
                    coroutineScope,
                    effect.message
                )
            }
        }
    }

    LaunchedEffect(sharedState.currentStep) {
        if (sharedState.currentStep == SharedState.ScannerStep.RESULT) {
            viewModel.onEvent(CameraEvent.OnNavigateToResult)
        }
    }

    BackHandler(onBack = remember { { viewModel.onEvent(CameraEvent.OnBack) } })

    CameraScreen(
        modifier = modifier,
        state = state,
        sharedState = sharedState,
        shutterTriggerTime = shutterTriggerTime,
        onInitCamera = remember {
            { lifecycleOwner, previewView ->
                sharedViewModel.onEvent(SharedEvent.OnInitCamera(lifecycleOwner, previewView))
            }
        },
        onTakePicture = remember {
            {
                sharedViewModel.onEvent(SharedEvent.OnTakePicture)
            }
        },
        onChangeDocumentType = remember {
            { documentType ->
                sharedViewModel.onEvent(SharedEvent.OnChangeDocumentType(documentType))
            }
        },
        onShutdownCamera = remember {
            {
                sharedViewModel.onEvent(SharedEvent.OnShutdownCamera)
            }
        },
        onChangeProcessingMode = remember {
            { processingMode ->
                sharedViewModel.onEvent(SharedEvent.OnChangeProcessingMode(processingMode))
            }
        },
        onZoomRatioChanged = remember {
            { zoomRatio ->
                viewModel.onEvent(CameraEvent.OnZoomRatioChanged(zoomRatio))
            }
        },
        onToggleTorch = remember { { viewModel.onEvent(CameraEvent.OnToggleTorch) } },
        onToggleGridLines = remember { { viewModel.onEvent(CameraEvent.OnToggleGridLines) } },
        onNavigateToSettings = navigateToSettings
    )
}
