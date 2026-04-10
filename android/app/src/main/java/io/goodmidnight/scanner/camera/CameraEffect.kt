package io.goodmidnight.scanner.camera

import android.graphics.Bitmap

/**
 * Sealed interface representing one-time side effects emitted by the CameraController.
 * These are typically consumed by the UI layer to show previews, navigate, or handle errors.
 */
sealed interface CameraEffect {
    /**
     * Emitted periodically during image analysis for real-time preview or processing.
     * @param bitmap The analyzed frame converted to a Bitmap.
     * @param rotation The rotation degrees of the image.
     */
    data class SendPreviewFrame(
        val bitmap: Bitmap,
        val rotation: Int,
    ) : CameraEffect

    /**
     * Emitted when a high-quality photo has been successfully captured.
     * @param bitmap The captured high-resolution image.
     * @param rotation The rotation degrees of the captured image.
     * @param processingMode The mode used for post-processing (e.g., cropping, enhancement).
     */
    data class SendCapturedImage(
        val bitmap: Bitmap,
        val rotation: Int,
        val processingMode: Int,
    ) : CameraEffect

    /**
     * Emitted when an error occurs during camera initialization or operation.
     * @param exception The exception that triggered the error.
     */
    data class SendCameraError(
        val exception: Exception,
    ) : CameraEffect
}
