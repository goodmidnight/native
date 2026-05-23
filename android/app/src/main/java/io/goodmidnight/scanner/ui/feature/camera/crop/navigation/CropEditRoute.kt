package io.goodmidnight.scanner.ui.feature.camera.crop.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.goodmidnight.scanner.core.save.DocumentSaveController.SaveFormat
import io.goodmidnight.scanner.ui.core.utils.LocalSnackbarHostState
import io.goodmidnight.scanner.ui.core.utils.showSnackbarImmediately
import io.goodmidnight.scanner.ui.feature.camera.crop.data.CropEditEffect
import io.goodmidnight.scanner.ui.feature.camera.crop.data.CropEditEvent
import io.goodmidnight.scanner.ui.feature.camera.crop.data.CropEditState
import io.goodmidnight.scanner.ui.feature.camera.crop.data.CropEditViewModel
import io.goodmidnight.scanner.ui.feature.camera.crop.ui.CropEditScreen
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedEvent
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedState
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun CropEditRoute(
    modifier: Modifier = Modifier,
    popBackStack: () -> Unit,
    navigateToResult: () -> Unit,
    sharedViewModel: SharedViewModel,
    viewModel: CropEditViewModel = hiltViewModel(),
) {
    val state: CropEditState by viewModel.state.collectAsStateWithLifecycle()
    val sharedState: SharedState by sharedViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    LaunchedEffect(sharedState.rawCapturedBitmap, sharedState.detectedFrame) {
        val bitmap = sharedState.rawCapturedBitmap
        val frame = sharedState.detectedFrame
        if (bitmap != null) {
            viewModel.onEvent(CropEditEvent.OnInitialize(bitmap, frame))
        }
    }

    BackHandler {
        viewModel.onEvent(CropEditEvent.OnBack)
    }

    LaunchedEffect(Unit) {
        viewModel.bindEffect(scope = this) { effect ->
            when (effect) {
                is CropEditEffect.PopBackStack -> {
                    sharedViewModel.onEvent(SharedEvent.OnUpdateStep(SharedState.ScannerStep.PREVIEW))
                    popBackStack()
                }
                is CropEditEffect.NavigateToResult -> {
                    sharedViewModel.onEvent(
                        SharedEvent.OnCompleteCrop(
                            points = effect.points,
                            selectedFilter = effect.filterIndex
                        )
                    )
                }
                is CropEditEffect.TriggerLongPressHaptic -> {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                is CropEditEffect.TriggerSnapHaptic -> {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                is CropEditEffect.ShowSnackBar -> {
                    snackbarHostState.showSnackbarImmediately(coroutineScope, effect.message)
                }
            }
        }
    }

    LaunchedEffect(sharedState.currentStep) {
        if (sharedState.currentStep == SharedState.ScannerStep.RESULT) {
            navigateToResult()
        }
    }

    CropEditScreen(
        modifier = modifier,
        state = state,
        sharedState = sharedState,
        onBack = remember { { viewModel.onEvent(CropEditEvent.OnBack) } },
        onComplete = remember {
            { pts ->
                viewModel.onEvent(CropEditEvent.OnCompleteCrop(pts))
            }
        }
    )
}
