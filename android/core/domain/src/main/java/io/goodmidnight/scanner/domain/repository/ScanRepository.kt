package io.goodmidnight.scanner.domain.repository

import android.graphics.Bitmap
import io.goodmidnight.scanner.model.CaptureResult
import io.goodmidnight.scanner.model.DocumentFrame
import io.goodmidnight.scanner.model.ScannerConfig

interface ScanRepository {
    fun detectDocument(bitmap: Bitmap, documentType: Int, rotationDegrees: Int): DocumentFrame?

    fun captureDocument(
        srcBitmap: Bitmap,
        frame: DocumentFrame,
        previewWidth: Int,
        previewHeight: Int,
        processingMode: Int,
        documentType: Int,
        rotationDegrees: Int,
    ): CaptureResult?

    fun updateConfig(config: ScannerConfig)
}