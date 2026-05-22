package io.goodmidnight.scanner.domain.usecase

import android.graphics.Bitmap
import io.goodmidnight.scanner.model.CaptureResult
import io.goodmidnight.scanner.model.DocumentFrame
import io.goodmidnight.scanner.domain.repository.ScanRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaptureDocumentUseCase @Inject constructor(
    private val repository: ScanRepository
) {
    operator fun invoke(
        srcBitmap: Bitmap,
        frame: DocumentFrame,
        previewWidth: Int,
        previewHeight: Int,
        processingMode: Int,
        documentType: Int,
        rotationDegrees: Int
    ): CaptureResult? {
        return repository.captureDocument(
            srcBitmap, frame, previewWidth, previewHeight, processingMode, documentType, rotationDegrees
        )
    }
}