package io.goodmidnight.scanner.model

import android.graphics.Rect

data class OcrBlock(
    val text: String,
    val boundingBox: Rect?
)
