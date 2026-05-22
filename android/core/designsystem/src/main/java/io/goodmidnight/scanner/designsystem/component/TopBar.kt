package io.goodmidnight.scanner.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.goodmidnight.scanner.designsystem.modifier.noRippleClickable
import io.goodmidnight.scanner.designsystem.preview.ComponentPreview
import io.goodmidnight.scanner.designsystem.theme.Icons
import io.goodmidnight.scanner.designsystem.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun STitleTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Column {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                STitleLargeText(
                    title,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            },
            navigationIcon = {
                onBack?.let {
                    Row(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.ArrowBackIosNew,
                            contentDescription = "${Icons.ArrowBackIosNew}",
                            modifier = Modifier
                                .size(24.dp)
                                .noRippleClickable { it() },
                        )
                    }
                }
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Theme.colorScheme.background,
                scrolledContainerColor = Theme.colorScheme.background,
                navigationIconContentColor = Theme.colorScheme.primaryIcon,
                titleContentColor = Theme.colorScheme.primaryIcon,
                actionIconContentColor = Theme.colorScheme.primaryIcon
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SMainTopBar(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = {
            STitleLargeText(title)
        },
        navigationIcon = {},
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Theme.colorScheme.background,
            scrolledContainerColor = Theme.colorScheme.background,
            navigationIconContentColor = Theme.colorScheme.primaryIcon,
            titleContentColor = Theme.colorScheme.primaryIcon,
            actionIconContentColor = Theme.colorScheme.primaryIcon
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SSearchTopBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    onBack: (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            SSearchTextField(
                value = value,
                onValueChange = onValueChange,
                onSearch = onSearch,
                focusRequester = focusRequester
            )
        },
        navigationIcon = {
            onBack?.let {
                Row(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.ArrowBackIosNew,
                        contentDescription = "${Icons.ArrowBackIosNew}",
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable { it() },
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Theme.colorScheme.background,
            scrolledContainerColor = Theme.colorScheme.background,
            navigationIconContentColor = Theme.colorScheme.primaryIcon,
            titleContentColor = Theme.colorScheme.primaryIcon,
            actionIconContentColor = Theme.colorScheme.primaryIcon
        ),
    )
}


@Composable
@ComponentPreview
fun STitleTopBarPreview() {
    Theme {
        STitleTopBar("타이틀", onBack = {})
    }
}

@Composable
@ComponentPreview
fun SMainTopBarPreview() {
    Theme {
        SMainTopBar("타이틀")
    }
}


@Composable
@ComponentPreview
fun SSearchTopBarPreview() {
    Theme {
        SSearchTopBar(
            value = "",
            onSearch = {},
            onValueChange = {},
        )
    }
}