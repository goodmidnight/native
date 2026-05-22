package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SHeadlineLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.headlineLarge,
        overflow = overflow,
        maxLines = maxLines,
        textAlign = textAlign,
        textDecoration = textDecoration
    )
}

@Composable
fun STitleLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.titleLarge,
        overflow = overflow,
        maxLines = maxLines,
        textAlign = textAlign,
    )
}

@Composable
fun STitleMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.titleMedium,
        overflow = overflow,
        maxLines = maxLines,
        textAlign = textAlign,
    )
}

@Composable
fun STitleSmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.titleSmall,
        overflow = overflow,
        maxLines = maxLines,
        textAlign = textAlign,
    )
}

@Composable
fun SBodyLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.bodyLarge,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
    )
}

@Composable
fun SBodyMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.bodyMedium,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
        fontWeight = fontWeight
    )
}

@Composable
fun SLabelMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.labelMedium
    )
}

@Composable
fun SLabelSmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.labelSmall
    )
}


@Composable
@ComponentPreview
fun STextPreview() {
    Theme {
        Column {
            SHeadlineLargeText(text = "Headline Large (28sp Bold)")
            STitleLargeText(text = "Title Large (22sp Bold)")
            STitleMediumText(text = "Title Medium (18sp SemiBold)")
            STitleSmallText(text = "Title Small (16sp SemiBold)")
            SBodyLargeText(text = "Body Large (16sp Normal)")
            SBodyMediumText(text = "Body Medium (14sp Normal)")
            SLabelMediumText(text = "Label Medium (12sp Medium)")
            SLabelSmallText(text = "Label Small (10sp Medium)")
        }
    }
}