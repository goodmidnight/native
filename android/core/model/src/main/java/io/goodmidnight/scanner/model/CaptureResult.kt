package io.goodmidnight.scanner.model

import android.graphics.Bitmap

data class CaptureResult(
    val image: Bitmap?,
    val status: Int,
    val isBlurry: Boolean,
    val hasGlare: Boolean,
    val message: String
)