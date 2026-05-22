package io.goodmidnight.scanner.data.datasource

import android.graphics.Bitmap
import io.goodmidnight.scanner.model.CaptureResult
import io.goodmidnight.scanner.model.DocumentFrame
import io.goodmidnight.scanner.model.ScannerConfig

interface ScannerDataSource {
    fun initEngine(config: ScannerConfig)
    fun updateConfig(config: ScannerConfig)
    fun detect(previewBitmap: Bitmap, documentType: Int, rotationDegrees: Int): DocumentFrame?
    fun capture(
        srcBitmap: Bitmap,
        frame: DocumentFrame,
        previewWidth: Int,
        previewHeight: Int,
        processingMode: Int,
        documentType: Int,
        rotationDegrees: Int
    ): CaptureResult?
    fun release()
}