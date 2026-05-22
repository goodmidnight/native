package io.goodmidnight.scanner.designsystem.theme

import androidx.compose.ui.graphics.Color
import io.goodmidnight.scanner.designsystem.theme.Alpha.LOW
import io.goodmidnight.scanner.designsystem.theme.Alpha.MEDIUM

val Purple_50 = Color(0xFFF5F0FF)
val Purple_100 = Color(0xFFEBDEFF)
val Purple_200 = Color(0xFFD9C4FF)
val Purple_300 = Color(0xFFBD9BFF)
val Purple_400 = Color(0xFFA172FF)
val Purple_500 = Color(0xFF8449FF)
val Purple_600 = Color(0xFF6A03CE)
val Purple_700 = Color(0xFF5502A5)
val Purple_800 = Color(0xFF40027C)
val Purple_900 = Color(0xFF2C0154)

// Neutrals: Gray
val Gray_White = Color(0xFFFFFFFF)
val Gray_50 = Color(0xFFF9FAFB)
val Gray_100 = Color(0xFFF2F4F7)
val Gray_200 = Color(0xFFE4E7EC)
val Gray_300 = Color(0xFFD0D5DD)
val Gray_400 = Color(0xFF98A2B3)
val Gray_500 = Color(0xFF667085)
val Gray_600 = Color(0xFF475467)
val Gray_700 = Color(0xFF344054)
val Gray_800 = Color(0xFF1D2939)
val Gray_900 = Color(0xFF101828)
val Gray_Black = Color(0xFF000000)

// Success: Green
val Green_50 = Color(0xFFECFDF3)
val Green_300 = Color(0xFF81C995)
val Green_500 = Color(0xFF34A853)
val Green_700 = Color(0xFF027A48)

// Error: Red
val Red_50 = Color(0xFFFEF3F2)
val Red_300 = Color(0xFFF2D682)
val Red_500 = Color(0xFFEA4335)
val Red_700 = Color(0xFFB42318)


/**
 * 앱의 디자인 시스템에서 사용될 시멘틱(의미론적) 색상표를 정의합니다.
 *
 * @property background 앱의 가장 바닥에 깔리는 기본 배경색입니다.
 * @property surface 카드, 다이얼로그처럼 배경 위에 떠 있는 UI 요소의 색상입니다.
 * @property surfaceVariant `surface`와 미묘한 차이를 두어 UI를 그룹화할 때 사용하는 표면색입니다.
 * @property onSurface `surface` 색상 위에 표시되는 텍스트 및 아이콘의 색상입니다.
 * @property onSurfaceVariant `surfaceVariant` 색상 위에 표시되는 텍스트 및 아이콘의 색상입니다.
 * @property accent 사용자의 주요 상호작용(버튼, 링크 등)을 위한 강조 색상입니다.
 * @property onAccent `accent` 색상 위에 표시되는 텍스트 및 아이콘의 색상입니다.
 * @property primaryText 가장 중요한 내용을 전달하는 텍스트의 색상입니다.
 * @property secondaryText 덜 중요한 부가 정보를 전달하는 텍스트의 색상입니다.
 * @property disabledText 비활성화된 컴포넌트의 텍스트 색상입니다.
 * @property primaryIcon 기본 아이콘의 색상입니다.
 * @property secondaryIcon 덜 중요한 부가 정보를 전달하는 아이콘의 색상입니다.
 * @property error 오류 상태를 나타내는 색상입니다.
 * @property onError `error` 색상 위에 표시되는 콘텐츠의 색상입니다.
 * @property success 성공 상태를 나타내는 색상입니다.
 * @property onSuccess `success` 색상 위에 표시되는 콘텐츠의 색상입니다.
 * @property outline 컴포넌트의 주요 외곽선 색상입니다.
 * @property outlineVariant 덜 중요한 장식용 외곽선이나 구분선 대용으로 사용됩니다.
 * @property scrim 모달 컴포넌트 뒷 배경을 덮는 반투명 오버레이 색상입니다.
 */
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
    primary = Purple_600,
    secondary= Purple_400,
    background = Gray_White,
    onBackground = Gray_900,
    surface = Gray_50,
    surfaceVariant = Gray_100,
    onSurface = Gray_900,
    onSurfaceVariant = Gray_600,
    accent = Gray_Black,
    onAccent = Gray_White,
    primaryText = Gray_900,
    secondaryText = Gray_500,
    disabledText = Gray_300,
    primaryIcon = Gray_900,
    secondaryIcon = Gray_500,
    error = Red_500,
    onError = Gray_White,
    success = Green_500,
    onSuccess = Gray_White,
    outline = Gray_200,
    outlineVariant = Gray_100,
    scrim = Gray_Black.copy(alpha = LOW),
    snackbarContainer = Gray_800,
    onSnackbarContainer = Gray_White,
    seekbarThumb = Purple_600,
    seekbarTrack = Gray_200,
    seekbarActiveTrack = Purple_600,
    seekbarBufferedTrack = Purple_200,
)

internal val darkColorScheme = ColorScheme(
    primary = Purple_400,
    secondary = Purple_300,
    background = Gray_Black,
    onBackground = Gray_White,
    surface = Gray_800,
    surfaceVariant = Gray_700,
    onSurface = Gray_White,
    onSurfaceVariant = Gray_400,
    accent = Gray_White,
    onAccent = Gray_Black,
    primaryText = Gray_White,
    secondaryText = Gray_400,
    disabledText = Gray_600,
    primaryIcon = Gray_White,
    secondaryIcon = Gray_400,
    error = Red_300,
    onError = Gray_Black,
    success = Green_300,
    onSuccess = Gray_Black,
    outline = Gray_700,
    outlineVariant = Gray_800,
    scrim = Gray_Black.copy(alpha = LOW),
    snackbarContainer = Gray_100,
    onSnackbarContainer = Gray_900,
    seekbarThumb = Purple_400,
    seekbarTrack = Gray_700,
    seekbarActiveTrack = Purple_400,
    seekbarBufferedTrack = Purple_900,
)
