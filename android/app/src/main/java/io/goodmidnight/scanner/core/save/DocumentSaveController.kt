package io.goodmidnight.scanner.core.save

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import io.goodmidnight.scanner.model.SaveFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import javax.inject.Inject

class DocumentSaveController @Inject constructor() {

    suspend fun saveImage(
        bitmap: Bitmap,
        outputStream: OutputStream,
        format: SaveFormat
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val compressFormat = when (format) {
                SaveFormat.PNG -> Bitmap.CompressFormat.PNG
                SaveFormat.JPEG -> Bitmap.CompressFormat.JPEG
                else -> return@withContext false
            }
            bitmap.compress(compressFormat, 100, outputStream)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun saveAsPdf(
        bitmap: Bitmap,
        outputStream: OutputStream,
        documentName: String
    ): Boolean = withContext(Dispatchers.IO) {
        var pdfDocument: PdfDocument? = null
        try {
            pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            
            val canvas = page.canvas
            val paint = Paint().apply {
                isAntiAlias = true
                isFilterBitmap = true
            }
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            pdfDocument.finishPage(page)

            pdfDocument.writeTo(outputStream)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            pdfDocument?.close()
        }
    }
}
