package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Image(
        imageVector = imageVector,
        contentDescription = contentDescription,
        colorFilter = ColorFilter.tint(tint),
        modifier = modifier
    )
}
