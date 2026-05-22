package io.goodmidnight.scanner.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Alpha
import io.goodmidnight.scanner.designsystem.theme.Icons
import io.goodmidnight.scanner.designsystem.theme.Theme

@Composable
fun SUnderlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    placeHolder: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    val textSelectionColors = TextSelectionColors(
        handleColor = Theme.colorScheme.accent,
        backgroundColor = Theme.colorScheme.accent.copy(Alpha.LOW)
    )
    val textColor: Color by animateColorAsState(
        if (enabled) Theme.colorScheme.primaryText
        else Theme.colorScheme.disabledText, label = "textColor"
    )
    val outlineColor: Color by animateColorAsState(
        if (enabled) Theme.colorScheme.outline
        else Theme.colorScheme.outlineVariant, label = "outlineColor"
    )


    CompositionLocalProvider(
        LocalTextSelectionColors provides textSelectionColors
    ) {
        BasicTextField(
            modifier = modifier.then(
                focusRequester?.let {
                    Modifier.focusRequester(it)
                } ?: Modifier
            ),
            value = value,
            singleLine = singleLine,
            interactionSource = interactionSource,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            enabled = enabled,
            textStyle = Theme.type.bodyMedium.copy(color = textColor),
            cursorBrush = SolidColor(Theme.colorScheme.accent),
            decorationBox = { text ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                if (value.isEmpty()) {
                                    SBodyMediumText(
                                        text = placeHolder ?: "",
                                        color = Theme.colorScheme.disabledText,
                                    )
                                }
                                text()
                            }
                        }

                        if (value.isNotEmpty()) {
                            IconButton(onClick = { onValueChange("") }) {
                                Icon(
                                    imageVector = Icons.Clear,
                                    contentDescription = "Clear text",
                                    tint = Theme.colorScheme.secondaryIcon
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = outlineColor,
                        thickness = 1.dp
                    )
                }
            }
        )
    }
}

@Composable
fun SContainerTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    placeHolder: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    val textSelectionColors = TextSelectionColors(
        handleColor = Theme.colorScheme.accent,
        backgroundColor = Theme.colorScheme.accent.copy(Alpha.LOW)
    )
    val textColor: Color by animateColorAsState(
        if (enabled) Theme.colorScheme.primaryText
        else Theme.colorScheme.disabledText, label = "textColor"
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides textSelectionColors
    ) {
        BasicTextField(
            modifier = modifier.then(
                focusRequester?.let {
                    Modifier.focusRequester(it)
                } ?: Modifier
            ),
            value = value,
            singleLine = singleLine,
            interactionSource = interactionSource,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            enabled = enabled,
            textStyle = Theme.type.bodyMedium.copy(color = textColor),
            cursorBrush = SolidColor(Theme.colorScheme.accent),
            decorationBox = { text ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .background(
                            color = Theme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            if (value.isEmpty()) {
                                SBodyMediumText(
                                    text = placeHolder ?: "",
                                    color = Theme.colorScheme.secondaryText,
                                )
                            }
                            text()
                        }
                    }

                    if (value.isNotEmpty()) {
                        IconButton(onClick = { onValueChange("") }) {
                            Icon(
                                imageVector = Icons.Clear,
                                contentDescription = "Clear text",
                                tint = Theme.colorScheme.secondaryIcon
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SSearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    placeHolder: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    val textSelectionColors = TextSelectionColors(
        handleColor = Theme.colorScheme.accent,
        backgroundColor = Theme.colorScheme.accent.copy(Alpha.LOW)
    )
    val textColor: Color by animateColorAsState(
        if (enabled) Theme.colorScheme.primaryText
        else Theme.colorScheme.disabledText, label = "textColor"
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides textSelectionColors
    ) {
        BasicTextField(
            modifier = modifier.then(
                focusRequester?.let {
                    Modifier.focusRequester(it)
                } ?: Modifier
            ),
            value = value,
            singleLine = singleLine,
            interactionSource = interactionSource,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch(value) }
            ),
            enabled = enabled,
            textStyle = Theme.type.bodyMedium.copy(color = textColor),
            cursorBrush = SolidColor(Theme.colorScheme.accent),
            decorationBox = { text ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Theme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(start = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            if (value.isEmpty()) {
                                SBodyMediumText(
                                    text = placeHolder ?: "",
                                    color = Theme.colorScheme.secondaryText,
                                )
                            }
                            text()
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AnimatedVisibility(
                            visible = value.isNotEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            IconButton(onClick = { onValueChange("") }) {
                                Icon(
                                    imageVector = Icons.Clear,
                                    contentDescription = "Clear text",
                                    tint = Theme.colorScheme.secondaryIcon
                                )
                            }
                        }
                        IconButton(onClick = { onSearch(value) }) {
                            Icon(
                                imageVector = Icons.Search,
                                contentDescription = "Search",
                                tint = Theme.colorScheme.primaryIcon
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
@ComponentPreview
fun STextFieldPreview() {
    var value by remember { mutableStateOf("") }
    Theme {
        Column(
            modifier = Modifier
                .background(Theme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SUnderlineTextField(
                value = value,
                onValueChange = { value = it },
                placeHolder = "Underline text field"
            )
            SContainerTextField(
                value = value,
                onValueChange = { value = it },
                placeHolder = "Container text field"
            )
            SSearchTextField(
                value = value,
                onValueChange = { value = it },
                onSearch = {},
                placeHolder = "Search text field"
            )
        }
    }
}