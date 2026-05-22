package io.goodmidnight.scanner.data.repository

import android.graphics.Bitmap
import io.goodmidnight.scanner.data.datasource.OcrDataSource
import io.goodmidnight.scanner.domain.repository.OcrRepository
import io.goodmidnight.scanner.model.OcrBlock
import io.goodmidnight.scanner.model.OcrLanguage
import javax.inject.Inject

class DefaultOcrRepository @Inject constructor(
    private val ocr: OcrDataSource,
) : OcrRepository {

    override suspend fun extractText(bitmap: Bitmap, language: OcrLanguage): List<OcrBlock> =
        ocr.extractText(
            bitmap = bitmap,
            language = language,
        )

}
