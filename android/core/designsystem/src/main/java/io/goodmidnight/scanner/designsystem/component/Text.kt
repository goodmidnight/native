package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
private fun SBaseText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    style: TextStyle = TextStyle.Default,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    fontWeight: FontWeight? = null,
) {
    val mergedStyle = style.copy(
        color = color,
        textAlign = textAlign ?: style.textAlign,
        textDecoration = textDecoration ?: style.textDecoration,
        fontWeight = fontWeight ?: style.fontWeight
    )
    BasicText(
        text = text,
        modifier = modifier,
        style = mergedStyle,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun SDisplayLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.displayLarge,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SDisplayMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.displayMedium,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SDisplaySmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.displaySmall,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SHeadingXXLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.headingXXLarge,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SHeadingXLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.headingXLarge,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SHeadingLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.headingLarge,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SHeadingMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.headingMedium,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SHeadingSmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.headingSmall,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SHeadingXSmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.headingXSmall,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

// --- Label Styles (controls, buttons, mini metadata) ---

@Composable
fun SLabelLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.labelLarge,
        textAlign = textAlign
    )
}

@Composable
fun SLabelMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.labelMedium,
        textAlign = textAlign
    )
}

@Composable
fun SLabelSmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.labelSmall,
        textAlign = textAlign
    )
}

@Composable
fun SLabelXSmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.labelXSmall
    )
}

// --- Paragraph Styles (body description text) ---

@Composable
fun SParagraphLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.paragraphLarge,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SParagraphMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.paragraphMedium,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
        fontWeight = fontWeight
    )
}

@Composable
fun SParagraphSmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.paragraphSmall,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SParagraphXSmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primaryText,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.paragraphXSmall,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

// --- Legacy wrapper components (backward compatibility) ---

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
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.displaySmall,
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
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.headingXLarge,
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
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.headingMedium,
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
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.headingSmall,
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
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.paragraphLarge,
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
    SBaseText(
        text = text,
        modifier = modifier,
        color = color,
        style = Theme.type.paragraphMedium,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
        fontWeight = fontWeight
    )
}

@Composable
@ComponentPreview
fun STextPreview() {
    Theme {
        Column {
            SDisplayLargeText(text = "Display Large (40sp Bold)")
            SHeadingXXLargeText(text = "Heading XXLarge (24sp Bold)")
            SHeadingMediumText(text = "Heading Medium (18sp SemiBold)")
            SParagraphMediumText(text = "Paragraph Medium (14sp Normal)")
            SLabelMediumText(text = "Label Medium (14sp Medium)")
        }
    }
}