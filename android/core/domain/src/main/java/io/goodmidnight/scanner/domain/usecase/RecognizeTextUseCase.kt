package io.goodmidnight.scanner.domain.usecase

import android.graphics.Bitmap
import io.goodmidnight.scanner.model.OcrBlock
import io.goodmidnight.scanner.model.OcrLanguage
import io.goodmidnight.scanner.domain.repository.OcrRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecognizeTextUseCase @Inject constructor(
    private val repository: OcrRepository
) {
    suspend operator fun invoke(bitmap: Bitmap, language: OcrLanguage): List<OcrBlock> {
        return repository.extractText(bitmap, language)
    }
}