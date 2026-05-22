package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        containerColor = Theme.colorScheme.surface,
        contentColor = Theme.colorScheme.onSurface,
        scrimColor = Theme.colorScheme.scrim,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Theme.colorScheme.outline
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
                .padding(horizontal = 24.dp),
            content = content
        )
    }
}

@Composable
fun SBottomSheetHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        STitleMediumText(
            text = title,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        HorizontalDivider(color = Theme.colorScheme.outlineVariant)
    }
}

@Composable
fun SBottomSheetItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null
) {
    Surface(
        onClick = onClick,
        color = androidx.compose.ui.graphics.Color.Transparent,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(12.dp))
            }
            SBodyLargeText(text = text)
        }
    }
}
