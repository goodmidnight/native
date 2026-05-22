package io.goodmidnight.scanner.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveImageToGalleryUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    @Throws(IOException::class)
    operator fun invoke(bitmap: Bitmap, displayName: String = "scan_${System.currentTimeMillis()}") {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/NativeScanner")
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw IOException("Failed to create new MediaStore record.")

        resolver.openOutputStream(uri).use { stream ->
            if (stream == null) {
                throw IOException("Failed to open output stream.")
            }
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                throw IOException("Failed to save bitmap.")
            }
        }
    }
}
