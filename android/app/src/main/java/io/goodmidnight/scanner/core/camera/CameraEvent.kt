package io.goodmidnight.scanner.core.camera

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

/**
 * Sealed interface representing actions that can be triggered in the camera system.
 * These events are sent from the UI/ViewModel to the CameraController for processing.
 */
sealed interface CameraEvent {
    /**
     * Triggered to initialize and bind camera use cases to the provided lifecycle.
     * @param lifecycleOwner The lifecycle owner (Activity/Fragment) to bind the camera to.
     * @param previewView The View where the camera preview will be rendered.
     */
    data class StartCamera(
        val lifecycleOwner: LifecycleOwner,
        val previewView: PreviewView
    ) : CameraEvent

    /**
     * Triggered to capture a high-resolution image.
     * @param processingMode Integer flag indicating how the captured image should be processed.
     */
    data class TakePicture(val processingMode: Int) : CameraEvent

    /**
     * Triggered to unbind all camera use cases and release resources.
     */
    object Shutdown : CameraEvent
}