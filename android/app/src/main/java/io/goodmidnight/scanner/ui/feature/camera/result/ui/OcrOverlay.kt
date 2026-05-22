package io.goodmidnight.scanner.ui.feature.camera.result.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.goodmidnight.scanner.designsystem.theme.Theme
import io.goodmidnight.scanner.model.OcrBlock

@Composable
fun OcrOverlay(
    bitmapWidth: Int,
    bitmapHeight: Int,
    blocks: List<OcrBlock>
) {
    val textMeasurer = rememberTextMeasurer()
    val color = Theme.colorScheme.primary

    Canvas(modifier = Modifier.fillMaxSize()) {
        val scale = minOf(size.width / bitmapWidth, size.height / bitmapHeight)
        val offsetX = (size.width - (bitmapWidth * scale)) / 2f
        val offsetY = (size.height - (bitmapHeight * scale)) / 2f

        blocks.forEach { block ->
            block.boundingBox?.let { rect ->
                drawRect(
                    color = color,
                    topLeft = Offset(rect.left * scale + offsetX, rect.top * scale + offsetY),
                    size = Size(rect.width() * scale, rect.height() * scale),
                    style = Stroke(width = 2.dp.toPx())
                )

                drawText(
                    text = block.text,
                    textMeasurer = textMeasurer,
                    topLeft = Offset(rect.left * scale + offsetX, rect.top * scale + offsetY),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.White
                    )
                )
            }
        }
    }
}