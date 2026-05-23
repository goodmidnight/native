package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.softShadow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
    ) { snackbarData ->
        SSnackbarComponent(snackbarData.visuals.message)
    }
}

@Composable
fun SSnackbarComponent(
    message: String,
) {
    val snackbarShape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .padding(16.dp)
            .fillMaxWidth()
            .softShadow(borderRadius = 24.dp, shadowRadius = 20.dp, offsetY = 4.dp)
            .clip(snackbarShape)
            .background(color = Theme.colorScheme.snackbarContainer)
            .padding(
                horizontal = 20.dp,
                vertical = 14.dp
            )
    ) {
        SParagraphLargeText(
            text = message,
            color = Theme.colorScheme.onSnackbarContainer
        )
    }
}

@ComponentPreview
@Composable
fun SSnackbarPreview() {
    Theme {
        SSnackbarComponent(
            message = "Photo has been safely saved to the document library."
        )
    }
}
