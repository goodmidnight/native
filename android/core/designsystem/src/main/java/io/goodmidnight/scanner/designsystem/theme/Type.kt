package io.goodmidnight.scanner.designsystem.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.goodmidnight.scanner.designsystem.R

data class Type(
    // Display styles (웅장하고 세련된 대형 텍스트)
    val displayLarge: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp
    ),
    val displayMedium: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 40.sp
    ),
    val displaySmall: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),

    // Heading styles (구조적인 타이틀 및 중요 헤더)
    val headingXXLarge: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp
    ),
    val headingXLarge: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    val headingLarge: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),
    val headingMedium: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    val headingSmall: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    val headingXSmall: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    // Label styles (버튼, 마커, 컨트롤 및 메타 텍스트)
    val labelLarge: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    val labelMedium: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    val labelSmall: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    val labelXSmall: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp
    ),

    // Paragraph styles (가독성이 뛰어난 본문 서술 텍스트)
    val paragraphLarge: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    val paragraphMedium: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    val paragraphSmall: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    val paragraphXSmall: TextStyle = TextStyle(
        fontFamily = PRETENDARD,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 14.sp
    ),
) {
    companion object {
        val PRETENDARD = FontFamily(
            Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
            Font(R.font.pretendard_black, FontWeight.Black, FontStyle.Normal),
            Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
            Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
            Font(R.font.pretendard_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
            Font(R.font.pretendard_light, FontWeight.Light, FontStyle.Normal),
            Font(R.font.pretendard_thin, FontWeight.Thin, FontStyle.Normal),
            Font(R.font.pretendard_extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
            Font(R.font.pretendard_extra_light, FontWeight.ExtraLight, FontStyle.Normal),
        )
    }
}
