package io.goodmidnight.scanner.domain.usecase

import android.graphics.Bitmap
import io.goodmidnight.scanner.model.DocumentFrame
import io.goodmidnight.scanner.domain.repository.ScanRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetectDocumentUseCase @Inject constructor(
    private val repository: ScanRepository
) {
    operator fun invoke(bitmap: Bitmap, documentType: Int, rotationDegrees: Int): DocumentFrame? {
        return repository.detectDocument(bitmap, documentType, rotationDegrees)
    }
}