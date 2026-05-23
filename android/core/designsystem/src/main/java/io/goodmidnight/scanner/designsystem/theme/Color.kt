package io.goodmidnight.scanner.designsystem.theme

import androidx.compose.ui.graphics.Color
import io.goodmidnight.scanner.designsystem.theme.Alpha.LOW

val Light_Background = Color(0xFFF8F9FA)
val Light_Surface = Color(0xFFFFFFFF)
val Light_PrimaryText = Color(0xFF212529)
val Light_SecondaryText = Color(0xFF6C757D)
val Light_Border = Color(0xFFE9ECEF)
val Light_BorderVariant = Color(0xFFF1F3F5)
val Light_DisabledText = Color(0xFFC1C7CD)
val Light_Success = Color(0xFF198038)
val Light_Error = Color(0xFFDA1414)

// Dark
val Dark_Background = Color(0xFF121212)
val Dark_Surface = Color(0xFF1E1E1E)
val Dark_PrimaryText = Color(0xFFF8F9FA)
val Dark_SecondaryText = Color(0xFFA0A5AB)
val Dark_Border = Color(0xFF2C2C2E)
val Dark_BorderVariant = Color(0xFF1C1C1E)
val Dark_DisabledText = Color(0xFF4D5358)
val Dark_Success = Color(0xFF24A148)
val Dark_Error = Color(0xFFFA4D56)

data class ColorScheme(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,

    val accent: Color,
    val onAccent: Color,

    val primaryText: Color,
    val secondaryText: Color,
    val disabledText: Color,
    val primaryIcon: Color,
    val secondaryIcon: Color,

    val error: Color,
    val onError: Color,
    val success: Color,
    val onSuccess: Color,

    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color,
    val snackbarContainer: Color,
    val onSnackbarContainer: Color,
    val seekbarThumb: Color,
    val seekbarTrack: Color,
    val seekbarActiveTrack: Color,
    val seekbarBufferedTrack: Color,
)

internal val lightColorScheme = ColorScheme(
    primary = Light_PrimaryText,
    secondary = Light_SecondaryText,
    background = Light_Background,
    onBackground = Light_PrimaryText,
    surface = Light_Surface,
    surfaceVariant = Light_Background,
    onSurface = Light_PrimaryText,
    onSurfaceVariant = Light_SecondaryText,
    accent = Light_PrimaryText,
    onAccent = Light_Surface,
    primaryText = Light_PrimaryText,
    secondaryText = Light_SecondaryText,
    disabledText = Light_DisabledText,
    primaryIcon = Light_PrimaryText,
    secondaryIcon = Light_SecondaryText,
    error = Light_Error,
    onError = Light_Surface,
    success = Light_Success,
    onSuccess = Light_Surface,
    outline = Light_Border,
    outlineVariant = Light_BorderVariant,
    scrim = Color(0xFF000000).copy(alpha = LOW),
    snackbarContainer = Light_PrimaryText,
    onSnackbarContainer = Light_Surface,
    seekbarThumb = Light_PrimaryText,
    seekbarTrack = Light_Border,
    seekbarActiveTrack = Light_PrimaryText,
    seekbarBufferedTrack = Color(0xFFDDE1E5),
)

internal val darkColorScheme = ColorScheme(
    primary = Dark_PrimaryText,
    secondary = Dark_SecondaryText,
    background = Dark_Background,
    onBackground = Dark_PrimaryText,
    surface = Dark_Surface,
    surfaceVariant = Dark_Background,
    onSurface = Dark_PrimaryText,
    onSurfaceVariant = Dark_SecondaryText,
    accent = Dark_PrimaryText,
    onAccent = Dark_Surface,
    primaryText = Dark_PrimaryText,
    secondaryText = Dark_SecondaryText,
    disabledText = Dark_DisabledText,
    primaryIcon = Dark_PrimaryText,
    secondaryIcon = Dark_SecondaryText,
    error = Dark_Error,
    onError = Dark_Surface,
    success = Dark_Success,
    onSuccess = Dark_Surface,
    outline = Dark_Border,
    outlineVariant = Dark_BorderVariant,
    scrim = Color(0xFF000000).copy(alpha = LOW),
    snackbarContainer = Dark_PrimaryText,
    onSnackbarContainer = Dark_Surface,
    seekbarThumb = Dark_PrimaryText,
    seekbarTrack = Dark_Border,
    seekbarActiveTrack = Dark_PrimaryText,
    seekbarBufferedTrack = Color(0xFF3A3F42),
)
