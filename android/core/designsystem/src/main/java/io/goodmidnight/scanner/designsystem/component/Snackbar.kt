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
import androidx.compose.ui.unit.dp
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
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .padding(16.dp)
            .fillMaxWidth()
            .background(
                color = Theme.colorScheme.snackbarContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 16.dp
            )

    ) {
        SBodyLargeText(
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
            message = "스낵바 메시지입니다."
        )
    }
}
