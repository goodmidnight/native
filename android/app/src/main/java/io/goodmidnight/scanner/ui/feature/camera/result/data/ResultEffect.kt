package io.goodmidnight.scanner.ui.feature.camera.result.data

import android.graphics.Bitmap
import io.goodmidnight.scanner.core.save.DocumentSaveController
import io.goodmidnight.scanner.ui.core.viewmodel.BaseEffect


sealed interface ResultEffect : BaseEffect {
    data class ShowSnackBar(val message: String) : ResultEffect
    data object PopBackStack : ResultEffect
    data class ShareImage(val bitmap: Bitmap) : ResultEffect
    data class SaveToFolder(val bitmap: Bitmap, val format: DocumentSaveController.SaveFormat) : ResultEffect
}
