package io.goodmidnight.scanner.camera

/**
 * Data class representing the current UI state of the camera.
 * This state is observed by the UI to update its appearance (e.g., showing loading spinners).
 */
data class CameraState(
    /**
     * True if the CameraProvider has been successfully initialized and use cases are bound.
     */
    val isCameraReady: Boolean = false,

    /**
     * True if the camera is currently in the process of capturing a high-resolution image.
     * Can be used to disable the shutter button or show a capturing overlay.
     */
    val isCapturing: Boolean = false
)
