package io.goodmidnight.scanner.ui.feature.camera.shared

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import io.goodmidnight.scanner.ui.core.viewmodel.BaseEvent

sealed interface SharedEvent : BaseEvent {
    data class OnInitCamera(
        val lifecycleOwner: LifecycleOwner,
        val previewView: PreviewView,
    ) : SharedEvent
    data object OnTakePicture : SharedEvent
    data class OnChangeDocumentType(val documentType: SharedState.DocumentType) : SharedEvent
    data class OnChangeProcessingMode(val processingMode: SharedState.CaptureMode) : SharedEvent
    data class OnUpdateStep(val step: SharedState.ScannerStep) : SharedEvent
    object OnShutdownCamera : SharedEvent
    data class OnCompleteCrop(val points: FloatArray, val selectedFilter: Int) : SharedEvent
}
