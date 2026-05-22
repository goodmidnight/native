package io.goodmidnight.scanner.ml

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import io.goodmidnight.scanner.data.datasource.OcrDataSource
import io.goodmidnight.scanner.model.OcrBlock
import io.goodmidnight.scanner.model.OcrLanguage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class MlKitOcrDataSource @Inject constructor() : OcrDataSource {

    private val textRecognizer by lazy {
        TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    }

    override suspend fun extractText(bitmap: Bitmap, language: OcrLanguage): List<OcrBlock> {
        return try {
            val image = InputImage.fromBitmap(bitmap, 0)

            val result = textRecognizer.process(image).await()

            result.textBlocks.flatMap { block ->
                block.lines.map { line ->
                    OcrBlock(
                        text = line.text,
                        boundingBox = line.boundingBox
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}