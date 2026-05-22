package io.goodmidnight.scanner.ui.feature.camera.camera.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.goodmidnight.scanner.designsystem.theme.Theme
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableModeSelector(
    pagerState: PagerState,
    items: List<String>,
    modifier: Modifier = Modifier,
    isBold: Boolean = false,
) {
    val contentPadding = PaddingValues(horizontal = 140.dp)
    val coroutineScope = rememberCoroutineScope()
    val lerp: (Float, Float, Float) -> Float = { start, stop, fraction ->
        start + fraction * (stop - start)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        val isSelected = pagerState.currentPage == page

        Text(
            text = items[page],
            color = if (isSelected) Theme.colorScheme.primary else Color.White,
            fontSize = if (isBold && isSelected) 14.sp else 12.sp,
            fontWeight = if (isBold || isSelected) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .graphicsLayer {
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                            ).absoluteValue
                    alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                }
                .clickable {
                    coroutineScope.launch { pagerState.animateScrollToPage(page) }
                }
        )
    }
}