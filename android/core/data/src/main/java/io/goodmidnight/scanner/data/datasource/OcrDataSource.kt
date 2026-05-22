package io.goodmidnight.scanner.data.datasource

import android.graphics.Bitmap
import io.goodmidnight.scanner.model.OcrBlock
import io.goodmidnight.scanner.model.OcrLanguage

/**
 * Data source interface for Optical Character Recognition (OCR).
 * Abstracts the underlying OCR technology (e.g., ML Kit, Tesseract).
 */
interface OcrDataSource {
    /**
     * Extracts text from the given bitmap in the specified language.
     */
    suspend fun extractText(bitmap: Bitmap, language: OcrLanguage): List<OcrBlock>
}
