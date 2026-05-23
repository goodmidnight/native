package io.goodmidnight.scanner.ui.feature.camera.shared

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.goodmidnight.scanner.core.camera.CameraController
import io.goodmidnight.scanner.core.camera.CameraEffect
import io.goodmidnight.scanner.core.camera.CameraEvent.Shutdown
import io.goodmidnight.scanner.core.camera.CameraEvent.StartCamera
import io.goodmidnight.scanner.core.camera.CameraEvent.TakePicture
import io.goodmidnight.scanner.domain.repository.SettingsRepository
import io.goodmidnight.scanner.domain.usecase.CaptureDocumentUseCase
import io.goodmidnight.scanner.domain.usecase.DetectDocumentUseCase
import io.goodmidnight.scanner.domain.usecase.RecognizeTextUseCase
import io.goodmidnight.scanner.domain.usecase.SaveImageToGalleryUseCase
import io.goodmidnight.scanner.domain.usecase.UpdateScannerConfigUseCase
import io.goodmidnight.scanner.model.DocumentFrame
import io.goodmidnight.scanner.model.ImageQuality
import io.goodmidnight.scanner.model.OcrBlock
import io.goodmidnight.scanner.ui.core.exception.AppError
import io.goodmidnight.scanner.ui.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val cameraController: CameraController,
    private val detectDocumentUseCase: DetectDocumentUseCase,
    private val captureDocumentUseCase: CaptureDocumentUseCase,
    private val recognizeTextUseCase: RecognizeTextUseCase,
    private val settingsRepository: SettingsRepository,
    private val saveImageToGalleryUseCase: SaveImageToGalleryUseCase,
    private val updateScannerConfigUseCase: UpdateScannerConfigUseCase,
) : BaseViewModel<SharedState, SharedEvent, SharedEffect, AppError>(
    SharedState()
) {

    init {
        bindEvent { event ->
            when (event) {
                is SharedEvent.OnInitCamera -> {
                    cameraController.processEvent(
                        StartCamera(event.lifecycleOwner, event.previewView)
                    )
                }

                is SharedEvent.OnTakePicture -> {
                    if (state.value.currentStep == SharedState.ScannerStep.PREVIEW) {
                        updateState { copy(currentStep = SharedState.ScannerStep.CAPTURING) }
                        cameraController.processEvent(
                            TakePicture(state.value.captureMode.code)
                        )
                    }
                }

                is SharedEvent.OnChangeDocumentType -> {
                    updateState { copy(documentType = event.documentType) }
                }

                is SharedEvent.OnShutdownCamera -> {
                    cameraController.processEvent(Shutdown)
                }

                is SharedEvent.OnUpdateStep -> updateState {
                    copy(
                        currentStep = event.step
                    )
                }

                is SharedEvent.OnChangeProcessingMode -> updateState {
                    copy(
                        captureMode = event.processingMode
                    )
                }

                is SharedEvent.OnCompleteCrop -> {
                    viewModelScope.launch {
                        processCompleteCrop(event.points, event.selectedFilter)
                    }
                }
            }
        }
        bindError { error ->
            error.handleError()
        }
        observeCameraEffects()
        observeSettings()
    }

    private fun observeSettings() {
        settingsRepository.getSettings()
            .onEach { settings ->
                updateState {
                    copy(
                        imageQuality = ImageQuality.valueOf(settings.imageQuality.name),
                        autoSaveToGallery = settings.autoSaveToGallery,
                        showGridLines = settings.showGridLines
                    )
                }
            }.launchIn(viewModelScope)

        state.map { it.imageQuality }
            .distinctUntilChanged()
            .onEach { imageQuality ->
                updateScannerConfigUseCase(imageQuality)
            }.launchIn(viewModelScope)
    }

    private fun observeCameraEffects() = viewModelScope.launch {
        cameraController.cameraSideEffect.collect { effect ->
            when (effect) {
                is CameraEffect.SendPreviewFrame -> detectFrame(effect.bitmap, effect.rotation)
                is CameraEffect.SendCapturedImage -> onImageCaptured(
                    effect.bitmap,
                    effect.rotation,
                    effect.processingMode
                )

                is CameraEffect.SendCameraError -> {
                    emitError(AppError.of(effect.exception))
                }
            }
        }
    }

    private fun onImageCaptured(
        bitmap: Bitmap,
        rotation: Int,
        processingMode: Int,
    ) {
        updateState {
            copy(
                rawCapturedBitmap = bitmap,
                rawCapturedRotation = rotation,
                rawCapturedProcessingMode = processingMode,
                currentStep = SharedState.ScannerStep.CROP_EDIT
            )
        }
    }

    private fun detectFrame(bitmap: Bitmap, rotation: Int) {
        val frame = detectDocumentUseCase(
            bitmap = bitmap,
            documentType = state.value.documentType.code,
            rotationDegrees = rotation
        )

        val isRotated = rotation == 90 || rotation == 270
        val actualImageWidth = if (isRotated) bitmap.height else bitmap.width
        val actualImageHeight = if (isRotated) bitmap.width else bitmap.height

        updateState {
            copy(
                detectedFrame = frame?.let {
                    SharedState.DocumentFrameState(
                        points = it.points,
                        isDetected = it.isDetected,
                        confidence = it.confidence,
                        isStable = it.isStable,
                        imageWidth = actualImageWidth,
                        imageHeight = actualImageHeight
                    )
                }
            )
        }
    }

    private suspend fun processCompleteCrop(
        editedPoints: FloatArray,
        selectedFilterIndex: Int,
    ) {
        val bitmap = state.value.rawCapturedBitmap ?: return
        val rotation = state.value.rawCapturedRotation
        val lastKnownFrameState = state.value.detectedFrame ?: return
        if (state.value.currentStep == SharedState.ScannerStep.RESULT) return

        updateState { copy(currentStep = SharedState.ScannerStep.CAPTURING) }

        val domainFrame = DocumentFrame(
            points = editedPoints,
            isDetected = true,
            confidence = 1.0f,
            isStable = true
        )

        val result = captureDocumentUseCase(
            srcBitmap = bitmap,
            frame = domainFrame,
            previewWidth = lastKnownFrameState.imageWidth,
            previewHeight = lastKnownFrameState.imageHeight,
            processingMode = selectedFilterIndex,
            documentType = state.value.documentType.code,
            rotationDegrees = rotation
        )

        if (result?.status == 0 && result.image != null) {
            var extractedBlocks: List<OcrBlock> = emptyList()
            var extractedFullText = ""

            if (state.value.captureMode == SharedState.CaptureMode.OCR) {
                extractedBlocks = recognizeTextUseCase(result.image!!, state.value.ocrLanguage)
                extractedFullText = extractedBlocks.joinToString("\n") { it.text }
            }

            if (state.value.autoSaveToGallery) {
                try {
                    saveImageToGalleryUseCase(result.image!!)
                    emitEffect(SharedEffect.ShowSnackBar("Image saved to gallery"))
                } catch (e: Exception) {
                    Log.e("SharedViewModel", "Failed to save image to gallery", e)
                    emitEffect(SharedEffect.ShowSnackBar("Failed to save image"))
                }
            }

            updateState {
                copy(
                    captureResult = SharedState.CaptureResultState(
                        model = result,
                        ocrBlocks = extractedBlocks,
                        fullText = extractedFullText
                    ),
                    currentStep = SharedState.ScannerStep.RESULT
                )
            }
            when {
                result.isBlurry -> emitEffect(SharedEffect.ShowSnackBar("The image is slightly out of focus. Please check if the text is clear."))
                result.hasGlare -> emitEffect(SharedEffect.ShowSnackBar("Glare detected. Some text might be unreadable."))
            }
        } else {
            updateState { copy(currentStep = SharedState.ScannerStep.CROP_EDIT) }

            val errorMessage = when {
                result?.isBlurry == true -> "The image is too blurry. Please hold the device steady and try again."
                result?.hasGlare == true -> "Severe glare detected. Please adjust the angle to avoid direct light."
                else -> result?.message ?: "Failed to process the document. Please try again."
            }
            emitEffect(SharedEffect.ShowSnackBar(errorMessage))
        }
    }

    override fun onCleared() {
        super.onCleared()
        cameraController.processEvent(Shutdown)
    }
}
