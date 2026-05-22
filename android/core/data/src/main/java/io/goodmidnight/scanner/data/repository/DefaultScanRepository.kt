package io.goodmidnight.scanner.data.repository

import android.graphics.Bitmap
import io.goodmidnight.scanner.data.datasource.ScannerDataSource
import io.goodmidnight.scanner.model.CaptureResult
import io.goodmidnight.scanner.model.DocumentFrame
import io.goodmidnight.scanner.domain.repository.ScanRepository
import io.goodmidnight.scanner.model.ScannerConfig
import javax.inject.Inject

class DefaultScanRepository @Inject constructor(
    private val scannerDataSource: ScannerDataSource,
) : ScanRepository {

    override fun detectDocument(
        bitmap: Bitmap,
        documentType: Int,
        rotationDegrees: Int,
    ): DocumentFrame? {
        return scannerDataSource.detect(bitmap, documentType, rotationDegrees)
    }

    override fun captureDocument(
        srcBitmap: Bitmap,
        frame: DocumentFrame,
        previewWidth: Int,
        previewHeight: Int,
        processingMode: Int,
        documentType: Int,
        rotationDegrees: Int,
    ): CaptureResult? {
        return scannerDataSource.capture(
            srcBitmap,
            frame,
            previewWidth,
            previewHeight,
            processingMode,
            documentType,
            rotationDegrees
        )
    }
    override fun updateConfig(config: ScannerConfig) {
        scannerDataSource.updateConfig(config)
    }

}
