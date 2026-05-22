package io.goodmidnight.scanner.domain.repository

import android.graphics.Bitmap
import io.goodmidnight.scanner.model.OcrBlock
import io.goodmidnight.scanner.model.OcrLanguage

interface OcrRepository {
    suspend fun extractText(bitmap: Bitmap, language: OcrLanguage): List<OcrBlock>
}