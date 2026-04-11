package io.goodmidnight.scanner.data.jni

import android.graphics.Bitmap
import io.goodmidnight.scanner.domain.model.CaptureResult
import io.goodmidnight.scanner.domain.model.DocumentFrame
import io.goodmidnight.scanner.domain.model.ScannerConfig

/**
 * JNI Wrapper class for the native C++ Scanner Engine.
 * This class manages the lifecycle of the native engine and provides an interface
 * for document detection and high-quality capture using OpenCV.
 */
class NativeScanner {
    /**
     * Pointer to the native C++ object (ScannerEngine).
     * This address is managed by the native layer and should not be modified manually.
     */
    private var nativePtr: Long = 0

    init {
        // Load the shared library "libnative-scanner.so" compiled via CMake.
        System.loadLibrary("native-scanner")
    }

    /**
     * Initializes the native engine with the given configuration.
     * @param config Configuration parameters like target width and algorithm thresholds.
     */
    fun initEngine(config: ScannerConfig) {
        if (nativePtr == 0L) {
            nativePtr = nativeInit(config)
        }
    }

    /**
     * Updates the configuration of the already initialized native engine.
     * @param config New configuration parameters to be applied.
     */
    fun updateConfig(config: ScannerConfig) {
        if (nativePtr != 0L) nativeUpdateConfig(nativePtr, config)
    }

    /**
     * Performs real-time document detection on a preview frame.
     * @param previewBitmap The bitmap of the current camera preview frame.
     * @param documentType Type of document to detect (e.g., ID Card, Document).
     * @param rotationDegrees Rotation of the bitmap in degrees.
     * @return [DocumentFrame] containing the detected corners and confidence, or null if engine not ready.
     */
    fun detect(previewBitmap: Bitmap, documentType: Int, rotationDegrees: Int): DocumentFrame? {
        if (nativePtr == 0L) return null
        return nativeDetect(nativePtr, previewBitmap, documentType, rotationDegrees)
    }

    /**
     * Performs a high-quality capture and processing based on a detected frame.
     * @param srcBitmap The high-resolution source bitmap.
     * @param frame The [DocumentFrame] previously detected on the preview.
     * @param previewWidth Width of the preview view used for coordinate mapping.
     * @param previewHeight Height of the preview view used for coordinate mapping.
     * @param processingMode Enhancement mode (e.g., original, color enhancement, grayscale).
     * @param documentType Type of document.
     * @param rotationDegrees Rotation of the source bitmap.
     * @return [CaptureResult] containing the warped image and status flags.
     */
    fun capture(
        srcBitmap: Bitmap,
        frame: DocumentFrame,
        previewWidth: Int,
        previewHeight: Int,
        processingMode: Int,
        documentType: Int,
        rotationDegrees: Int
    ): CaptureResult? {
        if (nativePtr == 0L) return null
        return nativeCapture(nativePtr, srcBitmap, frame, previewWidth, previewHeight, processingMode, documentType, rotationDegrees)
    }

    /**
     * Releases the native engine and frees memory on the C++ heap.
     * Should be called when the scanner is no longer needed to prevent memory leaks.
     */
    fun release() {
        if (nativePtr != 0L) {
            nativeRelease(nativePtr)
            nativePtr = 0L
        }
    }

    // --- JNI External Methods (implemented in C++) ---
    private external fun nativeInit(config: ScannerConfig): Long
    private external fun nativeUpdateConfig(ptr: Long, config: ScannerConfig)
    private external fun nativeDetect(ptr: Long, bitmap: Bitmap, type: Int, rotation: Int): DocumentFrame
    private external fun nativeCapture(ptr: Long, bitmap: Bitmap, frame: DocumentFrame, pWidth: Int, pHeight: Int, mode: Int, type: Int, rotation: Int): CaptureResult
    private external fun nativeRelease(ptr: Long)
}
