package io.goodmidnight.scanner.domain.usecase

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClearCacheUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        try {
            val cacheDir = context.cacheDir
            val tempScanDir = File(context.filesDir, "temp_scans")
            
            cacheDir?.deleteRecursively()
            if (tempScanDir.exists()) {
                tempScanDir.deleteRecursively()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
