package io.goodmidnight.scanner.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

enum class ColorTheme {
    SYSTEM, LIGHT, DARK
}


@Composable
fun Theme(
    theme: ColorTheme = ColorTheme.SYSTEM,
    content: @Composable (() -> Unit),
) {
    val useDarkTheme = when (theme) {
        ColorTheme.LIGHT -> false
        ColorTheme.DARK -> true
        ColorTheme.SYSTEM -> isSystemInDarkTheme()
    }

    CompositionLocalProvider(
        LocalColorScheme provides if (useDarkTheme) darkColorScheme else lightColorScheme,
        LocalType provides Type(),
        content = content
    )
}

object Theme {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val type: Type
        @Composable
        @ReadOnlyComposable
        get() = LocalType.current
}

val LocalColorScheme =
    staticCompositionLocalOf<ColorScheme> { error("CompositionLocal ColorScheme not present") }
val LocalType = staticCompositionLocalOf<Type> { error("CompositionLocal Type not present") }
